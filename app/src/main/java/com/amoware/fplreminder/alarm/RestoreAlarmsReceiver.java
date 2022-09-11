package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by amoware on 2022-09-10.
 */
public class RestoreAlarmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setAlarms(context, intent.getAction());
        } else if (intent.getAction().equals("android.intent.action.TIME_SET")) {
            setAlarms(context, intent.getAction());
        }
    }

    private void setAlarms(Context context, String action) {
        Log.d(tagger(getClass()), action);
        AlarmsLogic alarmsLogic = new AlarmsLogic(context);
        alarmsLogic.initializeAlarmsFromFile();
    }
}
