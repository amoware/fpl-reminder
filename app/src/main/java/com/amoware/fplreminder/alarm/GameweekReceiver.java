package com.amoware.fplreminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.ConnectionHandler;
import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.gameweek.GameweeksTask;
import com.amoware.fplreminder.notification.Notification;
import com.amoware.fplreminder.notification.NotificationService;
import com.amoware.fplreminder.notification.VibratorService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Is used to download gameweeks from fpl and set two new alarms based on the user preference and
 * on the next gameweek's deadline. The class' onReceive method is called when the time of a
 * gameweek's deadline occurs.
 * Created by amoware on 2019-12-30.
 */
public class GameweekReceiver extends BroadcastReceiver {

    private Context context;
    private FplReminder fplReminder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(tagger(getClass()), "Hello from GameweekReceiver..");
        this.context = context;
        this.fplReminder = new FplReminder(context);

        ConnectionHandler connectionHandler = new ConnectionHandler(context);
        if (connectionHandler.isNetworkAvailable()) {
            downloadGameweeks();
        } else {
            showNotification(createNotification(context.getString(R.string.notification_title_nointernet), context.getString(R.string.notification_text_nointernet)));
        }
    }

    private void downloadGameweeks() {
        GameweeksTask task = new GameweeksTask(this::handleDownloadedGameweeks);
        task.execute();
    }

    private void handleDownloadedGameweeks(List<Gameweek> gameweeks) {
        fplReminder.onGameweeksDownloaded(gameweeks);
        Gameweek currentGameweek = fplReminder.getCurrentGameweek();

        String notificationTitle, notificationText;
        if (gameweeks == null) {
            notificationTitle = context.getString(R.string.notification_title_remindernotset);
            notificationText = context.getString(R.string.notification_text_nogameweeks);
        } else if (currentGameweek == null || currentGameweek.getDeadlineTime() == null) {
            notificationTitle = context.getString(R.string.notification_title_remindernotset);
            notificationText = context.getString(R.string.notification_text_nodeadline);
        } else {
            DateFormat dateFormat = new SimpleDateFormat("EEE d MMM hh:mm", new Locale("en"));
            notificationTitle = context.getString(R.string.notification_title_reminderset);
            String reminderSet = dateFormat.format(DateUtil.subtractTime(currentGameweek.getDeadlineTime(), fplReminder.getNotificationTimer()));
            String deadline = dateFormat.format(currentGameweek.getDeadlineTime());
            notificationText = context.getString(R.string.notification_text_reminderset, reminderSet, currentGameweek.getName().toLowerCase(), deadline);
        }

        showNotification(createNotification(notificationTitle, notificationText));
    }

    private Notification createNotification(String notificationTitle, String notificationText) {
        Notification notification = new Notification();
        notification.setContentTitle(notificationTitle);
        notification.setContentText(notificationText);

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

        return notification;
    }

    private void showNotification(Notification notification) {
        NotificationService notificationService = new NotificationService(context);
        notificationService.notify(notification);
    }

}
