package com.loganfreeman.utahfishing.common.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Time {

    public static final DateFormat df = new SimpleDateFormat("mm/dd/yyyy");

    public static Date getDate(String date) {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * because calendar month starts from 0
     * @param d
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static CalendarDay toCalendarDay(String d) {
        String[] parts = d.split("/");
        List<Integer> dmy = Arrays.stream(parts).map(i -> Integer.parseInt(i)).collect(Collectors.toList());
        return CalendarDay.from(dmy.get(2), dmy.get(0) - 1, dmy.get(1));
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMDHMSTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    /**
     * MM-dd HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowMDHMSTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd HH:mm:ss");
        return mDateFormat.format(new Date());
    }

    /**
     * MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowYMD() {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
        return mDateFormat.format(new Date());
    }

    /**
     * yyyy-MM-dd
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYMD(Date date) {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");
        return mDateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getMD(Date date) {

        SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "MM-dd");
        return mDateFormat.format(date);
    }
}
