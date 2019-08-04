package com.sunincha.sampleservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyStartedService extends Service {
    public static final String TAG = "MyStartedService";

    @Override
    public void onCreate() {
        Utils.infoLog(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Utils.infoLog(TAG, "onStartCommand, Id "+startId);
        int serviceType = -1;
        String message = "null message";
        if (intent != null){
            serviceType = intent.getIntExtra("SERVICE_RESTART_TYPE", 0);
            message = intent.getStringExtra("SERVICE_MESSAGE");
        }

        Utils.infoLog(TAG, "-------Intent Extras-------");
        Utils.infoLog(TAG, "Service Type "+(intent==null?-1:serviceType));
        Utils.infoLog(TAG, "Service Message "+message);
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Utils.infoLog(TAG,"Sleeping for 15 seconds");
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                   Utils.infoLog(TAG,"InterruptedException");
                    e.printStackTrace();
                }
                //stop the service for current session
               // stopSelf(startId);
            }
        }).start();*/
        //here we specify how should be restarted when is stopped by the system
        return serviceType;
    }

    @Override
    public void onDestroy() {
        Utils.infoLog(TAG,"onDestroy");
        super.onDestroy();
    }
}
