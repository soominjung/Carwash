package soomin.carwash.item;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Soomin Jung on 2017-08-25.
 * 목표
 * 1. AsyncTask와 HTTPURLConnection을 이용한 간단한 HTTP 호출 만들기
 * 2. 리턴된 JSON을 파싱하는 방법을 통하여, JSON 객체 다루는 법 습득하기
 * 3. Phone Location (GPS) API 사용 방법 파악하기
 *
 * 참고 자료 : http://developer.android.com/training/basics/network-ops/connecting.html
 * */

public class OpenWeatherAPIClient {
    final static String openWeatherURL = "http://api.openweathermap.org/data/2.5/weather";
    public Weather getWeather(int lat,int lon){
        Weather w = new Weather();
        String urlString = openWeatherURL + "?lat="+lat+"&lon="+lon+"&appid=7d0203cfc7d7fb58e65e1b312ca410ef";

        try {
            // call API by using HTTPURLConnection
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            //urlConnection으로부터 getInputStream메서드를 통해 InputStream을 리턴받음
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //getStringFromInputStream으로 inputStream을 String으로 변환한 후 JSONObject로 변환
            JSONObject json = new JSONObject(getStringFromInputStream(in));

            // parse JSON
            w = parseJSON(json);
            w.setIon(lon);
            w.setLat(lat);

        }catch(MalformedURLException e){
            System.err.println("Malformed URL");
            e.printStackTrace();
            return null;

        }catch(JSONException e) {
            System.err.println("JSON parsing error");
            e.printStackTrace();
            return null;
        }catch(IOException e){
            System.err.println("URL Connection failed");
            e.printStackTrace();
            return null;
        }

        // set Weather Object

        return w;
    }

    private Weather parseJSON(JSONObject json) throws JSONException {
        Weather w = new Weather();
        //getJSONObject("main")을 이용해서 "main" json문서를 얻고 "temp"의 값을 읽어 세팅
        w.setTemperature(json.getJSONObject("main").getInt("temp"));
        w.setCity(json.getString("name"));
        //w.setCloudy();

        return w;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
