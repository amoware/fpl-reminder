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

    public String getString(String key, String defaultValue) {
        return preferences != null ? preferences.getString(key, defaultValue) : null;
    }

    public void putString(String key, String value) {
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }
}
