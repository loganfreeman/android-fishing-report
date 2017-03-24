package com.loganfreeman.utahfishing;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

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
}