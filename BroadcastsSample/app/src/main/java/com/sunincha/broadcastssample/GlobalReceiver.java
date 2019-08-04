package com.sunincha.broadcastssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GlobalReceiver extends BroadcastReceiver {
    public static final String TAG = "GlobalReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.info(TAG,"onReceive");
        Utils.info(TAG,intent.getAction().toString());
    }
}
