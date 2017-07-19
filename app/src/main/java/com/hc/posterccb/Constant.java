package com.hc.posterccb;

import android.os.Environment;

import java.lang.reflect.Method;

/**
 * Created by alex on 2017/7/1.
 */

public class Constant {
    public static final String XML_HEAD = "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n\"";
    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory() + "/Misc/mv.mp4";
    public static final String UDP_TESTPATH = Environment.getExternalStorageDirectory() + "/Misc/test/";


    public static final String TASKTYPE = "tasktype";

    public static final String XML_STARTDOM = "command";
    public static final String XML_LISTTAG = "content";
    public static final String XML_CONFIG = "config";

    //轮询返回响应类型
    //播放任务
    public static final String POLLING_PROGRAM = "program";
    //升级任务
    public static final String POLLING_UPGRADE = "upgrade";
    //控制类任务
    public static final String POLLING_CONTROL = "control";
    //即时消息类任务
    public static final String POLLING_REALTIMEMSG = "realtimemsg";
    //取消即时消息类任务
    public static final String POLLING_CANCELREALTIMEMSG = "cancelrealtimemsg";
    //终端配置类任务
    public static final String POLLING_CONFIG = "config";


    public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory() + "/PosterCCB";
    public static final String LOCAL_LOG_PATH = LOCAL_FILE_PATH + "/log";
    public static final String LOCAL_PROGRAM_PATH = LOCAL_FILE_PATH + "/media";
    public static final String LOCAL_PROGRAM_TXT = LOCAL_PROGRAM_PATH + "/program.txt";
    public static final String LOCAL_ERROR_TXT = LOCAL_FILE_PATH + "/error.txt";
    public static final String LOCAL_CONFIG_TXT = LOCAL_FILE_PATH + "/config.txt";

    //获取机器码
    public static String getSerialNumber() {

        String serial = null;

        try {

            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);

            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {

            e.printStackTrace();

        }

        return serial;

    }


    //
}
