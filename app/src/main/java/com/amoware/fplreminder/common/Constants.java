package com.amoware.fplreminder.common;

/**
 * Created by amoware on 2020-03-09.
 */
public class Constants {
    private static final String TAG = "Logcat";

    public static final String NUNITO_REGULAR = "nunito-regular.ttf";
    public static final String NUNITO_SEMIBOLD = "nunito-semibold.ttf";

    public static final String REMINDER_PREFERENCE = "reminderPreference";

    public static final String API_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";

    public static String tagger(Class<?> aClass) {
        return TAG + "/" + aClass.getSimpleName();
    }
}
