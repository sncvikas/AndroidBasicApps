package com.sunincha.broadcastssample;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothStateChangeReceiver extends BroadcastReceiver {
    public static final String TAG = "BluetoothStateChangeReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.info(TAG,"onReceive");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(intent.getAction().toString()).append("\n");
        stringBuilder.append("New State ").append("\n");
        stringBuilder.append(String.valueOf(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1)));
        stringBuilder.append("\n");
        Utils.info(TAG, stringBuilder.toString());
    }
}
