package com.amoware.fplreminder.common;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import static com.amoware.fplreminder.common.Constants.NUNITO_BOLD;
import static com.amoware.fplreminder.common.Constants.NUNITO_REGULAR;
import static com.amoware.fplreminder.common.Constants.NUNITO_SEMIBOLD;
import static com.amoware.fplreminder.common.Constants.tagger;

/**
 * Created by amoware on 2020-03-26.
 */
public class TypefaceUtil {

    public static Typeface getBoldTypeface(Context context) {
        return getTypeface(context, NUNITO_BOLD);
    }

    public static Typeface getSemiboldTypeface(Context context) {
        return getTypeface(context, NUNITO_SEMIBOLD);
    }

    public static Typeface getRegularTypeface(Context context) {
        return getTypeface(context, NUNITO_REGULAR);
    }

    private static Typeface getTypeface(Context context, String path) {
        Typeface typeface = Typeface.DEFAULT;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), path);
        } catch (Exception e) {
            Log.e(tagger(TypefaceUtil.class), "Unable to load typeface", e);
        }
        return typeface;
    }
}
