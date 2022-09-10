package com.amoware.fplreminder.common;

import androidx.annotation.Nullable;

/**
 * Created by amoware on 2022-09-07.
 */
public class StringUtil {
    public static boolean isStringEmpty(@Nullable String value) {
        return value == null || value.trim().equals("");
    }
}
