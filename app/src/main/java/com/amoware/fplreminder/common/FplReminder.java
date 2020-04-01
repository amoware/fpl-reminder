package com.amoware.fplreminder.common;

import android.content.Context;
import android.util.Log;

import com.amoware.fplreminder.alarm.AlarmsManager;
import com.amoware.fplreminder.gameweek.Gameweek;

import java.util.Date;
import java.util.List;

import static com.amoware.fplreminder.common.Constants.REMINDER_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2019-12-12.
 */
public class FplReminder {

    private final Context context;

    private Gameweek currentGameweek;

    private boolean notificationSound = true;
    private boolean notificationVibration = true;

    public FplReminder(Context context) {
        this.context = context;
    }

    public boolean isNotificationSound() {
        return notificationSound;
    }

    // Todo:
    // Vi vill spara undan värdet `notificationSound` i en preference.
    // Typ samma princip som i metoden get/setNotificationTimer.
    // Skapa en metod i preferenceManager för att spara och hämta en boolean
    public void setNotificationSound(boolean notificationSound) {
        this.notificationSound = notificationSound;
    }

    public boolean isNotificationVibration() {
        return notificationVibration;
    }

    // Todo:
    // Vi vill spara undan värdet `notificationVibration` i en preference.
    // Typ samma princip som i metoden get/setNotificationTimer.
    // Skapa en metod i preferenceManager för att spara och hämta en boolean
    public void setNotificationVibration(boolean notificationVibration) {
        this.notificationVibration = notificationVibration;
    }

    public Time getNotificationTimer() {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        String jsonString = preferenceManager.getString(REMINDER_PREFERENCE, null);
        return Time.parseTime(jsonString);
    }

    public void setNotificationTimer(Time time) {
        Date deadlineTime = (currentGameweek != null ? currentGameweek.getDeadlineTime() : null);
        Log.d(tagger(getClass()), "Deadline: " + deadlineTime + ", time selected: " + time);

        saveReminderInPreference(time);
        setAlarmForNotificationToBeShown();
    }

    private void saveReminderInPreference(Time time) {
        Log.d(tagger(getClass()), time.toJsonString());
        PreferenceManager preferenceManager = new PreferenceManager(context);
        preferenceManager.putString(REMINDER_PREFERENCE, time.toJsonString());
    }

    /**
     * Sets the next deadline before which the user has to manage his/her team. When the deadline
     * occurs, a new set of gameweeks from fpl site is downloaded and the two different alarms
     * are updated.
     * @param gameweeks gameweeks that have been downloaded
     */
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        currentGameweek = getCurrentGameweek(gameweeks);
        setAlarmForGameweekDeadline();
        setAlarmForNotificationToBeShown();
    }

    /**
     * Returns the current gameweek based on a list of gameweeks. Current gameweek is the
     * first gameweek in the list which's deadline time occurs before the timestamp of calling this
     * function.
     * @param gameweeks list of gameweeks
     * @return current gameweek
     */
    private Gameweek getCurrentGameweek(List<Gameweek> gameweeks) {
        if (gameweeks == null) {
            return null;
        }

        Date todaysDate = new Date();
        for (Gameweek gameweek : gameweeks) {
            if (todaysDate.compareTo(gameweek.getDeadlineTime()) < 0) {
                return new Gameweek(gameweek);
            }
        }
        return null;
    }

    /**
     * Sets an alarm that at last triggers {@link com.amoware.fplreminder.alarm.GameweekReceiver}.
     */
    private void setAlarmForGameweekDeadline() {
        if (currentGameweek != null) {
            AlarmsManager alarmsManager = new AlarmsManager(context);
            alarmsManager.setAlarmForGameweekDeadline(currentGameweek.getDeadlineTime());
        }
    }

    /**
     * Sets an alarm that at last triggers {@link com.amoware.fplreminder.alarm.ReminderReceiver}.
     */
    private void setAlarmForNotificationToBeShown() {
        Time time = getNotificationTimer();
        // Only set reminder when there's a current gameweek
        if (currentGameweek != null && time != null) {
            Date notificationDate = DateUtil.subtractTime(currentGameweek.getDeadlineTime(), time);
            AlarmsManager alarmsManager = new AlarmsManager(context);
            alarmsManager.setAlarmForNotificationToBeShown(notificationDate);
        }
    }

    public Gameweek getCurrentGameweek() {
        return currentGameweek;
    }

    public Context getContext() {
        return context;
    }
}
