package com.hc.posterccb;

import android.os.Environment;

import java.lang.reflect.Method;

/**
 * Created by alex on 2017/7/1.
 */

public class Constant {
    public static final String XML_HEAD = "\"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\\n\"";
    public static final String VIDEO1_PATH = Environment.getExternalStorageDirectory() + "/mv1.mp4";
    public static final String VIDEO2_PATH = Environment.getExternalStorageDirectory() + "/mv2.mp4";
    public static final String VIDEO3_PATH = Environment.getExternalStorageDirectory() + "/mv3.mp4";
    public static final String UDP_TESTPATH = Environment.getExternalStorageDirectory() + "/Misc/test/";




    public static final String TASKTYPE = "tasktype";
    public static final String TASKREPORT="taskreport";
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
    //播放控制类任务
    public static final String POLLING_CONTROLPROGRAM = "controlprogram";
    //通知终端配置上报
    public static final String POLLING_CFGREPORT = "cfgreport";
    //通知终端工作状态上报
    public static final String POLLING_WORKSTATUSREPORT = "workstatusreport";
    //通知终端在播内容上报
    public static final String POLLING_MONITORREPORT="monitorreport";
    //通知终端日志上报
    public static final String POLLING_LOGREPORT="logreport";
    //通知终端下载资源文件
    public static final String POLLING_DOWNLOADRES="downloadres";
    //通知终端上报资源下载状态
    public static final String POLLING_DOWNLOADSTATUSREPORT="downloadstatusreport";
    //


    //根目录app文件夹
    public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory() + "/PosterCCB";
    //升级文件保存文件夹
    public static final String LOCAL_APK_PATH=LOCAL_FILE_PATH+"/apk";
    //保存生成的日志文件夹
    public static final String LOCAL_LOG_PATH = LOCAL_FILE_PATH + "/log";
    //播放资源文件夹
    public static final String LOCAL_PROGRAM_PATH = LOCAL_FILE_PATH + "/media";
    //********
    public static final String LOCAL_PROGRAM_TXT = LOCAL_PROGRAM_PATH + "/program.txt";
    //保存错误日志文件夹
    public static final String LOCAL_ERROR_TXT = LOCAL_FILE_PATH + "/error.txt";
    //系统配置文件夹
    public static final String LOCAL_CONFIG_TXT = LOCAL_FILE_PATH + "/config.txt";
    //********
    public static final String LOCAL_PROGRAM_NORMAL_TXT = LOCAL_PROGRAM_PATH + "/normal.txt";
    //********
    public static final String LOCAL_PROGRAM_INTER_TXT = LOCAL_PROGRAM_PATH + "/inter.txt";
    //播放资源配置文件夹
    public static final String LOCAL_PROGRAM_MEDIACFG_PATH=LOCAL_FILE_PATH+"/";
    //正常播放机节目单
    public static final String LOCAL_PROGRAM_LIST_PATH=LOCAL_PROGRAM_PATH+"/program.xml";
    //插播播放节目单
    public static final String LOCAL_INSERT_PROGRAM_LIST_PATH=LOCAL_PROGRAM_PATH+"/insert_program.xml";
    //正常播放 播放资源下载列表
    public static final String LOCAL_RESOURCE_LIST_PATH=LOCAL_PROGRAM_PATH+"/resource.xml";
    //插播播放 播放资源下载列表
    public static final String LOCAL_INSERT_RESOURCE_LIST_PATH=LOCAL_PROGRAM_PATH+"/insert_resource.xml";

    public static final String LICENSE_KEY_FOOT = "Ho0cUp";

    //解密完成后生成序列号路径
    public static String CREAT_DES_FILE_PATH="/data/data/com.hc.posterccb/files";
    //解密完成后生成的文本文件
    public static String CREAT_DES_FILE=CREAT_DES_FILE_PATH+"/DesSerial.txt";

    //--------------secnumber
    public static final String RESULT_PATH = Environment.getRootDirectory().getPath() + "/bin/sleep.bkup";
    public static final String DEBUG_PATH = Environment.getRootDirectory().getPath() + "/etc/debug.txt";
    public static final String LOGCAT_PATH = Environment.getRootDirectory().getPath() + "/etc/security/logcat.txt";
    //Serial_Number path
    public static final String SERIAL_PATH =Environment.getRootDirectory().getPath() + "/etc/security/BSN.txt";

    //项目包名
    public static final String PACKAGE_APPLICATION="com.hc.posterccb";

    //序列号
    public static final String SERIAL_NUMBER=getSerialNumber();

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
