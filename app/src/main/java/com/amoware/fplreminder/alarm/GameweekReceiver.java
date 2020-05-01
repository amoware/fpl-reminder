package com.amoware.fplreminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amoware.fplreminder.common.ConnectionHandler;
import com.amoware.fplreminder.notification.Notification;
import com.amoware.fplreminder.notification.NotificationService;

import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Is used to download gameweeks from fpl and set two new alarms based on the user preference and
 * on the next gameweek's deadline. The class' onReceive method is called when the time of a
 * gameweek's deadline occurs.
 * Created by amoware on 2019-12-30.
 */
public class GameweekReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Todo update the list with gameweek deadlines
        Log.d(tagger(getClass()), "Hello from GameweekReceiver..");

        // What happens if the user isn't connected to a network? Or if gameweeks cannot be
        // downloaded?
        ConnectionHandler connectionHandler = new ConnectionHandler(context);

        if (!connectionHandler.isNetworkAvailable()) {
            Notification notification = new Notification();
            NotificationService notificationService = new NotificationService(context);
            notification.setContentText("Connection could not be established. Gameweek deadlines not downloaded correctly");
            notificationService.notify(notification);
        }

    }
}
