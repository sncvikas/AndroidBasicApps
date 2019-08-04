package com.sunincha.sampleservices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class MyAidlBoundService extends Service {
    public static final String TAG = "MyAidlBoundService";
    public MyAidlBoundService() {
    }

    @Override
    public void onCreate() {
        Utils.infoLog(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Utils.infoLog(TAG,"onBind");
        return mBinder;
    }

    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public String getServiceMessage() throws RemoteException {
            return "Hello From AIDL service";
        }
    };

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
