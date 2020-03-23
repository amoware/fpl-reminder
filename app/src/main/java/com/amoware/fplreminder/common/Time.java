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
     * Konverterar ett Time-objekt till en json-formaterad sträng. Ex kan värdet för strängen bli
     * {"hours":0,"minutes":0}.
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

    /* Returnerar ett Time-objekt utifrån en json-formaterad sträng. */
    public static Time parseTime(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string);
            return new Time(jsonObject.getInt(HOURS_FIELD), jsonObject.getInt(MINUTES_FIELD));
        } catch (JSONException e) {
            // Do nothing
        }
        return null;
    }
}
