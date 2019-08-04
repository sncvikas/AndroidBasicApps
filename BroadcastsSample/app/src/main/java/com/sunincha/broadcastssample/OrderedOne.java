package com.sunincha.broadcastssample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class OrderedOne extends BroadcastReceiver {
    public static final String TAG = "OrderedOne";

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.info(TAG, "onReceive");
        if(intent.getStringExtra("EXTRA_DO")!=null && intent.getStringExtra("EXTRA_DO").equals("ABORT")){
            Bundle bundle = new Bundle();
            bundle.putString("MESSAGE","ABORTED");
            abortBroadcast();
        }else {
            Bundle bundle = new Bundle();
            bundle.putString("MESSAGE","CONTINUED");
            setResult(Activity.RESULT_OK,null, bundle);
        }
    }
}
