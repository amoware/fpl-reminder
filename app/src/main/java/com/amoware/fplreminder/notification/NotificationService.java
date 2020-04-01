package com.amoware.fplreminder.notification;

import android.content.Context;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amoware.fplreminder.R;

import static com.amoware.fplreminder.App.CHANNEL_1_ID;

/**
 * Created by amoware on 2020-03-30.
 */
public class NotificationService {

    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
    }

    public void notify(Notification notification) {
        if (notification == null || context == null) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_1);

        if (notification.getContentTitle() != null) {
            builder.setContentTitle(notification.getContentTitle());
        }

        if (notification.getContentText() != null) {
            builder.setContentText(notification.getContentText());
        }

        if (notification.getSoundUri() != null) {
            builder.setSound(notification.getSoundUri());
        }

        if (notification.getVibrationPattern() != null) {
            builder.setVibrate(notification.getVibrationPattern());
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }
}
