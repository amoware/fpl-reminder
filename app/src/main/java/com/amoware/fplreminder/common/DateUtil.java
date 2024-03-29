package com.amoware.fplreminder.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by amoware on 2020-03-12.
 */
public class DateUtil {
    @NonNull
    public static Date getNow() {
        return new Date();
    }

    @Nullable
    public static Date subtractTime(Date date, Time timeBeforeDate) {
        if (timeBeforeDate == null) {
            return date;
        }

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, -timeBeforeDate.getHours());
        calendar.add(Calendar.MINUTE, -timeBeforeDate.getMinutes());

        return calendar.getTime();
    }

    public static boolean isToday(Date date) {
        return date != null && getDate(date).compareTo(getTodayDate()) == 0;
    }

    @NonNull
    public static Date getTodayDate() {
        return getDate(new Date());
    }

    @NonNull
    public static Date getDate(@Nullable Date date) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static boolean hasOccurred(@Nullable Date date) {
        return date != null && date.compareTo(getNow()) < 0;
    }

    public static boolean isFirstBeforeSecond(@NonNull Date first, @NonNull Date second) {
        return first.compareTo(second) < 0;
    }

    public static long getMillisecondsAgo(@Nullable Date date) {
        Date now = getNow();
        if (date == null) {
            return now.getTime();
        }
        return now.getTime() - date.getTime();
    }
}
