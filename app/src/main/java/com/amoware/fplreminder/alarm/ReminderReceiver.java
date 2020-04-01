package com.amoware.fplreminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.notification.Notification;
import com.amoware.fplreminder.notification.NotificationService;
import com.amoware.fplreminder.notification.VibratorService;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Is used to show the user a notification and the onReceive method is called when the time of an
 * alarm, that the user has set, occurs.
 * Created by amoware on 2019-12-30.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(tagger(getClass()), "Hello from ReminderReceiver..");

        Notification notification = new Notification();
        notification.setContentTitle("Reminder");
        notification.setContentText("Hello world");

        FplReminder fplReminder = new FplReminder(context);

        // Make sound depending on the user setting
        if (fplReminder.isNotificationSound()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.setSound(soundUri);
        }

        // Vibrate depending on the user setting
        if (fplReminder.isNotificationVibration()) {
            VibratorService service = new VibratorService();
            notification.setVibrationPattern(service.getDefaultVibratePattern());
        }

        NotificationService notificationService = new NotificationService(context);
        notificationService.notify(notification);
    }
}
