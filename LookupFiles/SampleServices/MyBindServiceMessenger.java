package com.sunincha.sampleservices;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class MyBindServiceMessenger extends Service {
    public static final String TAG = "MyBindServiceMessenger";


    public MyBindServiceMessenger() {
    }

    static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Utils.infoLog(TAG, "handleMessage");
            switch (msg.what) {
                case 0:
                    Utils.infoLog(TAG, "Client said Hello");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    Messenger mMessenger;

    @Override
    public IBinder onBind(Intent intent) {
        Utils.infoLog(TAG, "onBind");
        mMessenger = new Messenger(new IncomingHandler());
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.infoLog(TAG,"onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Utils.infoLog(TAG,"onDestroy");
        super.onDestroy();
    }
}
