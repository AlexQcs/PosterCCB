package com.hc.posterccb;

import android.os.Environment;

import java.lang.reflect.Method;

/**
 * Created by alex on 2017/7/1.
 */

public class Constant {
    public static final String XML_HEAD = "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n\"";
    public static final String VIDEO_PATH= Environment.getExternalStorageDirectory() +"/Misc/mv.mp4";
    public static final String UDP_TESTPATH=Environment.getExternalStorageDirectory()+"/Misc/test/";

    public static final String TASKTYPE="tasktype";

    public static final String XML_STARTDOM="command";
    public static final String XML_LISTTAG="content";

    //轮询返回响应类型
    public static final String POLLING_PROGRAM="program";
    public static final String POLLING_UPGRADE="upgrade";
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
