package com.sunincha.notificationssample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        TextView textView = (TextView) findViewById(R.id.message);
        Intent intent = getIntent();
        String title = intent.getStringExtra("key_title");
        int id = intent.getIntExtra("key_id",-1);
        StringBuilder stringBuilder = new StringBuilder(title);
        stringBuilder.append("\n");
        stringBuilder.append("This activity was launched from notification with ID " +String.valueOf(id));
        textView.setText(stringBuilder);
    }
}
