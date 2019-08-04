package com.snc.actiivtytransitions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //String that helps in filtering the logs
    public static final String TAG = "MainActivity";

    //User input from edit text
    EditText mEdiAnimalName;
    EditText mEditNumberOfLegs;

    //Button to listen to when user clicks
    Button mBtnSendData;


    //onCreate is the first method called by the android system when the activity is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoLog("onCreate");
        //Lets initialise each ui object
        mEdiAnimalName = findViewById(R.id.edit_animal);
        mEditNumberOfLegs = findViewById(R.id.edit_number_of_legs);

        //Lets initialise button object
        mBtnSendData = findViewById(R.id.btn_start_activity);

        //Lets assigns button click listener
        mBtnSendData.setOnClickListener(new View.OnClickListener() {

            //This method will be called when user taps the button
            @Override
            public void onClick(View view) {

                //Lets take user input and send that to another activity
                String name = mEdiAnimalName.getText().toString().trim();
                String numbeOfLegs = mEditNumberOfLegs.getText().toString().trim();


                //Lets see if any of them is empty, if so, ask user to enter input by showing a toast
                if (name == null || name.isEmpty() || numbeOfLegs == null || numbeOfLegs.isEmpty()) {
                    showToast("Please enter both values");
                    return;
                }
                // Lets check if user has entered any non-integer for the number of legs input

                int legsCount = 0;
                try {
                    legsCount = Integer.parseInt(numbeOfLegs);
                } catch (NumberFormatException ne) {
                    showToast("Please enter an integer for legs");
                    //lets clear the current input for legs
                    mEditNumberOfLegs.setText("");
                    ne.printStackTrace();
                    return;
                }


                //Intent extras are used to send data between activities
                //Lets create an intent to start another activity
                //Note that since we know which activity to start, we are mentioning the name of that activity
                //this is called Explicit Activity
                Intent newActivity = new Intent(MainActivity.this, SecondActivity.class);

                //Lets put some extras into the intent
                newActivity.putExtra("NAME", name);
                newActivity.putExtra("LEGS", legsCount);

                //Lets now start that activity using the intent that we have build above
                startActivity(newActivity);

            }
        });


    }

    //helper method to show a toast
    private void showToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    //helper method to print log to logcat
    private void infoLog(String msg) {
        Log.i("ActivityTransitions", TAG + " --> " + msg);
    }


    /*
        Below I am implementing all the activity life cycle methods to understand the transitioning
        of the activity, I'm printing the logs into logcat for every start. Please filter the logcat
        with "ActivityTransitions" string to see the logs
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
