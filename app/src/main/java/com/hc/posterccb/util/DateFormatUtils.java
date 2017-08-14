package com.hc.posterccb.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
    public static boolean checkTimer(String data, Set<String> timers) {
        if (timers == null && timers.size() <= 0) return true;
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
            if (targetData.after(list.get(i - 1)) && targetData.before(list.get(i))) {
                result = true;
            }
        }
        return result;
    }



    public void syncTime(String date) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            Date resDate = DateFormatUtils.string2Date(date, "yyyy-M-dd HH:mm:ss");
            String formatStr = DateFormatUtils.date2String(resDate, "yyyyMMdd.HHmmss");
//            String datetime = "20160323.103020"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT-8\n");
            os.writeBytes("/system/bin/date -s " + formatStr + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
