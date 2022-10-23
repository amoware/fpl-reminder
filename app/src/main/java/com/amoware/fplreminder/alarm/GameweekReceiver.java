package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.ConnectionHandler;
import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.model.gameweek.FetchGameweeksTask;
import com.amoware.fplreminder.notification.FplNotifier;
import com.amoware.fplreminder.notification.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        new FetchGameweeksTask(mFplReminder, (gameweeks) -> {
            if (gameweeks == null || gameweeks.size() == 0) {
                gameweeks = mFplReminder.getGameweeksFromStorage();
            }
            handleDownloadedGameweeks(gameweeks);
        }).execute();
    }

    private void handleDownloadedGameweeks(@Nullable List<Gameweek> gameweeks) {
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
            Date date = DateUtil.subtractTime(
                    currentGameweek.getDeadlineTime(),
                    mFplReminder.getNotificationTimer()
            );

            if (date == null) {
                notificationTitle = mContext.getString(R.string.notification_title_remindernotset);
                notificationText = mContext.getString(R.string.notification_text_nullableDate);
            } else {
                notificationTitle = mContext.getString(R.string.notification_title_reminderset);
                String reminderSet = dateFormat.format(date);
                String deadline = dateFormat.format(currentGameweek.getDeadlineTime());
                notificationText = mContext.getString(R.string.notification_text_reminderset, reminderSet,
                        currentGameweek.getName().toLowerCase(), deadline
                );
            }
        }

        showNotification(createNotification(notificationTitle, notificationText));
    }

    private Notification createNotification(String notificationTitle, String notificationText) {
        Notification notification = new Notification();
        notification.setContentTitle(notificationTitle);
        notification.setContentText(notificationText);
        return notification;
    }

    private void showNotification(Notification notification) {
        FplNotifier notifier = new FplNotifier(mContext);
        notifier.postReminderSetNotification(notification);
    }
}
