package com.snc.actiivtytransitions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    public static final String TAG = "SecondActivity";

    //Lets declare text views to show data passed from main activity
    TextView mTxtName;
    TextView mTxtLegs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        infoLog("onCreate");

        //Lets initialise views to show data
        mTxtName = findViewById(R.id.txt_name);
        mTxtLegs = findViewById(R.id.txt_legs);

        //Lets get the intent with which this activity was called
        Intent myIntent = getIntent();

        //Lets get each intent extras
        String name = myIntent.getStringExtra("NAME");

        int legs = myIntent.getIntExtra("LEGS",-1);
        //Notice that in above statement if intent extra is not found, -1 will returned by default

        //Now lets set the values to the respective views
        mTxtName.setText(name);
        mTxtLegs.setText(String.valueOf(legs));

    }

    //helper function to print logs
    private void infoLog(String msg){
        Log.i("ActivityTransitions",TAG+" --> "+msg);
    }

    /*
        Here I'm again implementing all the activity transitions methods to observe the flow of
        activity. Please look into logs for the log messages with filter "ActivityTransitions"
     */
    @Override
    protected void onStart() {
        super.onStart();
        infoLog("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        infoLog("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        infoLog("onPause");
    }


    @Override
    protected void onStop() {
        super.onStop();
        infoLog("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoLog("onDestroy");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        infoLog("onRestart");
    }

}
