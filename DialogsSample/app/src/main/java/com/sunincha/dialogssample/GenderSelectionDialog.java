package com.sunincha.dialogssample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class GenderSelectionDialog extends DialogFragment {

    //Lets create a sample to display single choice items
    String[] choices = new String[]{"Male", "Female"};

    GenderPickedListener listener;

    //All the lifecycle methods of a fragment can be used used
    //Lets use onAttach ot create an instance of interface listener from activity
    @Override
    public void onAttach(Context context) {
        //Create an instance of GenederPickedListen interface of the hosting activity
        try {
            listener = (GenderPickedListener) getActivity();
        } catch (ClassCastException ce) {
            throw new ClassCastException(getActivity().toString() + " must implement GenederPickedListener interface");
        }
        super.onAttach(context);
    }

    //Override this method to create a dialog and return this dialog to the host that is showing the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        //Use AlertDialog to create a dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Gender?");

        //Set the array of items to be listed in the dialog
        //Note that only one list item can be selected
        //Assign an onClickListener which is invoked when item is selected
        alertDialog.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //When an item is selected, pass the selected item to the hosting activity
                listener.onGenderPicked(i);
            }
        });

        //Return the dialog to the hosting activity
        return alertDialog.create();
    }

    //Lets create an interface that has methods to indicate the the item is picked from the dialog
    //This interface has to be implemented in the dialog hosting activity
    public interface GenderPickedListener {
        public void onGenderPicked(int i);
    }
}
