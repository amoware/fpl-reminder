package com.amoware.fplreminder;

import java.util.Date;

/**
 * Created by amoware on 2019-12-29.
 */
public class AlarmsManager {

    private Alarm reminderTime;
    private Alarm gameweekTime;

    public void setReminderTime(Alarm reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setGameweekTime(Alarm gameweekTime) {
        this.gameweekTime = gameweekTime;
    }

    public void updateReminderTime(Date newReminderTime) {

    }

    public void updateGameweekTime(Date newGameweekTime) {

    }
}
