package com.sunincha.broadcastssample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyLocalService extends Service {
   public static final String TAG = "MyLocalService";

    public MyLocalService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.info(TAG,"onStartCommand");
        //Lets sleep for 5 seconds and send local broadcast after

        try {
            Utils.info(TAG,"Sleeping for 5 seconds");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Utils.info(TAG,"InterruptedException");
            e.printStackTrace();
        }
        //Lets send local broadcast to be recived by MainActivity
        Intent intentBroadcast = new Intent("com.sunincha.broadcastssample.DOWNLOAD_FINISHED");
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intentBroadcast);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Utils.info(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
