package com.snc.quicknotes;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class Utils {
    public static final String APP_TAG = "NoteAppUsingSqlite";

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void infoLog(String tag, String msg) {
        Log.i(APP_TAG, tag + " --> " + msg);
    }


    public static void makeASnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (snackbar.isShown())
            snackbar.dismiss();
        snackbar.show();
    }
}
