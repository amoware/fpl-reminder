package com.amoware.fplreminder.alarm;

import static com.amoware.fplreminder.common.Constants.tagger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Created by amoware on 2022-09-10.
 */
public class RestoreAlarmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.BOOT_COMPLETED".equals(action)) {
            setAlarms(context, action);
        } else if ("android.intent.action.TIME_SET".equals(action)) {
            setAlarms(context, action);
        }
    }

    private void setAlarms(Context context, @NonNull String action) {
        Log.d(tagger(getClass()), action);
        AlarmsLogic alarmsLogic = new AlarmsLogic(context);
        alarmsLogic.initializeAlarmsFromStorage();
    }
}
