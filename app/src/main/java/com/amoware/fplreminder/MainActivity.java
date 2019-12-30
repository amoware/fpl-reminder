package com.amoware.fplreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.amoware.fplreminder.alarm.AlarmsManager;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by amoware on 2019-12-29.
 */
public class MainActivity extends AppCompatActivity {

    private NotificationSettings notificationSettings;
    private GameweekManager gameweekManager;

    public void displayNotificationTimer(){
    }

    public void displayCountdownTimer(){
    }

    public void displayCurrentGameweek(){
    }

    public void displayNotificationPreferences(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmsManager alarmsManager = new AlarmsManager(this);
        alarmsManager.setAlarmForGameweekDeadline(generateDate(30));
        alarmsManager.setAlarmForGameweekDeadline(generateDate(15)); // Overwrites alarm above
        alarmsManager.setAlarmForNotificationToBeShown(generateDate(30));
    }

    private Date generateDate(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,seconds);
        return calendar.getTime();
    }
}
