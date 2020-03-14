package com.amoware.fplreminder.alarm;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amoware.fplreminder.R;

import static com.amoware.fplreminder.App.CHANNEL_1_ID;
import static com.amoware.fplreminder.Constants.tagger;

/**
 * Is used to show the user a notification and the onReceive method is called when the time of an
 * alarm, that the user has set, occurs.
 * Created by amoware on 2019-12-30.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Todo show the user a notification
        Log.d(tagger(getClass()), "Hello from ReminderReceiver..");

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_1)
                .setContentTitle("Reminder")
                .setContentText("Hello world")
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notification);
    }
}
