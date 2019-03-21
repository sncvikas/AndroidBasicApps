package com.snc.basicbluetooth;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

     /*
         Here i'm creating class variables for all the UI elements that i will be using at runtime
         Im declaring variables here so that I can make use of this in all the methods inside the class
    */

    //Create variable to change discovered devices  title when discovery is in progress
    TextView mTxtDiscoveryTitle;

    //I need switch variable to get notified when its toggled
    Switch mSwitchEnableBt;

    //Two listviews to update whenever respective data is available
    ListView mListPairedDevices;
    ListView mListDiscoveredDevices;

    //Lets create another variable for BluetoothAdapter to perform Bluetooth specific operations
    BluetoothAdapter mBtAdapter;

    /*
        Lets create two lists that hold the actual values of paired devices and discovered devices
        We are creating arrays of type String
    */
    ArrayList<String> mPairedDevicesArray;
    ArrayList<String> mDiscoveredDevicesArray;

    /*
        We also need two arrayadapters that bind to the listviews of the xml and show the data that they are told to show
        So actual flow is like this "ArrayList--->ArrayAdapter--->ListView"
    */
    ArrayAdapter<String> mPairedDeviceAdapter;
    ArrayAdapter<String> mDiscoveredDevicesAdapter;



    /*
        This method is the first one to be called by android system when you open the application
        You can go through activity life cycle in android
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //here we are setting which xml to be applied for the current activity or the ui

        setContentView(R.layout.activity_main);

        int bleScanningState = -1;
        final String BLE_SCAN_KEY = "ble_scan_always_enabled";
        try {
            bleScanningState = Settings.Global.getInt(getContentResolver(),BLE_SCAN_KEY);
            if(bleScanningState == 1)
                Settings.Global.putInt(getContentResolver(), BLE_SCAN_KEY, 0);
            else
                Settings.Global.putInt(getContentResolver(), BLE_SCAN_KEY, 0);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(getBaseContext(),"CurrentState is "+String.valueOf(bleScanningState), Toast.LENGTH_LONG).show();



        /*
            Since android M, we have check if we have permission for location to get discovered devices
            Lets check that permission
            Calling an api that takes cares of checking/requesting for the permission
         */
        checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        //here we have to initialize each view with its corresponding id from the xml
        mTxtDiscoveryTitle = findViewById(R.id.txt_title_discovery);
        mSwitchEnableBt = findViewById(R.id.switch_enable_bt);
        mListPairedDevices = findViewById(R.id.list_paired);
        mListDiscoveredDevices = findViewById(R.id.list_discovered);

        /*
            Lets initialise the lists that hold the actual values of paired devices and discovered devices
         */
        mPairedDevicesArray = new ArrayList<>();
        mDiscoveredDevicesArray = new ArrayList<>();

        /*
            Here lets initialise the arrayadapters
        */
        /*
            Lets assign each array to corresponding adapter
            getBaseContext, tells which life cycle does the UI belong to, here it getBaseContext will
            be alive as long as activity is alive
            simple_list_item_1 is predefined android provided template for list items
            mPairedDeviceArray is telling which array this adapter belongs to
        */
        mPairedDeviceAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mPairedDevicesArray);
        mDiscoveredDevicesAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, mDiscoveredDevicesArray);

        /*
            Now lets assign which arrayadapter belongs to which listview in UI
        */
        mListPairedDevices.setAdapter(mPairedDeviceAdapter);
        mListDiscoveredDevices.setAdapter(mDiscoveredDevicesAdapter);


        /*
        Lets initialise Bluetooth adapter here, so its available for the entire
        life cycle of the activity
        */
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        /*
        Now that we have Bluetooth adapter, lets check if Bluetooth is already enabled,
        if yes, lets set the switch state to be on, else leave it as is
        */
        boolean isBtEnabled = mBtAdapter.isEnabled();

        if (isBtEnabled) {
            /*
            Since now that I have initialised switch object, i can call any APIs that belong to switch
             */
            mSwitchEnableBt.setChecked(true);

            //If Bluetooth is ON, lets get paired devices list and assign it to corresponding array adapter
            //calling another function that shows paired devices
            showPairedDevices();

            //setup discovery process
            setUpDiscovery();
        } else {
            //If Bluetooth is off when app is launched lets show a toast message to
            Toast.makeText(getBaseContext(), "Please turn on Bluetooth", Toast.LENGTH_LONG).show();
        }

        /*
            Here we are defining what we do when the Switch is toggled, we are just setting a callabck
            that will be invoked by android system
            You can look for 'how to listen for switch change events in android'
        */
        mSwitchEnableBt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*
                this will be called by android system whenever switch is toggled
                Here goes the actual logic
                CompoundButton is superclas of switch, u can ignore it for now
                boolean b is true if switch is enabled, false if switch is disabled
                */
                if (b) {
                    //Now turn on Bluetooth
                    mBtAdapter.enable();
                } else {
                      //turn off bluetooth
                    mBtAdapter.disable();

                }
            }
        });
        //Lets have a single entry point for registering broadcast receivers for all our receiver
    }


    //This will be called after onCreate in activity life cycle
    @Override
    protected void onStart() {
        super.onStart();
        //Lets register receivers here
        registerAllRecievers();
    }

    //This will be called after app goes in background
    @Override
    protected void onStop() {
        super.onStop();
        //lets unregisters are receivers here
        unregisterAllReceivers();

    }

    private void registerAllRecievers() {
        // ------------discovery------------
        // create a filter for discovered devices
        IntentFilter discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        //add another filter to know when actuall discovery started
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        //add another filter to know when actuall discovery finished
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //register receiver for newly discovered devices
        registerReceiver(mDeviceFoundReceiver, discoveryFilter);
        // ------------discovery end------------

        // ------------state change start------------

        //Lets tell the system what intent(event) our broadcast receiver is looking for
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

        //register the reoeiver to receive above intent
        registerReceiver(mBtStateChangeReceiver, filter);
        // ------------state change end------------


    }

    private void unregisterAllReceivers(){

        //unregister receivers so that we no longer receive intents
        if(mBtStateChangeReceiver != null) {
            unregisterReceiver(mBtStateChangeReceiver);
        }
        if(mDeviceFoundReceiver != null) {
            unregisterReceiver(mDeviceFoundReceiver);
        }
    }

    protected void checkLocationPermission(String myPermission) {
        int perm = ContextCompat.checkSelfPermission(getApplicationContext(), myPermission);
        if (perm != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{myPermission},
                    12);
        } else if (perm == PackageManager.PERMISSION_GRANTED) {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getBaseContext(), "Please provide permission to see discovered devices", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    BroadcastReceiver mBtStateChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //When Bt state changes, android system calls this

            //intent has some extra data associated with it, lets take that data that specifies the new state
            // See "what intent extras are"
            int newState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);

            /*
                If new state is ON,
                1. show paired devices devices
                2. Register for newly discovered device and start discovery, so that we will get to
                know when new devices are found
            */
            if (newState == BluetoothAdapter.STATE_ON) {
                //call the showPairedDevices to list paired devices
                showPairedDevices();
                setUpDiscovery();
                Toast.makeText(context, "Bluetooth is ON now!!", Toast.LENGTH_LONG).show();
            } else if (newState == BluetoothAdapter.STATE_OFF) {
                Toast.makeText(context, "Bluetooth is OFF now!!", Toast.LENGTH_LONG).show();
            }
        }
    };

    BroadcastReceiver mDeviceFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //This will be called as and when a new BLuetooth device is found in the vicinity

            //Lets check which of the three intent it is
            if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
                //if discovery started change the tile
                mTxtDiscoveryTitle.setText("Discovering...");
                Toast.makeText(context, "Discovery started !!", Toast.LENGTH_LONG).show();
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                //if discovery finished, revert the title
                mTxtDiscoveryTitle.setText("Discovered Devices");
                Toast.makeText(context, "Discovery finished !!", Toast.LENGTH_LONG).show();
            } else {
                //Android system sends BluetoothDevice object as part of the extras in the intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //Lets call showDiscoveredDevices method that takes care of showing new devices
                showDiscoveredDevices(device);
            }

        }
    };

    public void showPairedDevices() {
        //I'm creating another Set here just because bonded devices returns a Set of BluetoothDevice by default

        Set<BluetoothDevice> pairedDevicesSet = mBtAdapter.getBondedDevices();

        //Lets clear if already some data is in the array
        if (mPairedDevicesArray.size() != 0) {
            mPairedDevicesArray.clear();
            mPairedDeviceAdapter.notifyDataSetChanged();
        }

        //Lets convert that set to an array, by iterating through the set and adding the address and name to the array

        String tempName;
        String tempAddress;
        BluetoothDevice bd;

        Iterator<BluetoothDevice> iterator = pairedDevicesSet.iterator();
        while (iterator.hasNext()) {
            //Know that now iterator has a BluetoothDevice
            bd = iterator.next();

            // Address will never be null, assign address to tempAddress
            tempAddress = bd.getAddress();

            //if name is null add empty name, else add its name
            if (bd.getName() != null)
                tempName = bd.getName();
            else
                tempName = "";

                /*
                    Now that we have name and address , merge them both
                    and add it to the paired devices list
                */
            mPairedDevicesArray.add(tempName + " " + tempAddress);

                /*
                    Now that we added an item to the list
                    Lets tell the adapter to update the UI everytime an item is added to it
                */
            mPairedDeviceAdapter.notifyDataSetChanged();

        }

    }

    public void showDiscoveredDevices(BluetoothDevice device) {
        String tempAddress = device.getAddress();
        String tempName;
        //Assigning address and name
        if (device.getName() != null) {
            tempName = device.getName();
        } else {
            tempName = "";
        }

        //Now that we have name and address, merge them
        // add them to the array of discovered devices
        mDiscoveredDevicesArray.add(tempName + " " + tempAddress);

        //Notify the adapter to show the updated list of devices
        mDiscoveredDevicesAdapter.notifyDataSetChanged();

    }

    public void setUpDiscovery() {

        //Lets clear if already some data is int he discovered devices array
        if (mDiscoveredDevicesArray.size() != 0) {
            mDiscoveredDevicesArray.clear();
            mDiscoveredDevicesAdapter.notifyDataSetChanged();
        }
        //start the actual discovery
        mBtAdapter.startDiscovery();

    }
}

