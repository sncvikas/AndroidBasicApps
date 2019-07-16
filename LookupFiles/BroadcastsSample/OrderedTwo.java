package com.sunincha.broadcastssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OrderedTwo extends BroadcastReceiver {
    public static final String TAG = "OrderedTwo";

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.info(TAG, "onReceive");
        Utils.info(TAG, "Extra " + intent.getStringExtra("MESSAGE"));
    }
}
