package soomin.carwash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 1500);// 3 초
    }
}
