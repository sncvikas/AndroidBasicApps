package com.sunincha.broadcastssample;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = "MainActivity";

    // variable for holding listview
    ListView mListView;
    final IntentFilter airplaneMode = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);

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

        //Create a instance of list view
        mListView = findViewById(R.id.list_of_items);

        //Assign click listener
        mListView.setOnItemClickListener(this);


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

        Intent intent;
        final ComponentName bluetoothStateReceiver = new ComponentName("com.sunincha.broadcastssample","com.sunincha.broadcastssample.BluetoothStateChangeReceiver");
        final PackageManager packageManager = getPackageManager();

        switch(i){
            case 0:
                Toast.makeText(getBaseContext(),"Turn on Bluetooth to recieve the intent",Toast.LENGTH_LONG).show();
                break;
            case 1:
                //Disable a manifest receiver
                //after disabling we no longer get to receive the intent
                packageManager.setComponentEnabledSetting(bluetoothStateReceiver,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                break;
                //After this, even if you send an broadcast it wont be received by the receiver
            case 2:
                //Enable a manifest receiver
                //after disabling we no longer get to receive the intent
                packageManager.setComponentEnabledSetting(bluetoothStateReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                break;
                //After this, we will start receiving the broadcast
            case 3:
                //Lets register for airplane mode changed receiver
                registerReceiver(airplaneModeReceiver, airplaneMode);
                //now turn on Airplane mode to receive the intents
                break;
            case 4:
                //lets unregister airplane mode change listener receiver
                unregisterReceiver(airplaneModeReceiver);
                //even if you turn off airplane mode now, we wont receive intent
                break;
            case 5:
                //Send a broadcast
                //Other apps can also receive, but here only we will be receiving it
                //ReceiverOne.java is the receiver here
                intent = new Intent("com.sunincha.broadcastssample.GLOBAL");

                sendBroadcast(intent);
                //You will not receive this intent as Background execution is not allowed for
                //implicit broadcast
                //to make is explicit broadcast, let set component name
                //intent.setComponent(new ComponentName(getPackageName(),getPackageName()+".GlobalReceiver"));

                break;
            case 6:
                //Send a local broadcast
                //To demonstrate this lets start a service, and in that service after completion of
                //its work, it sends local broadcast, which we receive in our activity
                //first register
                IntentFilter intentFilter = new IntentFilter("com.sunincha.broadcastssample.DOWNLOAD_FINISHED");
                LocalBroadcastManager.getInstance(getBaseContext()).registerReceiver(localBroadcastReceiver, intentFilter);

                //now lets start the service
                intent = new Intent(MainActivity.this, MyLocalService.class);
                startService(intent);
                break;
            case 7:
                //send ordered broadcast
                intent = new Intent("com.sunincha.broadcastssample.ORDERED");
                intent.setPackage(getPackageName());
                sendOrderedBroadcast(intent,null);
                break;
            case 8 :
                //send an ordered broadcast to and receive data back , send it again to subsequent
                //receivers
                intent = new Intent("com.sunincha.broadcastssample.ORDERED");
                intent.putExtra("EXTRA_DO","ABORT");
                intent.setPackage(getPackageName());
                sendOrderedBroadcast(intent,null, orderedBroadcastResultReceiver, null, Activity.RESULT_OK,null, null);
            case 9 :
                //send an ordered broadcast to and receive data back , send it again to subsequent
                //receivers
                intent = new Intent("com.sunincha.broadcastssample.ORDERED");
                intent.putExtra("EXTRA_DO","CONTINUE");
                intent.setPackage(getPackageName());
                sendOrderedBroadcast(intent,null, orderedBroadcastResultReceiver, null, Activity.RESULT_OK,null, null);

        }
    }

    //Lets create a receiver for dynamically registered broadcast receivers
    //this one is for airplane mode change intent
    BroadcastReceiver airplaneModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                Utils.info(TAG,"onReceive");
                Utils.info(TAG,intent.getAction().toString());
        }
    };
    //This receiver is deleivered with intent from the local service
    //after we receive the intent, we stop the service
    BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.info(TAG,"onReceive");
            Utils.info(TAG,intent.getAction().toString());
            Intent serviceIntent = new Intent(context,MyLocalService.class);
            stopService(serviceIntent);
        }
    };

    BroadcastReceiver orderedBroadcastResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Utils.info(TAG,"onReceive");

            if(getResultCode() == RESULT_CANCELED) {
                Utils.info(TAG,"orderedBroadcastResultReceiver");
                Utils.info(TAG,"RESULT_CANCELED, Aborting broadcast");
                setResultCode(RESULT_CANCELED);
            }else{
                Utils.info(TAG,"orderedBroadcastResultReceiver");
                Utils.info(TAG,"RESULT_OK");
                Utils.info(TAG,intent.getStringExtra("MESSAGE"));
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
    }
}
