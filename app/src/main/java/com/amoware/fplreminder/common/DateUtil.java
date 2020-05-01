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

    public static Date addTime(Date date, Time timeAfterDate) {
        if (timeAfterDate == null) {
            return date;
        }

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, timeAfterDate.getHours());
        calendar.add(Calendar.MINUTE, timeAfterDate.getMinutes());

        return calendar.getTime();
    }
}
