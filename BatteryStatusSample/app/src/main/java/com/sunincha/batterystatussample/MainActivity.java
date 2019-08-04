package com.sunincha.batterystatussample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "BatteryInfo";
    TextView mState;
    TextView mPercentage;
    TextView mCharging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mState = findViewById(R.id.txt_value_state);
        mPercentage = findViewById(R.id.txt_value_percent);
        mCharging = findViewById(R.id.txt_value_is_charging);

        updateBatteryInfo();
        //Lets register receivers for different battery intents
        IntentFilter batteryIntents = new IntentFilter();
        batteryIntents.addAction(Intent.ACTION_POWER_CONNECTED);
        batteryIntents.addAction(Intent.ACTION_POWER_DISCONNECTED);
        batteryIntents.addAction(Intent.ACTION_BATTERY_CHANGED);
        batteryIntents.addAction(Intent.ACTION_BATTERY_LOW);
        batteryIntents.addAction(Intent.ACTION_BATTERY_OKAY);
        registerReceiver(batteryInfoReceiver, batteryIntents);


    }

    BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            printLog("onReceive");
            printLog(intent.getAction().toString());
            updateBatteryInfo();
        }
    };

    public void updateBatteryInfo() {
        printLog("updateBatteryInfo");

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //See that we are not passing receiver at this point
        //since Intent.ACTION_BATTERY_CHANGED is a sticky broadcast
        //BatteryManager returns whatever data it has immediately
        Intent batteryState = getBaseContext().registerReceiver(null, intentFilter);

        if (batteryState == null) {
            Toast.makeText(getBaseContext(), "No battery state found", Toast.LENGTH_LONG).show();
            return;
        }

        int batteryStatus = batteryState.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        String chargingState = "Unknown";

        switch (batteryStatus) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                chargingState = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                chargingState = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                chargingState = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                chargingState = "Unknown";
        }
        int pluggedState = batteryState.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = pluggedState == BatteryManager.BATTERY_PLUGGED_USB;
        boolean isAcCharge = pluggedState == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryState.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryState.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPercentage = (level / (float) scale) * 100;

        String chargeMode = "Not available";
        if (usbCharge)
            chargeMode = "USB";
        else if (isAcCharge)
            chargeMode = "AC Power";

        mState.setText(chargingState);
        mCharging.setText(chargeMode);
        mPercentage.setText(String.valueOf(batteryPercentage) + "%");
    }

    private void printLog(String message) {
        Log.i(TAG, message);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(batteryInfoReceiver);
        super.onDestroy();

    }
}

