package com.amoware.fplreminder;

import java.util.Date;

public class AlarmsManager {

    private Alarm reminderTime;
    private Alarm gameweekTime;

    public void setReminderTime(Alarm reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setGameweekTime(Alarm gameweekTime) {
        this.gameweekTime = gameweekTime;
    }

    public void updateReminderTime(Date newReminderTime){

    }

    public void updateGameweekTime(Date newGameweekTime){

    }
}
