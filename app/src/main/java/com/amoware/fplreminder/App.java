package com.amoware.fplreminder;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class App extends Application {
    public static final String PREP_TEAM_CHANNEL = "teamChangesChannel";
    public static final String NEW_REMINDER_CHANNEL = "newReminderChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel(PREP_TEAM_CHANNEL, "Team changes", "Make team changes");
            createNotificationChannel(NEW_REMINDER_CHANNEL, "New reminder", "Status about new reminder");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(String id, String name, String description) {
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager == null) {
            return;
        }

        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(description);

        manager.createNotificationChannel(channel);
    }
}
