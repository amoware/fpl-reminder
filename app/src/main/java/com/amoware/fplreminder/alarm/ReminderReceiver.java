package com.amoware.fplreminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Is used to show the user a notification and the onReceive method is called when the time of an
 * alarm, that the user has set, occurs.
 * Created by amoware on 2019-12-30.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Todo show the user a notification
        Log.d("AlarmsManager", "Hello from ReminderReceiver..");
    }
}
