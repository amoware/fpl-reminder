package com.amoware.fplreminder.common;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Date;

/**
 * Created by amoware on 2020-03-23.
 */
public class PreferenceManager {
    @Nullable
    private final Context mContext;

    public PreferenceManager(@Nullable Context context) {
        mContext = context;
    }

    @Nullable
    private SharedPreferences getSharedPreferences() {
        if (mContext != null) {
            return android.preference.PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return null;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences != null ? preferences.getBoolean(key, defaultValue) : defaultValue;
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences != null) {
            preferences.edit().putBoolean(key, value).apply();
        }
    }

    public String getString(String key, String defaultValue) {
        SharedPreferences preferences = getSharedPreferences();
        return preferences != null ? preferences.getString(key, defaultValue) : defaultValue;
    }

    public void putString(String key, String value) {
        SharedPreferences preferences = getSharedPreferences();
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }

    @Nullable
    public Date getDate(String key) {
        SharedPreferences preferences = getSharedPreferences();
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
        SharedPreferences preferences = getSharedPreferences();
        if (preferences != null && value != null) {
            preferences.edit().putLong(key, value.getTime()).apply();
        }
    }
}
