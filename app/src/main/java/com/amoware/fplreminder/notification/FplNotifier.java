package com.amoware.fplreminder.notification;

import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static com.amoware.fplreminder.App.NEW_REMINDER_CHANNEL;
import static com.amoware.fplreminder.App.PREP_TEAM_CHANNEL;
import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.FplReminder;

/**
 * Created by amoware on 2022-10-22.
 */
public class FplNotifier {
    @Nullable
    private final Context mContext;
    @NonNull
    private final FplReminder mFplReminder;

    public FplNotifier(@Nullable Context context) {
        mContext = context;
        mFplReminder = new FplReminder(context);
    }

    public void pushGetYourTeamReadyNotification(@NonNull Notification notification) {
        pushNotification(PREP_TEAM_CHANNEL, notification);
    }

    public void pushIsNewReminderSetNotification(@NonNull Notification notification) {
        pushNotification(NEW_REMINDER_CHANNEL, notification);
    }

    private void pushNotification(String channelId, @NonNull Notification notification) {
        if (mContext == null) {
            Log.e(tagger(getClass()), "pushNotification of '" + channelId + "' failed, context is null");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(mContext, channelId)
                .setSmallIcon(R.drawable.football);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(android.app.Notification.PRIORITY_HIGH);
        }

        if (notification.getContentTitle() != null) {
            builder.setContentTitle(notification.getContentTitle());
        }

        if (notification.getContentText() != null) {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                    notification.getContentText()));
        }

        setSound(builder);
        setVibrate(builder);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(1, builder.build());
    }

    private void setSound(@NonNull NotificationCompat.Builder builder) {
        // Make sound depending on the user setting
        if (mFplReminder.isNotificationSound()) {
            Uri soundUri = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
            builder.setSound(soundUri);
        }
    }

    private void setVibrate(@NonNull NotificationCompat.Builder builder) {
        // Vibrate depending on the user setting
        if (mFplReminder.isNotificationVibration()) {
            builder.setVibrate(getDefaultVibratePattern());
        }
    }

    /**
     * Returns a pattern used for vibration:
     * [0]: Start without a delay
     * [1]: Vibrate for 150 milliseconds
     * [2]: Pause for 100 milliseconds
     * [3]: Vibrate for 150 milliseconds
     */
    private long[] getDefaultVibratePattern() {
        return new long[]{ 0, 150, 100, 150 };
    }
}
