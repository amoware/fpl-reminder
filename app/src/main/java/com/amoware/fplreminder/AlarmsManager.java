package com.amoware.fplreminder;

import java.util.Date;

public class AlarmsManager {

    private Alarm reminerTime;
    private Alarm gameweekTime;


    public void setReminerTime(Alarm reminerTime) {
        this.reminerTime = reminerTime;
    }

    public void setGameweekTime(Alarm gameweekTime) {
        this.gameweekTime = gameweekTime;
    }

    public void updateReminderTime(Date newReminderTime){

    }

    public void updateGameweekTime(Date newGameweekTime){

    }
}
