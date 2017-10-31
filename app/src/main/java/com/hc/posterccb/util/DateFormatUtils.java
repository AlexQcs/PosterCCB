package com.hc.posterccb.util;

import android.app.AlarmManager;
import android.content.Context;

import com.hc.posterccb.application.ProApplication;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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


    public static void syncTime(String date) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            Date resDate = DateFormatUtils.string2Date(date, "yyyy-MM-dd HH:mm:ss");
            String formatStr = DateFormatUtils.date2String(resDate, "yyyyMMdd.HHmmss");
//            String formatStr = "20170926.103020"; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + formatStr + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void syncSysTime(String date) {
        try {
            requestPermission();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date resDate = DateFormatUtils.string2Date(date, "yyyy-MM-dd HH:mm:ss");
        int year = Integer.parseInt(date2String(resDate, "yyyy"));
        int month = Integer.parseInt(date2String(resDate, "MM"));
        int day = Integer.parseInt(date2String(resDate, "dd"));
        int hour = Integer.parseInt(date2String(resDate, "HH"));
        int minutes = Integer.parseInt(date2String(resDate, "mm"));
        int seconds = Integer.parseInt(date2String(resDate, "ss"));

        Calendar c = Calendar.getInstance();

        c.set(year, month, day, hour, minutes, seconds);

        AlarmManager am = (AlarmManager) ProApplication.getInstance()
                .getSystemService(Context.ALARM_SERVICE);
        am.setTime(c.getTimeInMillis());

    }

    static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    static Process createSuProcess() throws IOException  {
        File rootUser = new File("/system/xbin/ru");
        if(rootUser.exists()) {
            return Runtime.getRuntime().exec(rootUser.getAbsolutePath());
        } else {
            return Runtime.getRuntime().exec("su");
        }
    }

    static Process createSuProcess(String cmd) throws IOException {

        DataOutputStream os = null;
        Process process = createSuProcess();

        try {
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit $?\n");
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }

        return process;
    }
}
