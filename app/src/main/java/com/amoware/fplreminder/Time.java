package com.amoware.fplreminder;

import androidx.annotation.NonNull;

/**
 * Created by amoware on 2019-12-12.
 */
public class Time {

    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @NonNull
    @Override
    public String toString() {
        return "Time{" + "hours=" + hours + ", minutes=" + minutes + '}';
    }
}
