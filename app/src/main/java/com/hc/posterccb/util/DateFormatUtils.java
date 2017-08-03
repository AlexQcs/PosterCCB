package com.hc.posterccb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by alex on 2017/7/19.
 */

public class DateFormatUtils {
    public static String date2String(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    public static Date string2Date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //检查是否在指定的时间范围内
    public static boolean checkTimeer(String data, String[] timers) {
        boolean result = false;
        Date targetData = string2Date(data, "HH:ss");
        List<Date> list = new ArrayList<>();
        for (String timer : timers) {
            String[] temp = timer.split("-");
            for (String s : temp) {
                Date tempDate = string2Date(s, "HH:ss");
                list.add(tempDate);
            }
        }
        if (list.size() == 0 || list.size() % 2 != 0) return true;
        for (int i = 1; i < list.size(); i = i + 2) {
            if (targetData.getTime() > list.get(i - 1).getTime() && targetData.getTime() < list.get(i).getTime()) {
                result = true;
            }
        }
        return result;
    }
}
