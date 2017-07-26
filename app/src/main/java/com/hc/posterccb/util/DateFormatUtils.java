package com.hc.posterccb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex on 2017/7/19.
 */

public class DateFormatUtils {
    public static String date2String(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        return sdf.format(date);
    }

    public static Date string2Date(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }
}
