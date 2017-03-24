package com.loganfreeman.utahfishing;

import com.loganfreeman.utahfishing.common.utils.Time;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static com.loc.e.d;
import static org.junit.Assert.*;

import static com.loganfreeman.utahfishing.common.utils.Time.df;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public  void testCalendarDay() throws ParseException {
        Date d = df.parse("03/11/2017");
        CalendarDay calendarDay = CalendarDay.from(d);
        assertEquals(calendarDay.getDay(), 11);
        assertEquals(calendarDay.getMonth(), 3);
        assertEquals(calendarDay.getYear(), 2017);
    }

    @Test
    public void testDate() throws ParseException {
        Date d = df.parse("03/11/2017");
        System.out.println(d.toString());
        System.out.println(df.format(d));
        Calendar calendarDay = Calendar.getInstance();
        calendarDay.setTime(d);
        assertEquals(calendarDay.get(Calendar.DAY_OF_MONTH), 11);
        assertEquals(calendarDay.get(Calendar.MONTH), 3);
        assertEquals(calendarDay.get(Calendar.YEAR), 2017);

    }

    @Test
    public  void toCalendarDay() {
        String d = "03/11/2017";
        CalendarDay calendarDay = Time.toCalendarDay(d);
        assertEquals(calendarDay.getDay(), 11);
        assertEquals(calendarDay.getMonth(), 2);
        assertEquals(calendarDay.getYear(), 2017);
    }

    @Test
    public  void calendarDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 11);
        System.out.println(calendar.getTime().toString());
        CalendarDay calendarDay = CalendarDay.from(calendar);
        assertEquals(calendarDay.getDay(), 11);
        assertEquals(calendarDay.getMonth(), 2);
        assertEquals(calendarDay.getYear(), 2017);
    }

    @Test
    public void selectCalenday() {
        String d = "03/11/2017";
        CalendarDay calendarDay = Time.toCalendarDay(d);
        assertEquals(CalendarDay.from(2017, 2, 11), calendarDay);
    }
}