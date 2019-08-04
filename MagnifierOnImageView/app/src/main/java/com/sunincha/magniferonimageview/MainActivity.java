package com.sunincha.magniferonimageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Magnifier;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Lets get an instance of image view and text view
        ImageView imageView = (ImageView) findViewById(R.id.img);
        TextView textView = (TextView) findViewById(R.id.image_msg);


        //Create a magnifier for the imageView
        final Magnifier magnifier = new Magnifier(imageView);

        //set on touch listener for the imageview, which is triggered when you touch the imageview
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int[] viewLocation = new int[2];

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:

                        //Getting current location of the view that is touched
                        view.getLocationOnScreen(viewLocation);

                        // lets show the magnifier at x and y co-ordinates
                        magnifier.show(motionEvent.getRawX() - viewLocation[0], motionEvent.getRawY() - viewLocation[1]);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        //Lets dismiss the magnifier when finger is removed off the screen
                        magnifier.dismiss();
                        break;
                }
                return true;
            }
        });


        //We are creating and applying magnifer for the text view as well.
        final Magnifier magnifierTxt = new Magnifier(textView);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        final int[] viewLocation = new int[2];

                        view.getLocationOnScreen(viewLocation);
                        magnifierTxt.show(motionEvent.getRawX() - viewLocation[0], motionEvent.getRawY() - viewLocation[1]);

                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        magnifierTxt.dismiss();
                        break;
                }
                return true;
            }
        });
    }
}
