package com.sunincha.dialogssample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class ReminderRecurrenceDialog extends DialogFragment {

    //Create a instance of interface that has method to indicate to the host activity that which
    //item is selected
    ReminderRecurrenceSelectedListener mListener;

    //Lets initialise the listener when fragment is attached the activity
    @Override
    public void onAttach(Context context) {
        try {
            mListener = (ReminderRecurrenceSelectedListener) getActivity();
        }catch(ClassCastException ce){
            throw new ClassCastException(getActivity().toString()+" must implement ReminderRecurrenceSelectedListener interface");
        }

        super.onAttach(context);
    }

    //An interface that hosting activity must implement to get callback from the dialog
    //when an item is selected from the dialog
    public interface ReminderRecurrenceSelectedListener {
        public void onReminderRecurrenceSelected(int i);
    }

    //Lets create a integer to that holds information on which item is selected
    // set it to the default value
    int mSelectedItem = 0;

    //Let's create a dialog with items and title
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog  = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("How often ?");

        //Here we pass an array to be shown as list of radiobuttons in dialog, notice that we are
        //referring to the array defined in strings.xml
        //0, is the item that is checked by default
        //listener that gets called when user changes the choice

        alertDialog.setSingleChoiceItems(R.array.reminder_types, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Capture the new choice
                mSelectedItem  = i;
            }
        });

        //Lets have positive button that, onclicking, returns the user choice to the hosting activity
        alertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onReminderRecurrenceSelected(mSelectedItem);
            }
        });

        //Lets have a negative button that, on clicking, returns -1 to indicate that the user does
        // not want a reminder
        alertDialog.setNegativeButton("Never", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.onReminderRecurrenceSelected(-1);
            }
        });

        // Lets return  the dialog
        return alertDialog.create();
    }
}
