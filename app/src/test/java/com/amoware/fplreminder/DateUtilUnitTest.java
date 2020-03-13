package com.amoware.fplreminder;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by amoware on 2020-03-13.
 */
public class DateUtilUnitTest {

    @Test
    public void subtractTimeFromDate() {
        Date date = getDate(2020, 3, 13, 23, 59);

        Time time1 = new Time(0, 0);
        Date expectedDate1 = getDate(2020, 3, 13, 23, 59);

        Time time2 = new Time(0, 1);
        Date expectedDate2 = getDate(2020, 3, 13, 23, 58);

        Time time3 = new Time(47, 59);
        Date expectedDate3 = getDate(2020, 3, 12, 0, 0);

        assertEquals(DateUtil.subtractTime(date, time1), expectedDate1);
        assertEquals(DateUtil.subtractTime(date, time2), expectedDate2);
        assertEquals(DateUtil.subtractTime(date, time3), expectedDate3);
    }

    @Test
    public void subtractNullTimeFromDate() {
        Date date = getDate(2021, 1, 1, 0, 0);
        Time time = null;

        assertEquals(DateUtil.subtractTime(date, time), date);
    }

    @Test
    public void subtractTimeFromNullDate() {
        Date date = null;
        Time time = new Time(47, 0);

        assertNull(DateUtil.subtractTime(date, time));
    }

    @Test
    public void subtractNullTimeFromNullDate() {
        Date date = null;
        Time time = null;

        assertNull(DateUtil.subtractTime(date, time));
    }

    private Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTime();
    }
}
