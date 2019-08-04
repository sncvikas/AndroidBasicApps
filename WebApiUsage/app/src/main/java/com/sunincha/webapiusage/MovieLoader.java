package com.sunincha.webapiusage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class MovieLoader extends AsyncTaskLoader {
    public static final String TAG = MovieLoader.class.getName();
    String mUrl;

    public MovieLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        //this runs on UI thread
        //once loader is initialised,
        // we have to call forceLoad to perform the background operation which calls loadInBackground
        forceLoad();
    }

    @Nullable
    @Override
    public Object loadInBackground() {
        //This is called in the background thread
        Utils.debugLog(TAG,"loadInBackground");

        return Utils.fetchMovieData(mUrl);
        //from here control moves back to the UI thread
    }
}
