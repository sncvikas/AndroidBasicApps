package com.sunincha.sampleservicesclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sunincha.sampleservices.IMyAidlInterface;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "MainActivity";

    //Create variable for listview
    ListView mList;

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

        // intent for messenger service
        Intent messengerServiceIntent = new Intent();
        //create a component for the destination service
        messengerServiceIntent.setComponent(new ComponentName("com.sunincha.sampleservices", "com.sunincha.sampleservices.MyBindServiceMessenger"));


        //Another intent for AIDL service
        Intent aidlServiceIntent = new Intent();
        aidlServiceIntent.setComponent(new ComponentName("com.sunincha.sampleservices", "com.sunincha.sampleservices.MyAidlBoundService"));


        switch (i) {
            case 0:
                //bind to Messenger service
                Utils.infoLog(TAG, "Binding to service in different app via messenger");
                if (bindService(messengerServiceIntent, mMessengerConnection, BIND_AUTO_CREATE))
                    Utils.infoLog(TAG, "Bound successfully");
                else
                    Utils.infoLog(TAG, "Failed to bind");


                break;
            case 1:
                //unbind from the service
                if (myBindServiceMessenger != null)
                    unbindService(mMessengerConnection);
                break;

            case 2:
                //bind to aidl service
                Utils.infoLog(TAG, "Binding to service in different app via AIDL");
                if (bindService(aidlServiceIntent, mAidlConnection, BIND_AUTO_CREATE))
                    Utils.infoLog(TAG, "Bound successfully to AIDL service");
                else
                    Utils.infoLog(TAG, "Error in binding to AIDL service");
                break;

            case 3:
                //Unbinding from AIDL service
                if (aidlInterfaceHandle != null)
                    unbindService(mAidlConnection);
                break;
        }
    }

    //Create a handler for MessengerService
    Messenger myBindServiceMessenger;

    //Create a serviceConnection for Messenger service
    ServiceConnection mMessengerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            Utils.infoLog(TAG, "connected to MyBindServiceMessenger");
            myBindServiceMessenger = new Messenger(iBinder);

            //create a message object
            Message msg = Message.obtain(null, 0, 0, 0);
            try {
                Utils.infoLog(TAG, "Sending message to MyBindServiceMessenger");
                myBindServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Utils.infoLog(TAG, "Disconnected from MyBindServiceMessenger");
            myBindServiceMessenger = null;
        }
    };

    //AIDL bound service handler
    IMyAidlInterface aidlInterfaceHandle = null;

    //Service connection for AIDL
    ServiceConnection mAidlConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Utils.infoLog(TAG, "Connected to AIDL service");
            aidlInterfaceHandle = IMyAidlInterface.Stub.asInterface(iBinder);
            //get message aidl
            try {
                String message = aidlInterfaceHandle.getServiceMessage();
                Utils.infoLog(TAG, "Message is " + message);

            } catch (RemoteException e) {
                Utils.infoLog(TAG, "Error is getting message from AIDL service");
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Utils.infoLog(TAG, "Disconnected from AIDL service");
            aidlInterfaceHandle = null;
        }
    };

}
