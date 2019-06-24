package com.sunincha.dialogssample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Arrays;

public class FoodFilterTypeDialog extends DialogFragment {


    //Lets create a boolean array to hold array of selected items
    //Lets have Veg, Egg selected by default
    boolean[] mSelectedItems = new boolean[]{true, false, false, false, true};


    // Lets create an interface that activity implements to get selected items
    public interface FoodFilterTypeSelectedListener {
        //Lets pass an array of items selected
        public void onFoodFilterSelected(boolean[] array);
    }

    //Create a interface listener from the activity
    FoodFilterTypeSelectedListener mListener;

    //Lets initialise the listener with that of hosting activity
    @Override
    public void onAttach(Context context) {

        try{
            mListener = (FoodFilterTypeSelectedListener)getActivity();
        }catch(ClassCastException ce) {
            throw new ClassCastException(getActivity().toString()+" must implement FoodFilterTypeSelectedListener inteface");
        }
        super.onAttach(context);
    }

    //Lets create a dialog with multiple items selected by default
    //it also lets user select multiple items
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Filter by type");

        //lets set multiple ingredients filter to filter the food for user
        //Pass the default selection for Veg, and Egg
        alertDialog.setMultiChoiceItems(R.array.food_filter, mSelectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            //Every time user changes the choice(selects/deselects), this listener will be called
            // with information of item, and its new state
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                //Lets update our mSelectedItems with infomation
                mSelectedItems[i] = b;
            }
        });

        //Lets set a positive button that on clicking passes the list of items the user has selected
        //we basically pass the array itselt
        alertDialog.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //call interface callback
                mListener.onFoodFilterSelected(mSelectedItems);
            }
        });

        //Lets add another button to make it easy foe user to select all at once
        alertDialog.setNegativeButton("All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //This fills the array with all true items
                Arrays.fill(mSelectedItems, true);
                mListener.onFoodFilterSelected(mSelectedItems);
            }
        });

        //Return the dialog to hosting activity
        return alertDialog.create();
    }
}
