package com.sunincha.sampleservices;

import android.app.IntentService;
import android.content.Intent;

public class MyIntentService extends IntentService {
    public static final String TAG = "MyIntentService";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        Utils.infoLog(TAG, "onCreate");
        super.onCreate();
    }

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.infoLog(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
*/

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            String message = intent.getStringExtra("SERVICE_MESSAGE");
            Utils.infoLog(TAG, "Extra Message " + message);
        }
        try {
            Utils.infoLog(TAG, "Sleeping for 5 seconds");
            Utils.infoLog(TAG, "Send another intent before 5 seconds to queue it");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Notice that IntentService is stopped automatically without explicitly calling stopSelf
    @Override
    public void onDestroy() {
        Utils.infoLog(TAG, "onDestroy is called after intents are served \nor when explicitly called by used");
        super.onDestroy();
    }
}
