package soomin.carwash;

import java.util.concurrent.ExecutionException;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import soomin.carwash.item.Weather_Interface;
import soomin.carwash.item.Repo;
import soomin.carwash.item.Weather;
import soomin.carwash.custom.OpenWeatherAPITask;

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


public class MainActivity extends AppCompatActivity {

    private String url = "http://api.openweathermap.org";
    private String key = "7d0203cfc7d7fb58e65e1b312ca410ef";

    @Bind(R.id.lat)
    EditText mlat;
    @Bind(R.id.lon)
    EditText mlon;
    @Bind(R.id.getWeatherBtn)
    Button getBtn;
    @Bind(R.id.tem)
    TextView tem;
    @Bind(R.id.tvLatitude)
    TextView tvLatitude;
    @Bind(R.id.tvLongtitude)
    TextView tvLongtitude;

    @OnClick(R.id.getWeatherBtn)
    public void setWeather(View view){

        String lat = mlat.getText().toString();
        String lon = mlon.getText().toString();

        //서버의 json 응답을 간단하게 변환하도록 해주는 작업
        Retrofit client = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
        //인터페이스
        Weather_Interface interFace = client.create(Weather_Interface.class);

        //Call
        Call<Repo> call = interFace.get_weather(key,Double.valueOf(lat), Double.valueOf(lon));
        call.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                if(response.isSuccessful()){
                    Repo repo = response.body();
                    tem.setText("Temp : " +repo.getMain().getTemp()
                            +"\nTempMin : " +repo.getMain().getTemp_min()
                            +"\nTempMax : " +repo.getMain().getTemp_max()
                            +"\nHumidity : " +repo.getMain().getHumidity()
                            +"\nPressure : " +repo.getMain().getPressure()
                            +"\nWind_Speed : " +repo.getWind().getWind_speed()
                            +"\nWind_deg : " +repo.getWind().getWind_deg());
                }
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "fail",Toast.LENGTH_LONG).show();
            }
        });
    }

    //로그 확인을 위해
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
    }
}


/**
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
/* 옵션메뉴 만드는 건데 당장 필요 없음
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
/**
    // MapView 참고 http://seuny.tistory.com/14
    public void getWeather(View view)
    {

        EditText tvLon = (EditText) findViewById(R.id.lon);
        String strLon = tvLon.getText().toString();
        int lon = Integer.parseInt(strLon);

        EditText tvLat = (EditText) findViewById(R.id.lat);
        String strLat = tvLat.getText().toString();
        int lat = Integer.parseInt(strLat);


        OpenWeatherAPITask t= new OpenWeatherAPITask();
        try {
            Weather w = t.execute(lon,lat).get();

            System.out.println("Temp :"+w.getTemperature());

            TextView tem = (TextView)findViewById(R.id.tem);
            String temperature = String.valueOf(w.getTemperature());

            tem.setText(temperature);
            w.getTemperature();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
*/