package com.sunincha.dialogssample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements GenderSelectionDialog.GenderPickedListener,
        ReminderRecurrenceDialog.ReminderRecurrenceSelectedListener,
        FoodFilterTypeDialog.FoodFilterTypeSelectedListener,
        LoginCustomLayoutDialog.UserCredentialsEnteredListener {

    //Lets create an instance of listview that shows different types of dialog
    ListView mListView;

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

        mListView = findViewById(R.id.list_dialogs);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        //If a the same dialog is required in multiple activities
                        // it is recommended to create dialog as the dialogFragment that can
                        // be reused in all the required activities
                        //Here this simple dialog will be used in current activity alone
                        showConfirmationDialog();
                        break;
                    case 1:
                        //Lets a show single choice items list in a fragment
                        // Its always a good idea show dialogs in fragment, so that dialogs can be
                        // used in multiple activities
                        GenderSelectionDialog genderSelectionDialog = new GenderSelectionDialog();
                        genderSelectionDialog.show(getSupportFragmentManager(), "GenderSelectionDialog");
                        break;
                    case 2:
                        //Lets show a dialog that has list of single choice items, (list of radiobuttons)
                        //difference between above and this dialog is that this dialog can have one
                        //item in the list can be selected by default when dialog is shown
                        ReminderRecurrenceDialog reminderRecurrenceDialog = new ReminderRecurrenceDialog();
                        reminderRecurrenceDialog.show(getSupportFragmentManager(), "ReminderRecurrenceDialog");
                        break;
                    case 3:
                        //Lets show a dialog with list which allows multiple selections (list of checkboxes)
                        // Here user can select multiple items and multiple items cane checked by default
                        FoodFilterTypeDialog foodFilterTypeDialog = new FoodFilterTypeDialog();
                        foodFilterTypeDialog.show(getSupportFragmentManager(), "FoodFilterTypeDialog");
                        break;
                    case 4:
                        //Lets show a dialog with custom layout, fragment here is shown as a dialog
                        LoginCustomLayoutDialog loginCustomLayoutDialog = new LoginCustomLayoutDialog();
                        loginCustomLayoutDialog.show(getSupportFragmentManager(), "LoginCustomLayoutDialog");
                        break;
                    case 5:
                        //Lets show the same custom login dialog as a full screen dialog
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //passing android.R.id.content makes the dialog acquire the entire screen
                        fragmentTransaction.add(android.R.id.content, new LoginCustomLayoutDialog());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.commit();
                        break;
                }
            }
        });
    }

    private void showConfirmationDialog() {
        //Create an instance of AlertDialog by passing context
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme_NoActionBar)); // Here we are passing theme to alertdialog 

        //set a tile
        alertDialog.setTitle("Warning!");

        //set content, this could be a list, text, or custom layout
        alertDialog.setMessage("All files will be deleted");

        //Provide actions for the dialog, action can be maximum of 3, positive, negative, neutral
        // Positive button appears on the right most
        alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Show a toast with success message
                showSnackBar("Proceed to delete");
            }
        });

        //Negative button appears on the left most
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show a toast with user cancelled message
                showSnackBar("Cancelled delete operation");
            }
        });

        // Neutral button can be like 'remind me later' kinda of actions.
        alertDialog.setNeutralButton("Remind Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Show toast to remind user later
                showSnackBar("Will remind you later");
            }
        });

        //create the dialog
        alertDialog.create();

        //show the dialog to user
        alertDialog.show();
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


    //This method is for showing a snackbar
    private void showSnackBar(String message) {
        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
    }


    //This is a callback from GenderSelectionDialog that is of type SingleChoiceList
    @Override
    public void onGenderPicked(int i) {
        showSnackBar("Selected Gender :" + getResources().getStringArray(R.array.gender_types)[i]);
    }

    //This is a callback from ReminderRecurrenceDialog that is of type SingleChoiceList but with
    // a default selection
    @Override
    public void onReminderRecurrenceSelected(int i) {
        if (i == -1) {
            showSnackBar("User doesn't want reminder");
        } else {
            showSnackBar(getResources().getStringArray(R.array.reminder_types)[i] + " reminder is set");
        }
    }

    //This is callback from FoodFilterTypeDialog that is of type MultiChoiceList
    @Override
    public void onFoodFilterSelected(boolean[] array) {
        boolean nothingSelected = true;
        //Show selected filter
        StringBuilder message = new StringBuilder("User selected ");
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                nothingSelected = false;
                message.append(getResources().getStringArray(R.array.food_filter)[i]).append(", ");
            }
        }
        if (nothingSelected) {
            showSnackBar("Defaulting to all types");
        } else
            showSnackBar(message.toString());


    }

    //This is a callback from the LoginCustomLayoutDialog that has custom layout
    @Override
    public void onUserCredentialsEntered(String username, String password) {
        showSnackBar("UserName " + username + "\nPassword " + password);
    }
}
