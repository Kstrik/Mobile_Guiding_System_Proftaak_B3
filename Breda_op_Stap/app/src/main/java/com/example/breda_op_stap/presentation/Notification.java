package com.example.breda_op_stap.presentation;

/*
    Added on 13-12-2019 - Waylon
    Usage: encounteredWaypointNotifier(title, description);
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.breda_op_stap.R;

public class Notification
{
    private String CHANNEL_WAYPOINT_ENCOUNTERED_ID = "Encountered Channel";
    private String CHANNEL_OFF_ROUTE_ID = "Off route Channel";
    private NotificationManager notificationManager;
    private Context context;

    public Notification(Context context)
    {
        this.context = context;
        //Make the channel, required for Android 8.0 and above.
        notificationManager = this.context.getSystemService(NotificationManager.class);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_WAYPOINT_ENCOUNTERED_ID, "channel1", importance);
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_OFF_ROUTE_ID, "channel2", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }

    public void notifyWaypointEncounter(String titel, String description)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, CHANNEL_WAYPOINT_ENCOUNTERED_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titel)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //TODO: Implement a check to change between Toast and Notification
        Toast.makeText(this.context, this.context.getResources().getString(R.string.notification_close_by) + " " + titel, Toast.LENGTH_SHORT).show();

        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public void notifyOffRoute()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, CHANNEL_OFF_ROUTE_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Off route")
                .setContentText(this.context.getResources().getString(R.string.notification_off_route))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(this.context.getResources().getString(R.string.notification_off_route)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Toast.makeText(this.context, this.context.getResources().getString(R.string.notification_off_route), Toast.LENGTH_SHORT).show();

        notificationManager.notify(1, builder.build());
    }
}