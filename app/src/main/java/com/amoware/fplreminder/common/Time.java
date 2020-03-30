package com.amoware.fplreminder.common;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amoware on 2019-12-12.
 */
public class Time {

    private static final String HOURS_FIELD = "hours";
    private static final String MINUTES_FIELD = "minutes";

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

    /*
     * Converts a Time object to a json formatted string. For instance, the string's value may
     * assume {"hours":0,"minutes":0}.
     */
    public String toJsonString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HOURS_FIELD, hours);
            jsonObject.put(MINUTES_FIELD, minutes);
            return jsonObject.toString();
        } catch (JSONException e) {
            // Do nothing
        }
        return null;
    }

    /* Parses and returns a Time object based on a json formatted string. */
    public static Time parseTime(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string != null ? string : "");
            return new Time(jsonObject.getInt(HOURS_FIELD), jsonObject.getInt(MINUTES_FIELD));
        } catch (JSONException e) {
            // Do nothing
        }
        return null;
    }
}
