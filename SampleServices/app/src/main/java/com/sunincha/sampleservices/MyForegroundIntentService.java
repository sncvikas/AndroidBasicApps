package com.sunincha.sampleservices;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Foreground service requires permission to be added in manifest
 */
public class MyForegroundIntentService extends IntentService {

    public static final String TAG = "MyForegroundIntentService";

    public MyForegroundIntentService() {
        super("MyForegroundIntentService");
    }

    @Override
    public void onCreate() {
        Utils.infoLog(TAG, "onCreate");
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.infoLog(TAG, "onHandleIntent");
        NotificationChannel notificationChannel;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationChannel = new NotificationChannel("CHANNEL", "My Channel", NotificationManager.IMPORTANCE_HIGH);
            nm.createNotificationChannel(notificationChannel);
        }
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "CHANNEL");
        notification.setContentTitle("Foreground service");
        notification.setContentText("Service running for 10 seconds");
        notification.setSmallIcon(R.drawable.ic_launcher_foreground);
        //notify the user that u re starting a foreground service
        managerCompat.notify(1000, notification.build());

        //call this before u start yr work
        startForeground(1000, notification.build());

        try {
            Utils.infoLog(TAG, "Sleep for 5 seconds and stop");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Utils.infoLog(TAG, "onDestroy");
        super.onDestroy();
    }
}
