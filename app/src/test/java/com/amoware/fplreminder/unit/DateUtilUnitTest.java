package com.amoware.fplreminder.unit;

import com.amoware.fplreminder.common.DateUtil;
import com.amoware.fplreminder.common.Time;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

        assertEquals(DateUtil.subtractTime(date, null), date);
    }

    @Test
    public void subtractTimeFromNullDate() {
        Time time = new Time(47, 0);

        assertNull(DateUtil.subtractTime(null, time));
    }

    @Test
    public void subtractNullTimeFromNullDate() {
        assertNull(DateUtil.subtractTime(null, null));
    }

    private Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTime();
    }

    @Test
    public void hasOccurred_dateBeforeTimestamp() {
        assertTrue(DateUtil.hasOccurred(getDateRelativeToTimestamp(-1)));
    }

    @Test
    public void hasOccurred_dateAfterTimestamp() {
        assertFalse(DateUtil.hasOccurred(getDateRelativeToTimestamp(1)));
    }

    @Test
    public void hasOccurred_nullDate() {
        assertFalse(DateUtil.hasOccurred(null));
    }

    private Date getDateRelativeToTimestamp(int minutesOffset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesOffset);
        return calendar.getTime();
    }

}
