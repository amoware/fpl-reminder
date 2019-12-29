package com.amoware.fplreminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
    }
}
