package com.amoware.fplreminder.gameweek;

import androidx.annotation.NonNull;

import com.amoware.fplreminder.common.Time;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by amoware on 2019-12-29.
 */
public class Gameweek {

    private static final String NAME_FIELD = "name";
    private static final String DEADLINE_FIELD = "deadlineTime";

    private String name;
    private Date deadlineTime;

    public Gameweek(String name, long deadlineTime) {
        this(name, deadlineTime != -1L ? new Date(deadlineTime) : null);
    }

    public Gameweek(String name, Date deadlineTime) {
        this.name = name;
        this.deadlineTime = deadlineTime;
    }

    public Gameweek(Gameweek gameweek) {
        this.name = gameweek != null ? gameweek.getName() : null;
        this.deadlineTime = gameweek != null ? new Date(gameweek.getDeadlineTime().getTime()) : null;
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

    /*
     * Converts a Gameweek object to a json formatted string. For instance, the string's value may
     * assume {"name":"gameweek 2","deadlineTime":12345567}.
     */
    public String toJsonString() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(NAME_FIELD, name);
            jsonObject.put(DEADLINE_FIELD, deadlineTime != null ? deadlineTime.getTime() : -1L);
            return jsonObject.toString();
        } catch (JSONException e) {
            // Do nothing
        }
        return null;
    }

    /* Parses and returns a Gameweek object based on a json formatted string. */
    public static Gameweek parseGameweek(String string) {
        try {
            JSONObject jsonObject = new JSONObject(string != null ? string : "");
            return new Gameweek(
                    jsonObject.getString(NAME_FIELD), jsonObject.getLong(DEADLINE_FIELD)
            );
        } catch (JSONException e) {
            // Do nothing
        }
        return null;
    }
}
