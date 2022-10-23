package com.amoware.fplreminder.notification;

import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static com.amoware.fplreminder.App.REMINDER_SET_CHANNEL;
import static com.amoware.fplreminder.App.PREP_TEAM_CHANNEL;
import static com.amoware.fplreminder.common.Constants.tagger;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.FplReminder;

import java.util.List;

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

    public void postGetYourTeamReadyNotification(@NonNull Notification notification) {
        postNotification(PREP_TEAM_CHANNEL, notification);
    }

    public void postReminderSetNotification(@NonNull Notification notification) {
        postNotification(REMINDER_SET_CHANNEL, notification);
    }

    private void postNotification(String channelId, @NonNull Notification notification) {
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
        return new long[]{0, 150, 100, 150};
    }

    public boolean areNotificationsDisabled() {
        if (mContext == null) {
            Log.e(tagger(getClass()), "areNotificationsDisabled, context is null");
            return false;
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!notificationManager.areNotificationsEnabled()) {
                return true;
            }

            List<NotificationChannel> channels = notificationManager.getNotificationChannels();
            for (NotificationChannel channel : channels) {
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    return true;
                }
            }
            return false;
        }

        return !notificationManager.areNotificationsEnabled();
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    public boolean shouldRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return shouldRequestPostNotificationsPermission();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean shouldRequestPostNotificationsPermission() {
        if (mContext == null) {
            Log.e(tagger(getClass()), "shouldRequestPostNotificationsPermission, context is null");
            return false;
        }
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS);
        return permission != PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void requestPermission(@NonNull ComponentActivity activity,
                                  @NonNull RequestPermissionResultCallback callback) {
        ActivityResultLauncher<String> requestPermissionLauncher =
                activity.registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        callback::onRequestPermissionResult
                );
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    public interface RequestPermissionResultCallback {
        void onRequestPermissionResult(boolean isGranted);
    }
}
