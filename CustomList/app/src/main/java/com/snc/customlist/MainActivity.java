package com.snc.customlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    //This is the actual list view thats holds the custom list items
    ListView mListView;

    //This is our custom class for list adapter, this decides which UI xml to be picked for the list
    //row
    CustomListAdapter mCustomListAdapter;

    //I will be storing the list of installed applications in this arraylist
    ArrayList<InstalledAppInfo> mInstalledApps;

    //This is interface callback is for getting notified when user clicks on the item more info
    CustomListAdapter.InfoIconClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.infoLog(TAG,"onCreate");
        //Lets initialise the list that have in the UI
        mListView = findViewById(R.id.list_custom);

        //Actual implementation of showing information on the clicked application such as,
        // version, name, package name, app name, app icon in a dialog
        mListener = new CustomListAdapter.InfoIconClickListener() {
            @Override
            public void onInfoIconClicked(int i) {
                Utils.infoLog(TAG,"onInfoIconClicked, position "+ String.valueOf(i));
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setIcon(mInstalledApps.get(i).getIcon());
                dialog.setTitle(mInstalledApps.get(i).getName());
                dialog.setMessage(String.format("PackageName\n\t%s\nVersion\n\t%s",
                        mInstalledApps.get(i).getPackageName(), mInstalledApps.get(i).getVersion()));
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.create().show();

            }
        };
        mInstalledApps = new ArrayList<>();
        /*
            Lets create an async task that actually pulls information on the list of installed
            applications
         */
        new LoadAppInfoTask().execute();

    }

    class LoadAppInfoTask extends AsyncTask<Void, Integer, Integer> {


        /* Asynctask has three arguments
            1. the array for the input the user needs for doInBackground function, Void in our case
            2. input for the publishprogress function called from within doInBackground
            3. output of doInBackground function that goes as input for onPostExecute
        */


        //Creating a progress dialog to be shown while we are loading the application info
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.infoLog(TAG,"onPreExecute");

            //This is called before we start the background work

            //Lets start showing the progressbar
            progressDialog.setMessage("Loading application info...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Utils.infoLog(TAG,"doInBackground");
            //This is core of the asynctask where the actual work is done

            //We are loading the application information for all the installed applications
            // and store the data in separate class
            final PackageManager packageManager = getPackageManager();
            final List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(0);
            PackageInfo pInfo = null;
            for (ApplicationInfo info : installedApps) {
                try {
                    pInfo = packageManager.getPackageInfo(info.packageName, 0);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                mInstalledApps.add(new InstalledAppInfo(packageManager.getApplicationIcon(info), info.loadLabel(getPackageManager()).toString(), pInfo.versionName, info.packageName));

                //Here we are updating the progress bar with dynamic data
                publishProgress(mInstalledApps.size());
            }
            return mInstalledApps.size();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //This is called from publish progress

            //Lets show the current count in the dialog message
            if (progressDialog.isShowing()) {
                progressDialog.setMessage("Loading application info " + String.valueOf(values[0]));
            }
        }

        @Override
        protected void onPostExecute(Integer appCount) {
            super.onPostExecute(appCount);
            Utils.infoLog(TAG,"onPostExecute");
            //This is end of the asynctak work

            //Lets dismiss the progress dialog
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Utils.showToast(getBaseContext(), String.format("Loaded information of %d apps", appCount));

            //Here I'm creating a new custom adapter adapter
            // we pass context of the activity, and list of apps installed, and callback listener to
            //listen to when user clicks on the more info icon
            mCustomListAdapter = new CustomListAdapter(getBaseContext(), mInstalledApps, mListener);

            //Attach our custom adapter to the list UI
            mListView.setAdapter(mCustomListAdapter);

        }

    }


}
