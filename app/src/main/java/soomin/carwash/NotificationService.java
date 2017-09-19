package soomin.carwash;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

// (Eng Ryder) http://stackoverflow.com/questions/43432592/android-java-notification
public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer=new Timer();
        mTimer.schedule(timerTask,2000,2*1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private Timer mTimer;
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            Log.e("log","Running");
            notifiy();
        }
    };

    @Override
    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent intent=new Intent("com.company.app");
        intent.putExtra("your value","torestore");
        sendBroadcast(intent);
    }

    public void notifiy(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("RSSPullService");

        Intent mIntent=new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        PendingIntent pendingIntent=PendingIntent.getActivity(getBaseContext(),0,mIntent,0); // Intent.FLAG_ACTIVITY_NEW_TASK); // http://croute.me/275
        Context context=getApplicationContext();

        Notification.Builder builder=new Notification.Builder(context)
                .setContentTitle("T")
                .setContentText("M")
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                ;//.setSmallIcon();

        Notification notification=builder.build();
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }
}