package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amoware.fplreminder.common.FplReminder;
import com.amoware.fplreminder.gameweek.Gameweek;

/**
 * Created by amoware on 2022-09-11.
 */
public class AlarmsLogic {
    @NonNull
    private final FplReminder mFplReminder;

    public AlarmsLogic(@Nullable Context context) {
        mFplReminder = new FplReminder(context);
    }

    public void initializeAlarmsFromFile() {
        Gameweek gameweek = mFplReminder.getCurrentGameweekFromStorage();
        if (gameweek != null) {
            initializeAlarms(gameweek);
            return;
        }

        Log.w(tagger(getClass()), "current gameweek from storage is null");

        gameweek = mFplReminder.getCurrentGameweekFromAPI();
        if (gameweek != null) {
            initializeAlarms(gameweek);
            return;
        }

        Log.w(tagger(getClass()), "current gameweek from api is null");
    }

    private void initializeAlarms(@NonNull Gameweek gameweek) {
        mFplReminder.setAlarms(gameweek);
    }
}
