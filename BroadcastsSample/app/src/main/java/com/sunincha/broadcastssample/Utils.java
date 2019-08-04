package com.sunincha.broadcastssample;

import android.util.Log;

public class Utils {
    public static final String TAG = "BroadcastsSample";
    public static void info(String tag, String message){
        Log.i(TAG,tag+" >> "+message);
    }
}
