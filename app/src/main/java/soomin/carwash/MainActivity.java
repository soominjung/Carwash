package soomin.carwash;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kimkevin.cachepot.CachePot;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import soomin.carwash.adapter.SpinnerAdapter;
import soomin.carwash.item.CityPositionList;
import soomin.carwash.item.CustomTextView;
import soomin.carwash.item.Repo;
import soomin.carwash.item.Weather_Interface;
import soomin.carwash.item.rainReport;


public class MainActivity extends AppCompatActivity {

    private String url = "http://api.openweathermap.org/";
    private String key = "7d0203cfc7d7fb58e65e1b312ca410ef";

    Double latitude = 37.566229;
    Double longitude = 126.977689;

    int pos;
    private CityPositionList cpList = new CityPositionList();
    List<CityPositionList.Position> cityPositionList = cpList.getCpList();

    FragmentManager fm = getFragmentManager();
    WeatherFragment wf;
    FragmentTransaction fragmentTransaction;

    @Bind(R.id.tem)
    CustomTextView tem;
    @Bind(R.id.ivWeather)
    ImageView ivWeather;
    @Bind(R.id.mLayout)
    ConstraintLayout mLayout;
    @Bind(R.id.tvDescription)
    TextView tvDescription;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @OnClick(R.id.getWeatherBtn)
    public void setWeather() {

        String units = "metric";

        Retrofit client = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        Weather_Interface interFace = client.create(Weather_Interface.class);
        Call<Repo> call = interFace.get_weather(key,latitude,longitude,units,"14");
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if(response.isSuccessful()){
                    Repo repo = response.body();

                    CachePot.getInstance().push(repo);

                    if (!isFinishing() && !isDestroyed()) {
                        wf=new WeatherFragment();
                        fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentBorC, wf);
                        fragmentTransaction.commitAllowingStateLoss();
                    }

                    List<rainReport> rRep= new ArrayList<rainReport>();

                    int noRainCnt=0;
                    int rrCnt=0;
                    int max=0;
                    int maxInx=0;
                    rRep.add(new rainReport(0,0));

                    for(int i=0;i<repo.getList().size();i++){
                        int id = repo.getList().get(i).getList2().get(0).getId();
                        if(id/100!=2&&id/100!=3&&id/100!=5&&id/100!=6){
                            noRainCnt++;
                            if(i==repo.getList().size()-1){
                                rRep.get(rrCnt).setNoRain(noRainCnt);
                                rRep.add(new rainReport(i+1,0));
                                rrCnt++;
                            }
                        }else {
                            rRep.get(rrCnt).setNoRain(noRainCnt);
                            rRep.add(new rainReport(i+1,0));
                            rrCnt++;
                            noRainCnt = 0;
                        }
                    }
                    for (int i = 0; i < rrCnt; i++) {
                        if (max < rRep.get(i).getNoRain()) {
                            max = rRep.get(i).getNoRain();
                            maxInx = i;
                        }
                    }
                    showMessage(rRep.get(maxInx).getNoRain(),rRep.get(maxInx).getAfterRain());
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Toast.makeText(MainActivity.this,"인터넷 연결 상태를 확인해주세요.",Toast.LENGTH_LONG).show();

            }
        });

    }

    public void showMessage(int longestNoRain,int afterRain) {
        if(longestNoRain>2){
            if(afterRain==0)
                tvDescription.setText("오늘부터 "+longestNoRain+"일 간 비/눈소식 없음");
            else
                tvDescription.setText(afterRain+"일 후부터 "+longestNoRain+"일 간 비/눈소식 없음");
        }
        else
            tvDescription.setText("2주간 잦은 비/눈소식");
        if (longestNoRain < 3) {
            mLayout.setBackgroundColor(Color.rgb(101,114,122));
            toolbar.setBackgroundColor(Color.rgb(81,94,92));
            tem.setText("당분간\n세차하면 안돼요 :(");
            ivWeather.setImageResource(R.drawable.rain);
        } else if (longestNoRain<4) {
            if(afterRain==0) {
                mLayout.setBackgroundColor(Color.rgb(10, 119, 166));
                toolbar.setBackgroundColor(Color.rgb(0,99,149));
                tem.setText("오늘 세차해도\n괜찮아요.");
                ivWeather.setImageResource(R.drawable.cloudcc);
            }
            else {
                mLayout.setBackgroundColor(Color.rgb(10, 119, 166));
                toolbar.setBackgroundColor(Color.rgb(0,99,146));
                tem.setText(afterRain + "일 후에 세차해도\n나쁘지 않아요.");
                ivWeather.setImageResource(R.drawable.cloud);
            }
        } else if (longestNoRain<6){
            if(afterRain==0) {
                mLayout.setBackgroundColor(Color.rgb(0, 169, 217));
                toolbar.setBackgroundColor(Color.rgb(0,149,197));
                tem.setText("오늘 세차하면\n좋아요 :)");
                ivWeather.setImageResource(R.drawable.cloudnsuncc);
            }
            else {
                mLayout.setBackgroundColor(Color.rgb(0, 169, 217));
                toolbar.setBackgroundColor(Color.rgb(0,149,197));
                tem.setText(afterRain + "일 후에\n세차하면 좋아요 :)");
                ivWeather.setImageResource(R.drawable.cloudnsun);
            }
        } else {
            if(afterRain==0) {
                mLayout.setBackgroundColor(Color.rgb(36, 183, 198));
                toolbar.setBackgroundColor(Color.rgb(16,163,178));
                tem.setText("오늘 꼭!\n세차하세요:D");
                ivWeather.setImageResource(R.drawable.suncc);
            }
            else {
                mLayout.setBackgroundColor(Color.rgb(36, 183, 198));
                toolbar.setBackgroundColor(Color.rgb(16,163,178));
                tem.setText(afterRain + "일 후는\n세차하기 좋은 날!");
                ivWeather.setImageResource(R.drawable.sun);
            }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void startLocationService(){
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
            // 위치 확인이 안되는 경우 최근 확인된 위치 정보 먼저 확인
             Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }
    }

    //리스너 클래스 정의
    public class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(pos==0)
                setWeather();
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }

    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar); //툴바를 액션바와 같게

        if (!isFinishing() && !isDestroyed()) {
            wf=new WeatherFragment();
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fragmentBorC, wf);
            fragmentTransaction.commit();
        }

        startLocationService();
        setWeather();

        Spinner s1 = (Spinner) findViewById(R.id.spinner);
        String[] test= {"현위치","서울","부산","대구","인천","광주","대전","울산","경기남부","경기북부","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주"};
                SpinnerAdapter s1Adapter = new SpinnerAdapter(this,android.R.layout.simple_spinner_item, test);
        s1.setAdapter(s1Adapter);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                pos = position;
                if(pos!=0){
                    latitude=cityPositionList.get(pos).getLat();
                    longitude=cityPositionList.get(pos).getLon();
                }
                setWeather();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //각각의 버튼을 클릭할 때 수행할것을 정의
        switch (item.getItemId()){
            case R.id.action_bt1:
                Intent intent = new Intent(getApplicationContext(),MapActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}