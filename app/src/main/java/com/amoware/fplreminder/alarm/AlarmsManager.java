package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.amoware.fplreminder.common.DateUtil;

import java.util.Date;

/**
 * Manages alarms. There are two type of alarms: one for notification to be shown to the user and
 * a second one which is triggered when a gameweek's deadline occurs.
 * Created by amoware on 2019-12-29.
 */
public class AlarmsManager {
    private final Context mContext;

    private final int ALARM_GAMEWEEK_ID = 0;
    private final int ALARM_REMINDER_ID = 1;

    public AlarmsManager(Context context) {
        mContext = context;
    }

    public void setAlarmForGameweekDeadline(Date date) {
        setAlarm(date, ALARM_GAMEWEEK_ID);
    }

    public void setAlarmForNotificationToBeShown(Date date) {
        setAlarm(date, ALARM_REMINDER_ID);
    }

    /**
     * Sets an alarm to be triggered at a specific date. If an alarm (based on the id) already
     * exists, the existing alarm is overwritten by the new one.
     *
     * @param date when the alarm to be triggered
     * @param id   unique id for the alarm
     */
    private void setAlarm(Date date, int id) {
        Log.d(tagger(getClass()), "Setting an alarm (id=" + id + ") at: " + date);

        Class<? extends BroadcastReceiver> receiverClass = getReceiverClass(id);
        if (receiverClass == null) {
            Log.e(tagger(getClass()), "receiverClass is null");
            return;
        }

        AlarmManager alarmManager = getAlarmManager();
        if (alarmManager == null) {
            Log.e(tagger(getClass()), "alarmManager is null");
            return;
        }

        if (DateUtil.hasOccurred(date)) {
            Log.w(tagger(getClass()), "Not setting alarm (id=" + id + "). " + date + " has already occurred");
            return;
        }

        Intent intent = new Intent(mContext, receiverClass);
        PendingIntent pendingIntent = getPendingIntent(id, intent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }

    private AlarmManager getAlarmManager() {
        return (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    private PendingIntent getPendingIntent(int id, Intent intent) {
        final int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
        return PendingIntent.getBroadcast(mContext, id, intent, flags);
    }

    /**
     * Gets one of the app's BroadcastReceiver classes based on an id that matches an alarm.
     *
     * @param id unique id for the alarm
     * @return class that extends BroadcastReceiver
     */
    private Class<? extends BroadcastReceiver> getReceiverClass(int id) {
        Class<? extends BroadcastReceiver> receiverClass = null;
        if (id == ALARM_GAMEWEEK_ID) {
            receiverClass = GameweekReceiver.class;
        } else if (id == ALARM_REMINDER_ID) {
            receiverClass = ReminderReceiver.class;
        }
        return receiverClass;
    }
}
