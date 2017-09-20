package soomin.carwash;

/**
 * Created by Soomin Jung on 2017-09-20.
 */

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import soomin.carwash.item.Repo;
import soomin.carwash.item.Weather_Interface;

public class WidgetProvider extends AppWidgetProvider {

    public static double ycoord = 0.0D;
    public static double xcoord = 0.0D;

    Double latitude = 37.566229;
    Double longitude = 126.977689;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d("WidgetProvider", "onUpdate() called : " + ycoord + ", " + xcoord);

        final int size = appWidgetIds.length;

        for (int i = 0; i < size; i++) {
            int appWidgetId = appWidgetIds[i];


            Intent intent = new Intent(context,MainActivity.class);


            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.txtInfo, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        context.startService(new Intent(context,GPSLocationService.class));
    }


    public static class GPSLocationService extends Service {
        public static final String TAG = "GPSLocationService";

        private LocationManager manager = null;

        private LocationListener listener = new LocationListener() {

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }

            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged() called.");

                setWheatherText(location.getLatitude(), location.getLongitude());

                stopSelf();
            }
        };

        public IBinder onBind(Intent intent) {
            return null;
        }

        public void onCreate() {
            super.onCreate();

            Log.d(TAG, "onCreate() called.");

            manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        }

        public int onStartCommand(Intent intent, int flags, int startId) {
            startListening();

            return super.onStartCommand(intent, flags, startId);
        }

        public void onDestroy() {
            stopListening();

            Log.d(TAG, "onDestroy() called.");

            super.onDestroy();
        }

        private void startListening() {
            Log.d(TAG, "startListening() called.");

            final Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            final String bestProvider = manager.getBestProvider(criteria, true);

            if (bestProvider != null && bestProvider.length() > 0) {
                manager.requestLocationUpdates(bestProvider, 500, 10, listener);
            } else {
                final List<String> providers = manager.getProviders(true);

                for (final String provider : providers) {
                    manager.requestLocationUpdates(provider, 500, 10, listener);
                }
            }
        }

        private void stopListening() {
            try {
                if (manager != null && listener != null) {
                    manager.removeUpdates(listener);
                }

                manager = null;
            } catch (final Exception ex) {

            }
        }

        public void setWheatherText(double latitude, double longitude) {
            String units = "metric";

            String url = "http://api.openweathermap.org/";
            String key = "7d0203cfc7d7fb58e65e1b312ca410ef";
            String info = "";

            Retrofit client = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).client(createOkHttpClient()).build();
            Weather_Interface interFace = client.create(Weather_Interface.class);
            Call<Repo> call = interFace.get_weather(key,latitude,longitude,units,"14");
            call.enqueue(new Callback<Repo>() {
                @Override
                public void onResponse(Call<Repo> call, Response<Repo> response) {
                    if(response.isSuccessful()){
                        Repo repo = response.body();
                        //String text = "";

                        int noRainCnt=0;

                        for(int i=0;i<repo.getList().size();i++){
                            int id = repo.getList().get(i).getList2().get(0).getId();
                            if(id/100!=2&&id/100!=3&&id/100!=5&&id/100!=6){
                                noRainCnt++;
                            }else {
                                break;
                            }
                        }

                        showText(noRainCnt);
                    }
                }

                @Override
                public void onFailure(Call<Repo> call, Throwable t) {
                    Log.d(TAG, "fail");
                }
            });
/*
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

            views.setTextViewText(R.id.txtInfo,info);
            //views.setInt(R.id.wlayout, "setBackgroundColor", Color.rgb(9, 123, 172));


            ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);*/

            xcoord = longitude;
            ycoord = latitude;
            Log.d(TAG, "coordinates : " + latitude + ", " + longitude);

        }

        public void showText(int noRain) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);
            if (noRain < 3) {
                views.setTextViewText(R.id.txtInfo, "오늘 세차하면\n안돼요.");
                views.setInt(R.id.wlayout, "setBackgroundColor", Color.argb(200,101,114,122));
            } else if (noRain > 2 && noRain < 5) {
                views.setTextViewText(R.id.txtInfo, "오늘 세차해도\n괜찮아요.");
                views.setInt(R.id.wlayout, "setBackgroundColor", Color.argb(200,10, 119, 166));
            } else if (noRain > 4 && noRain < 6) {
                views.setTextViewText(R.id.txtInfo, "오늘 세차하면\n좋아요 :)");
                views.setInt(R.id.wlayout, "setBackgroundColor", Color.argb(200, 0, 169, 217));
            }else {
                views.setTextViewText(R.id.txtInfo, "오늘 꼭\n세차하세요 :D");
                views.setInt(R.id.wlayout, "setBackgroundColor", Color.argb(200,36, 183, 198));
            }

            ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
        }
    }



    private static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder.build();
    }

}
