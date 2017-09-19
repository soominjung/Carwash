package soomin.carwash;

/**
 * Created by Soomin Jung on 2017-09-18.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


// 앱 실행시에 필요한 권한을 처리하기 위한 액티비티

public class PermissionActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100;

    //화면을 구성하고 SDK버전과 권한을 처리.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if(Build.VERSION.SDK_INT < 23){
            goIndexActivity();
        } else {
            if(checkAndRequestPermissions()){
                goIndexActivity();
            }
        }
    }

    //권한을 확인하고 권한이 부여되어있지 않다면 요청한다. 모든 권한이 부여되면 true
    private boolean checkAndRequestPermissions() {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;

    }

    //권한 요청 결과를 받는 메소드
    //requestCode:요청코드, permissions:권한종류, grantResults:권한결과
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length == 0) return;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goIndexActivity();
                } else {
                    showPermissionDialog();
                }
                return;
        }
    }


    //권한 설정 화면으로 이동할지 선택하는 다이얼로그를 보임
    private void showPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("권한 설정");
        dialog.setMessage("권한이 필요합니다");
        dialog.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Toast.makeText(PermissionActivity.this, "권한 설정 후 재시작",Toast.LENGTH_LONG).show();
                finish();
                goAppSettingActivity();
            }
        });
        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }


    //현재 엑티비티를 종료하고 인덱스로 넘어감
    private void goIndexActivity() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    //권한을 설정할 수 있는 설정 액티비티를 실행
    private void goAppSettingActivity(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }


}
