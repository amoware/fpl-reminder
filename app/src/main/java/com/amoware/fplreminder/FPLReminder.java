package com.amoware.fplreminder;

import android.content.Context;
import android.util.Log;

import com.amoware.fplreminder.alarm.AlarmsManager;
import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.PreferenceManager;
import com.amoware.fplreminder.common.Time;

import java.util.Date;

import static com.amoware.fplreminder.common.Constants.REMINDER_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-12.
 */
public class FPLReminder {

    private final Context context;

    private Date notificationTime;
    private Date deadlineTime;
    private Time scrollTime;
    private boolean notificationSound;
    private boolean notificationVibration;

    public FPLReminder(Context context) {
        this.context = context;
    }

    public boolean isNotificationSound() {
        return notificationSound;
    }

    public void setNotificationSound(boolean notificationSound) {
        this.notificationSound = notificationSound;
    }

    public boolean isNotificationVibration() {
        return notificationVibration;
    }

    public void setNotificationVibration(boolean notificationVibration) {
        this.notificationVibration = notificationVibration;
    }

    public Time getNotificationTimer() {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        String jsonString = preferenceManager.getString(REMINDER_PREFERENCE, null);
        return Time.parseTime(jsonString);
    }

    public void setNotificationTimer(Date deadlineTime, Time time) {
        Log.d(tagger(getClass()), "Deadline: " + deadlineTime + ", time selected: " + time);
        saveReminderInPreference(time);

        Date notificationDate = DateUtil.subtractTime(deadlineTime, getNotificationTimer());
        AlarmsManager alarmsManager = new AlarmsManager(context);
        alarmsManager.setAlarmForNotificationToBeShown(notificationDate);
    }

    private void saveReminderInPreference(Time time) {
        Log.d(tagger(getClass()), time.toJsonString());
        PreferenceManager preferenceManager = new PreferenceManager(context);
        preferenceManager.putString(REMINDER_PREFERENCE, time.toJsonString());
    }
}
