package com.example.breda_op_stap.logic;
/*
    Added on 13-12-2019 - Waylon
    Usage: encounteredWaypointNotifier("Hart van Breda", "Eetcafe in het centrum.");
 */

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.breda_op_stap.R;

public class Notification {
    String CHANNEL_ID = "Notification Channel";
    NotificationManager notificationManager;
/*
    private Notification(Context context) {
        this.context = context;
        //Make the channel, required for Android 8.0 and above.
        notificationManager = getBaseContext().getSystemService(NotificationManager.class);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "channel", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void encounteredWaypointNotifier (String titel, String description) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titel)
                .setContentText(description)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(description))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Toast.makeText(getBaseContext(), "Je bent nu in de buurt van: " + titel, Toast.LENGTH_SHORT).show();

        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
*/
}