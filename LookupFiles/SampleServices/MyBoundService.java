package com.sunincha.sampleservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyBoundService extends Service {
    public static final String TAG = "MyBoundService";
    MyBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        Utils.infoLog(TAG,"onCreate");
        super.onCreate();
    }

    public MyBoundService() {
    }

    public class MyBinder extends Binder {

        MyBoundService getServiceBinder() {
            Utils.infoLog(TAG, "getServiceBinder");
            return MyBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Utils.infoLog(TAG,"onBind");
        return binder;
    }

    public String getServiceMessage(){
        Utils.infoLog(TAG,"getServiceMessage");
        return "Hello There !!";
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Utils.infoLog(TAG, "onUnBind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Utils.infoLog(TAG, "onDestroy");
        super.onDestroy();
    }
}
