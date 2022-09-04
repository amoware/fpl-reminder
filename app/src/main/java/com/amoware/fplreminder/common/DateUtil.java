package com.amoware.fplreminder.common;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by amoware on 2020-03-12.
 */
public class DateUtil {

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
        return date != null && getDate(date).compareTo(getTodaysDate()) == 0;
    }

    public static Date getTodaysDate() {
        return getDate(new Date());
    }

    public static Date getDate(Date date) {
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

    public static boolean hasOccurred(Date date) {
        return date != null && date.compareTo(new Date()) < 0;
    }

}
