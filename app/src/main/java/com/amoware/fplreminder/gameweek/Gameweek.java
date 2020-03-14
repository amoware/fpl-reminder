package com.amoware.fplreminder.gameweek;

import androidx.annotation.NonNull;

import java.util.Date;

/**
 * Created by amoware on 2019-12-29.
 */
public class Gameweek {

    private String name;
    private Date deadlineTime;

    public Gameweek(String name, Date deadlineTime) {
        this.name = name;
        this.deadlineTime = deadlineTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(Date deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "Gameweek{name='" + name + "\', deadlineTime=" + deadlineTime + '}';
    }
}
