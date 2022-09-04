package com.amoware.fplreminder.common;

import android.content.Context;
import android.content.SharedPreferences;

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
}
