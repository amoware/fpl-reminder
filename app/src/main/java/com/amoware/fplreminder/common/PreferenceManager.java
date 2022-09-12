package com.amoware.fplreminder.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Created by amoware on 2020-03-23.
 */
public class PreferenceManager {
    private SharedPreferences preferences;

    public PreferenceManager(Context context) {
        if (context != null) {
            preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences != null ? preferences.getBoolean(key, defaultValue) : defaultValue;
    }

    public void putBoolean(String key, boolean value) {
        if (preferences != null) {
            preferences.edit().putBoolean(key, value).apply();
        }
    }

    public String getString(String key, String defaultValue) {
        return preferences != null ? preferences.getString(key, defaultValue) : defaultValue;
    }

    public void putString(String key, String value) {
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }

    @Nullable
    public Date getDate(String key) {
        if (preferences != null) {
            long time = preferences.getLong(key, -1);
            if (time == -1) {
                return null;
            }
            return new Date(time);
        }
        return null;
    }

    public void putDate(String key, @Nullable Date value) {
        if (preferences != null || value != null) {
            preferences.edit().putLong(key, value.getTime()).apply();
        }
    }
}
