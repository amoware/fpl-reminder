package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.amoware.fplreminder.R;
import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.notification.FplNotifier;
import com.amoware.fplreminder.notification.Notification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Is used to show the user a notification and the onReceive method is called when the time of an
 * alarm, that the user has set, occurs.
 * Created by amoware on 2019-12-30.
 */
public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(tagger(getClass()), "Hello from ReminderReceiver..");

        FplReminder fplReminder = new FplReminder(context);

        Notification notification = new Notification();
        notification.setContentTitle(context.getString(R.string.notification_title_reminder));

        String reminderText = getReminderText(fplReminder.getCurrentGameweekFromStorage());
        notification.setContentText(reminderText == null ?
                "Something is wrong" :
                context.getString(R.string.notification_text_reminder, reminderText)
        );

        FplNotifier notifier = new FplNotifier(context);
        notifier.pushGetYourTeamReadyNotification(notification);
    }

    @Nullable
    private String getReminderText(@Nullable Gameweek currentGameweek) {
        if (currentGameweek == null) {
            return null;
        }

        if (currentGameweek.getDeadlineTime() != null) {
            Locale locale = new Locale("en");

            String text;
            DateFormat dateFormat;
            if (DateUtil.isToday(currentGameweek.getDeadlineTime())) {
                text = "today before";
            } else {
                dateFormat = new SimpleDateFormat("EEEE", locale);
                text = String.format("before %s at", dateFormat.format(currentGameweek.getDeadlineTime()));
            }

            dateFormat = new SimpleDateFormat("HH:mm", locale);
            String time = dateFormat.format(currentGameweek.getDeadlineTime());

            return String.format("%s %s", text, time);
        }
        return "as soon as possible";
    }
}
