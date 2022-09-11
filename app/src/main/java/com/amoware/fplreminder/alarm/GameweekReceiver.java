package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

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
import com.amoware.fplreminder.model.gameweek.GameweeksTask;
import com.amoware.fplreminder.model.gameweek.GameweeksTaskInterface;
import com.amoware.fplreminder.notification.Notification;
import com.amoware.fplreminder.notification.NotificationService;
import com.amoware.fplreminder.notification.VibratorService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Is used to download gameweeks from fpl and set two new alarms based on the user preference and
 * on the next gameweek's deadline. The class' onReceive method is called when the time of a
 * gameweek's deadline occurs.
 * Created by amoware on 2019-12-30.
 */
public class GameweekReceiver extends BroadcastReceiver {
    private Context mContext;
    private FplReminder mFplReminder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(tagger(getClass()), "Hello from GameweekReceiver..");
        mContext = context;
        mFplReminder = new FplReminder(context);

        ConnectionHandler connectionHandler = new ConnectionHandler(context);
        if (connectionHandler.isNetworkAvailable()) {
            downloadGameweeks();
        } else {
            handleDownloadedGameweeks(mFplReminder.getGameweeksFromStorage());
        }
    }

    private void downloadGameweeks() {
        GameweeksTask task = new GameweeksTask(new GameweeksTaskInterface() {
            @Override
            public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
                if (gameweeks == null || gameweeks.size() == 0) {
                    gameweeks = mFplReminder.getGameweeksFromStorage();
                }
                handleDownloadedGameweeks(gameweeks);
            }

            @Override
            public void writeBootstrapStaticContentToFile(String content) {
                mFplReminder.writeGameweekContentToFile(content);
            }
        });
        task.execute();
    }

    private void handleDownloadedGameweeks(List<Gameweek> gameweeks) {
        mFplReminder.onGameweeksDownloaded(gameweeks);
        Gameweek currentGameweek = mFplReminder.getCurrentGameweek();

        String notificationTitle, notificationText;
        if (gameweeks == null) {
            notificationTitle = mContext.getString(R.string.notification_title_remindernotset);
            notificationText = mContext.getString(R.string.notification_text_nogameweeks);
        } else if (currentGameweek == null || currentGameweek.getDeadlineTime() == null) {
            notificationTitle = mContext.getString(R.string.notification_title_remindernotset);
            notificationText = mContext.getString(R.string.notification_text_nodeadline);
        } else {
            DateFormat dateFormat = new SimpleDateFormat("EEE d MMM HH:mm", new Locale("en"));
            notificationTitle = mContext.getString(R.string.notification_title_reminderset);
            String reminderSet = dateFormat.format(DateUtil.subtractTime(currentGameweek.getDeadlineTime(),
                    mFplReminder.getNotificationTimer()));
            String deadline = dateFormat.format(currentGameweek.getDeadlineTime());
            notificationText = mContext.getString(R.string.notification_text_reminderset, reminderSet,
                    currentGameweek.getName().toLowerCase(), deadline);
        }

        showNotification(createNotification(notificationTitle, notificationText));
    }

    private Notification createNotification(String notificationTitle, String notificationText) {
        Notification notification = new Notification();
        notification.setContentTitle(notificationTitle);
        notification.setContentText(notificationText);

        // Make sound depending on the user setting
        if (mFplReminder.isNotificationSound()) {
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notification.setSound(soundUri);
        }

        // Vibrate depending on the user setting
        if (mFplReminder.isNotificationVibration()) {
            VibratorService service = new VibratorService();
            notification.setVibrationPattern(service.getDefaultVibratePattern());
        }

        return notification;
    }

    private void showNotification(Notification notification) {
        NotificationService notificationService = new NotificationService(mContext);
        notificationService.notify(notification);
    }
}
