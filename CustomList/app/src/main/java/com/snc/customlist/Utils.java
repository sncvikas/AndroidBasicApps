package com.snc.customlist;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {
    public static final String APP_TAG = "CustomList";
    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void infoLog(String tag, String msg) {
        Log.i(APP_TAG,tag +" --> "+msg);
    }
}
