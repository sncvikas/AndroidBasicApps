package com.sunincha.sampleservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //list items to perform different service operations
    ListView mList;
    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Initialise the list
        mList = findViewById(R.id.list_items);

        //set onclick listeners for the list items
        mList.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //This is invoked when an item is list is tapped

        //Lets keep an startedService variable to start different services
        Intent startedService = new Intent(MainActivity.this, MyStartedService.class);

        //IntentService intent
        Intent intentService = new Intent(MainActivity.this, MyIntentService.class);

        //Bound service
        Intent boundService = new Intent(MainActivity.this, MyBoundService.class);


        //foreground service
        Intent foregroundService = new Intent(MainActivity.this, MyForegroundIntentService.class);

        switch (i) {
            case 0:
                //start a startedservicesample
                //start a non sticky service
                startedService.putExtra("SERVICE_RESTART_TYPE", 2);
                startedService.putExtra("SERVICE_MESSAGE", "Its a non sticky service");
                startService(startedService);
                //Now run adb shell am force-stop com.sunincha.sampleservices within 5 seconds to force stop the application
                //from logcat notice that our service is not restarted at all
                break;
            case 1:
                //start a MyStartedService
                //start a sticky service with string extra
                startedService.putExtra("SERVICE_RESTART_TYPE", 1);
                startedService.putExtra("SERVICE_MESSAGE", "Its a sticky service");
                startService(startedService);
                //Now run adb shell am force-stop com.sunincha.sampleservices within 5 seconds to force stop the application
                //from logcat notice that our service is restarted but startedService is null
                break;
            case 2:
                //start a MyStartedService
                //start a service  with redeliver startedService with string extra
                startedService.putExtra("SERVICE_RESTART_TYPE", 3);
                startedService.putExtra("SERVICE_MESSAGE", "Its a sticky service with redeliver startedService");
                startService(startedService);
                //Now run adb shell am force-stop com.sunincha.sampleservices within 5 seconds to force stop the application
                //from logcat notice that our service is restarted with same startedService as above
                break;
            case 3:
                //stop the above service explicitly
                stopService(startedService);
                break;
            case 4:
                //start an intent service
                //put an extra message
                intentService.putExtra("SERVICE_MESSAGE","Intent Message 1");
                startService(intentService);
                break;
            case 5:
                //queue an intent again for same the intent service
                intentService.putExtra("SERVICE_MESSAGE","Intent Message 2");
                startService(intentService);
                break;
            case 6:
                //stop an intent service
                stopService(intentService);
                break;
            case 7:
                //bind to service within the same process
                //last parameter tell to create the service if it does not already exist
                if(bindService(boundService, mMyBoundServiceConn,BIND_AUTO_CREATE))
                    Utils.infoLog(TAG, "Bound successfully");
                else
                    Utils.infoLog(TAG, "Error in binding");
                break;
            case 8:
                //Unbind from the service
                //this stops the service
                unbindService(mMyBoundServiceConn);
                break;
            case 9:
                //start a foreground service
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(foregroundService);
                }else{
                    Utils.infoLog(TAG,"Not supported in current version of Android");
                }
                break;
        }
    }

    //Keep an handle to bound service
    MyBoundService boundServiceHandle;

    //Just Create  a service connection for MyBindService
    ServiceConnection mMyBoundServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            //service got connected
            Utils.infoLog(TAG,"onServiceConnected");

            //Get an instance of binder
            MyBoundService.MyBinder binder = (MyBoundService.MyBinder) iBinder;
            boundServiceHandle = binder.getServiceBinder();
            //Call an api is binder service to get message
            String msg = boundServiceHandle.getServiceMessage();
            Utils.infoLog(TAG,"Message from service "+msg);
            Toast.makeText(getBaseContext(),"Service said "+msg,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //service got disconnected
            Utils.infoLog(TAG,"onServiceDisconnected");
            boundServiceHandle = null;
        }
    };



}
