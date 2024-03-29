package com.amoware.fplreminder.common;

import static com.amoware.fplreminder.common.Constants.FETCHED_GAMEWEEKS_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.REMINDER_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.SOUND_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.VIBRATION_PREFERENCE;
import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amoware.fplreminder.alarm.AlarmsManager;
import com.amoware.fplreminder.gameweek.Gameweek;
import com.amoware.fplreminder.model.gameweek.GameweeksClient;

import java.util.Date;
import java.util.List;

/**
 * Created by amoware on 2019-12-12.
 */
public class FplReminder {
    private final Context mContext;
    private final GameweekStorage mGameweekStorage;

    @Nullable
    private Gameweek mCurrentGameweek;

    public FplReminder(Context context) {
        mContext = context;
        mGameweekStorage = new GameweekStorage(context);
    }

    @Nullable
    public Gameweek getCurrentGameweek() {
        return mCurrentGameweek;
    }

    @Nullable
    public List<Gameweek> getGameweeksFromStorage() {
        return mGameweekStorage.readGameweeksFromFile();
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isNotificationSound() {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        return preferenceManager.getBoolean(SOUND_PREFERENCE, false);
    }

    public void setNotificationSound(boolean notificationSound) {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        preferenceManager.putBoolean(SOUND_PREFERENCE, notificationSound);
    }

    public boolean isNotificationVibration() {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        return preferenceManager.getBoolean(VIBRATION_PREFERENCE, false);
    }

    public void setNotificationVibration(boolean notificationVibration) {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        preferenceManager.putBoolean(VIBRATION_PREFERENCE, notificationVibration);
    }

    @Nullable
    public Time getNotificationTimer() {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        String jsonString = preferenceManager.getString(REMINDER_PREFERENCE, null);
        return Time.parseTime(jsonString);
    }

    public void setNotificationTimer(@Nullable Time time) {
        Date deadlineTime = (mCurrentGameweek != null ? mCurrentGameweek.getDeadlineTime() : null);
        Log.d(tagger(getClass()), "Deadline: " + deadlineTime + ", time selected: " + time);

        if (time == null) {
            Log.e(tagger(getClass()), "time is null, failed to set notification timer");
            return;
        }

        saveReminderInPreference(time);
        setAlarmForNotificationToBeShown(mCurrentGameweek);
    }

    private void saveReminderInPreference(@NonNull Time time) {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        preferenceManager.putString(REMINDER_PREFERENCE, time.toJsonString());
    }

    /**
     * Sets the next deadline before which the user has to manage his/her team. When the deadline
     * occurs, a new set of gameweeks from fpl site is downloaded and the two different alarms
     * are updated.
     *
     * @param gameweeks gameweeks that have been downloaded
     */
    public void onGameweeksDownloaded(List<Gameweek> gameweeks) {
        setCurrentGameweek(parseCurrentGameweek(gameweeks));
        setAlarms(mCurrentGameweek);
    }

    public void setAlarms(@Nullable Gameweek gameweek) {
        if (gameweek == null) {
            Log.e(tagger(getClass()), "gameweek is null, failed to set alarms");
            return;
        }
        setAlarmForGameWeekDeadline(gameweek);
        setAlarmForNotificationToBeShown(gameweek);
    }

    private void setCurrentGameweek(@Nullable Gameweek currentGameweek) {
        mCurrentGameweek = currentGameweek;
    }

    /**
     * Returns the current gameweek based on a list of gameweeks. Current gameweek is the
     * first gameweek's deadline time that occurs before the timestamp of calling this
     * function.
     *
     * @param gameweeks list of gameweeks
     * @return current gameweek
     */
    @Nullable
    private Gameweek parseCurrentGameweek(@Nullable List<Gameweek> gameweeks) {
        if (gameweeks == null) {
            return null;
        }

        Date todayDate = new Date();
        for (Gameweek gameweek : gameweeks) {
            if (DateUtil.isFirstBeforeSecond(todayDate, gameweek.getDeadlineTime())) {
                return new Gameweek(gameweek);
            }
        }
        return null;
    }

    /**
     * Sets an alarm that eventually triggers {@link com.amoware.fplreminder.alarm.GameweekReceiver}.
     */
    private void setAlarmForGameWeekDeadline(@NonNull Gameweek gameweek) {
        AlarmsManager alarmsManager = new AlarmsManager(mContext);
        alarmsManager.setAlarmForGameweekDeadline(gameweek.getDeadlineTime());
    }

    /**
     * Sets an alarm that eventually triggers {@link com.amoware.fplreminder.alarm.ReminderReceiver}.
     */
    private void setAlarmForNotificationToBeShown(@NonNull Gameweek gameweek) {
        Time time = getNotificationTimer();
        if (time == null) {
            Log.w(tagger(getClass()), "time is null");
            time = new Time(0, 0);
        }

        Date notificationDate = DateUtil.subtractTime(gameweek.getDeadlineTime(), time);
        AlarmsManager alarmsManager = new AlarmsManager(mContext);
        alarmsManager.setAlarmForNotificationToBeShown(notificationDate);
    }

    public void writeGameweeksToStorage(@Nullable List<Gameweek> gameweeks) {
        mGameweekStorage.writeGameweeksToFile(gameweeks);
    }

    @Nullable
    public Gameweek getCurrentGameweekFromStorage() {
        List<Gameweek> storedGameweeks = mGameweekStorage.readGameweeksFromFile();
        return parseCurrentGameweek(storedGameweeks);
    }

    @Nullable
    public Gameweek getCurrentGameweekFromAPI() {
        GameweeksClient client = new GameweeksClient();

        List<Gameweek> gameweeks = client.fetchGameweeks();
        if (gameweeks == null || gameweeks.size() == 0) {
            return null;
        }

        writeGameweeksToStorage(gameweeks);

        return parseCurrentGameweek(gameweeks);
    }

    @Nullable
    public Date getFetchedGameweeksDate() {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        return preferenceManager.getDate(FETCHED_GAMEWEEKS_PREFERENCE);
    }

    public void updateFetchedGameweeksDate() {
        PreferenceManager preferenceManager = new PreferenceManager(mContext);
        preferenceManager.putDate(FETCHED_GAMEWEEKS_PREFERENCE, DateUtil.getNow());
    }
}
