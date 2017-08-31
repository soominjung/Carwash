package soomin.carwash.custom;

import android.os.AsyncTask;
import soomin.carwash.item.*;

/**
 * Created by Soomin Jung on 2017-08-25.
 */

public class OpenWeatherAPITask extends AsyncTask<Integer, Void, Weather> {
    @Override
    public Weather doInBackground(Integer... params) {
        OpenWeatherAPIClient client = new OpenWeatherAPIClient();

        int lat = params[0];
        int lon = params[1];
        // API 호출
        Weather w = client.getWeather(lat,lon);

        //System.out.println("Weather : "+w.getTemperature());

        // 작업 후 리턴
        return w;
    }
}
