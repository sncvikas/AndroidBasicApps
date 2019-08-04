package com.sunincha.dialogssample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginCustomLayoutDialog extends DialogFragment implements View.OnClickListener {

    //Create edittext instances for storing username, password
    EditText mEditUserName;
    EditText mEditPassword;

    //Create button instances for clear and login
    Button mBtnLogin;
    Button mBtnClear;

    // Declare a view for the layout
    View view;

    //Inflate a layout for your custom dialog, here it is login page
    //return the layout in onCreateView lifecycle method of this fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_dialog_login, container ,false);

        return view;
    }

    //Set dialog to be without title
    //This is called only when the fragment is hosted as a dialog in current activity
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }



    //interface for sending data entered back to the hosting activity
    public interface UserCredentialsEnteredListener {
        public void onUserCredentialsEntered(String username, String password);
    }

    //Create an instance of the interface of the hosting activity
    UserCredentialsEnteredListener mListener = null;

    //Lets initialise the listener of the activity
    @Override
    public void onAttach(Context context) {
        try{
            mListener = (UserCredentialsEnteredListener)getActivity();
        }catch(ClassCastException ce){
            throw new ClassCastException(getActivity().toString()+" must implement UserCredentialsEnteredListener interface");
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mEditPassword = view.findViewById(R.id.password);
        mEditUserName = view.findViewById(R.id.username);
        mBtnClear = view.findViewById(R.id.clear);
        mBtnLogin = view.findViewById(R.id.login);


        mBtnLogin.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);

        super.onActivityCreated(savedInstanceState);
    }

    //This is called when buttons clear/login are clicked
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.clear:
                //Clear both username and password fields
                mEditUserName.setText("");
                mEditPassword.setText("");
                break;
            case R.id.login:
                //check if both fields are entered by the user
                String userName = mEditUserName.getText().toString().trim();
                String password = mEditPassword.getText().toString().trim();
                if(userName.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(),"Please enter both fields",Toast.LENGTH_SHORT).show();
                }else{
                    //Lets send the data back to the hosting activity
                    mListener.onUserCredentialsEntered(userName,password);
                    dismiss();
                }
                break;
        }
    }
}
