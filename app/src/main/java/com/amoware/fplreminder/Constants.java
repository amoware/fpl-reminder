package com.amoware.fplreminder;

/**
 * Created by amoware on 2020-03-09.
 */
public class Constants {
    public static final String TAG = "Logcat";

    public static final String NUNITO_REGULAR = "nunito-regular.ttf";
    public static final String NUNITO_SEMIBOLD = "nunito-semibold.ttf";

    public static String tagger(Class<?> aClass) {
        return TAG + "/" + aClass.getSimpleName();
    }
}
