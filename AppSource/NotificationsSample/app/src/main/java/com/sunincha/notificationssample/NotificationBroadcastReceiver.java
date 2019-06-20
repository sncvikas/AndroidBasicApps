package com.sunincha.notificationssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if(intent.getAction() != null && intent.getAction().equals("INLINE_REPLY")) {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("This was an inline reply\n");
            stringBuilder.append("Message is \""+getMessageText(intent)+"\"");

            Toast.makeText(context,stringBuilder,Toast.LENGTH_LONG).show();


            //Show another notification to indicate that reply was sent, so as to hide input button
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context,MainActivity.CHANNEL_ID);
            notification.setSmallIcon(android.R.drawable.ic_popup_reminder);
            notification.setContentText(getMessageText(intent));
            NotificationManagerCompat.from(context).notify(MainActivity.BASIC,notification.build());

        }else
            Toast.makeText(context,"Notification has sent a broadcast",Toast.LENGTH_LONG).show();
    }
    //Retrieve the message user has entered
    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence("reply_content");
        }
        return null;
    }
}
