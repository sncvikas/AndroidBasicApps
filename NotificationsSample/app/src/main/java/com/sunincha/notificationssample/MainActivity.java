package com.sunincha.notificationssample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //Creating a member variable for the listview
    ListView mListView;

    //Channel Id used in Android O and above
    public static final String CHANNEL_ID = "General";

    //Create a member of notification manager compat class
    NotificationManagerCompat mNotificationManagerCompat;

    //Assign a notification ID for every notification
    public static int BASIC = 1001;

    //Counter used in progress bar notification
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instantiating the listview
        mListView = (ListView) findViewById(R.id.list);

        //Set a click listener for the list item
        mListView.setOnItemClickListener(this);


        mNotificationManagerCompat = NotificationManagerCompat.from(this);


        //If version is greater than android O, we need a notification channel to send notifications
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            //Title for the channel that is displayed in app settings
            String channelTitle = "General Notifications";

            //Description for the channel
            String channelDesc = "This channel is for general notifications from the app";

            // Importance for the channel, one of default, high, medium, low etc
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            //create a notification channel
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelTitle,
                    channelImportance);

            // Set description for the channel
            channel.setDescription(channelDesc);

            //Ask notification manager to create a channel
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 14) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID);
            startActivity(intent);
            return;
        }
        createBasicNotification(i + 1);
    }

    private void createBasicNotification(int style) {

        //Create a notification builder
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder
                (this, CHANNEL_ID);

        //Assigning an icon for notification that is displayed in status bar
        notificationBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder);


        switch (style) {
            case 1:
                //Assign notification title
                notificationBuilder.setContentTitle("Title : Basic notification");

                //this is for notification with one line text
                //Assign context text for the notification
                notificationBuilder.setContentText("Content: This is notification content");

                //Lets make this notification fully visible in lock screen
                notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                break;

            case 2:
                //Assign notification title
                notificationBuilder.setContentTitle("Title:Notification with multiline line text");

                //This is for notification that have multiline line
                //Assign a style for the notification content
                notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().
                        bigText("Content: This is notification content\n" +
                                "This is second line\nThis is third line"));

                //Lets make this notification partially visible in lock screen
                notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
                break;

            case 3:
            case 8:
                //Assign notification title
                notificationBuilder.setContentTitle("Title:Notification with an Image");

                notificationBuilder.setContentText("This is Tajmahal");

                //This is for setting a large icon for the notification that is
                // displayed on the right

                Bitmap image = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.tajmahal);
                //set icon using large icon
                notificationBuilder.setLargeIcon(image);

                if (style == 8) {
                    //Set a style to show big image on expanding the notification
                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null));
                }

                //Lets make this notification not at all visible to user when device is locked
                notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
                break;

            case 4:
                //Notification with intent to launch an activity

                //Assign a notification title
                notificationBuilder.setContentTitle("TItle : Click to open activity");

                //Create an intent
                Intent intent = new Intent(this, SecondActivity.class);
                intent.putExtra("key_title", "Notification");
                intent.putExtra("key_id", BASIC);

                //create a pending intent for the notification, similar we can create intents to
                //start a service or send a broadcast
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                notificationBuilder.setContentIntent(pendingIntent);
                break;

            case 5:
                // Notification with an action button that starts a broadcast receiver

                //Assign a title
                notificationBuilder.setContentTitle("Click an action button");

                //Create an intent to start a broadcast receiver
                Intent intentBroadcast = new Intent(this, NotificationBroadcastReceiver.class);
                PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intentBroadcast, 0);
                notificationBuilder.addAction(android.R.drawable.ic_btn_speak_now, "Broadcast", pendingIntent1);
                break;

            case 6:
                //Notification with inline reply

                //Assign a title
                notificationBuilder.setContentTitle("Enter your reply");

                //Set an intent to be launched when reply is entered
                Intent inlineReplyIntent = new Intent(this, NotificationBroadcastReceiver.class);
                inlineReplyIntent.setAction("INLINE_REPLY");
                PendingIntent inlineReplyPendingIntent = PendingIntent.getBroadcast(this, 0, inlineReplyIntent, 0);

                //Create a remoteInput action
                RemoteInput remoteInput = new RemoteInput.Builder("reply_content")
                        .setLabel("Reply")
                        .build();

                NotificationCompat.Action action = new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_btn_speak_now, "Reply",
                        inlineReplyPendingIntent).addRemoteInput(remoteInput).build();

                notificationBuilder.addAction(action);
                break;

            case 7:
                //Notification with progress bar

                //Assign a title
                notificationBuilder.setContentTitle("Downloading");

                //Setting initial progress and max
                // show the notification
                notificationBuilder.setProgress(100, 0, false);
                mNotificationManagerCompat.notify(BASIC, notificationBuilder.build());

                //start a countdowntimer for 5 seconds which ticks every second
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long l) {
                        //update the progress every second
                        notificationBuilder.setProgress(100, 20 * i++, false);
                        mNotificationManagerCompat.notify(BASIC, notificationBuilder.build());
                    }

                    @Override
                    public void onFinish() {
                        //set progress and max to same value after download is complete
                        //change the content title
                        i = 0;
                        notificationBuilder.setContentTitle("Download complete");
                        notificationBuilder.setProgress(100, 100, false);
                        mNotificationManagerCompat.notify(BASIC, notificationBuilder.build());
                    }
                }.start();

                return;
            case 10:

                //Show message style notification from multiple people

                //Create some message objects
                long time = System.currentTimeMillis();
                NotificationCompat.MessagingStyle.Message message1 = new NotificationCompat.MessagingStyle.Message("Dinner tonight?", time - 5000, "Naveen");
                NotificationCompat.MessagingStyle.Message message2 = new NotificationCompat.MessagingStyle.Message("Yes", time - 4000, "Prashant");
                NotificationCompat.MessagingStyle.Message message3 = new NotificationCompat.MessagingStyle.Message("Travelling :(", time - 3000, "Ravi");

                //set messaging style
                notificationBuilder.setStyle(new NotificationCompat.MessagingStyle("Me").
                        setConversationTitle("Friends").
                        addMessage(message1).
                        addMessage(message2).
                        addMessage(message3));
                break;

            case 11:
                //Notification that disappears after 5 seconds
                notificationBuilder.setContentTitle("Disappear after 5 seconds");
                notificationBuilder.setTimeoutAfter(5000);
                break;

            case 12:
                //create a headsup notification
                createNotificationWithHighImportance();
                return;
            case 13:
                //Cancel all notifications
                mNotificationManagerCompat.cancelAll();
                return;

            case 14:
                //Create custom notification layout
                createCustomNotificationLayout();
                return;

        }

        //set ongoing to true to make notification persistent, it cant be cleared by the user
        //notificationBuilder.setOngoing(true);

        //When user taps on it to open an activity, notification will be cleared.
        notificationBuilder.setAutoCancel(true);

        //set priority, this will be takenover by channel importance in android 8 and above
        notificationBuilder.setPriority(NotificationManager.IMPORTANCE_DEFAULT);

        //Finally show the notification
        mNotificationManagerCompat.notify(BASIC, notificationBuilder.build());

        return;
    }

    private void createCustomNotificationLayout() {
        //create a remote custom layout xml for notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);

        //set a style to fit custom layout
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());

        //set the custom view to notification
        builder.setCustomContentView(remoteViews);

        //show the notification
        NotificationManagerCompat.from(this).notify(BASIC, builder.build());
    }

    private void createNotificationWithHighImportance() {
        String channelId = "ALERT";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String title = "HighAlert";
            String desc = "Very important notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, title, importance);
            channel.setDescription(desc);
            channel.setLightColor(android.R.color.holo_blue_bright);
            channel.enableLights(true);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId);
        notification.setSmallIcon(android.R.drawable.ic_lock_power_off);
        notification.setContentText("Headsup notification");
        notification.setContentTitle("High Alert");
        notification.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManagerCompat.from(this).notify(1002, notification.build());
    }

}