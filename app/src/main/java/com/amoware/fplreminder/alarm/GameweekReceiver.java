package com.amoware.fplreminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Is used to download gameweeks from fpl and set two new alarms based on the user preference and
 * on the next gameweek's deadline. The class' onReceive method is called when the time of a
 * gameweek's deadline occurs.
 * Created by amoware on 2019-12-30.
 */
public class GameweekReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmsManager", "Hello from GameweekReceiver..");
    }
}
