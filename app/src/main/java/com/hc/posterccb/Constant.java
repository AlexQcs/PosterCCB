package com.hc.posterccb;

import android.os.Environment;

import com.hc.posterccb.util.system.NetworkUtil;

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


    //    public static final String BASE_URL="http://128.160.97.6:16300/zhyh/";
//    public static final String BASE_URL="http://128.160.97.6:16300/zhyh/PlayDev.do";
    //默认服务器地址
    public static final String BASE_URL = "http://54.0.161.68:16300/zhyh/";
    public static final String BASE_PORT="PlayDev";

    //请求同步资源列表
    public static final String  RESOURCESYNC="resourcesync";

    public static final String TASKTYPE = "tasktype";
    public static final String TASKREPORT = "taskreport";
    public static final String XML_STARTDOM = "command";
    public static final String XML_LISTTAG = "content";
    public static final String XML_CONFIG = "config";
    public static final String REPORT_CONFIG = "configReport";

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
    public static final String POLLING_MONITORREPORT = "monitorreport";
    //通知终端日志上报
    public static final String POLLING_LOGREPORT = "logreport";
    //通知终端下载资源文件
    public static final String POLLING_DOWNLOADRES = "downloadres";
    //通知终端上报资源下载状态
    public static final String POLLING_DOWNLOADSTATUSREPORT = "downloadstatusreport";
    //


    //shareprefrence字符
    //默认播放列表
    public static final String SP_PROGRAM_DEF = "defaultpls";
    //升级任务
    public static final String SP_UPGRADE = "UpGradeBean";
    //baseurl
    public static final String SP_BASEURL = "baseurl";
    //realtime
    public static final String SP_REALTIME = "realtime";
    //即时消息时间
    public static final String SP_STARTREALTIMEMSG = "StartRealTimeMsg";
    public static final String SP_ENDREALTIMEMSG = "EndRealTimeMsg";


    //根目录app文件夹
    public static final String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory() + "/PosterCCB";
    //升级文件保存文件夹
    public static final String LOCAL_APK_PATH = LOCAL_FILE_PATH + "/apk";
    //保存生成的日志文件夹
    public static final String LOCAL_LOG_PATH = LOCAL_FILE_PATH + "/log";
    //播放资源文件夹
    public static final String LOCAL_PROGRAM_PATH = LOCAL_FILE_PATH + "/media";

    //保存错误日志文件夹
    public static final String LOCAL_ERROR_TXT = LOCAL_FILE_PATH + "/error.txt";
    //系统配置文件夹
    public static final String LOCAL_CONFIG_TXT = LOCAL_FILE_PATH + "/config.txt";
    //********
    public static final String LOCAL_PROGRAM_NORMAL_TXT = LOCAL_PROGRAM_PATH + "/normal.txt";
    //********
    public static final String LOCAL_PROGRAM_INTER_TXT = LOCAL_PROGRAM_PATH + "/inter.txt";
    //播放资源配置文件夹
    public static final String LOCAL_PROGRAM_MEDIACFG_PATH = LOCAL_FILE_PATH + "/mediacfg";
    //升级apk文件夹
    public static final String LOCAL_UPGRO_PATH = LOCAL_FILE_PATH + "/apk";
    //升级apk路径
    public static final String LOCAL_UPAPK_PATH = LOCAL_UPGRO_PATH + "/up.apk";
    //********
    public static final String LOCAL_PROGRAM_CFG = LOCAL_PROGRAM_MEDIACFG_PATH + "/programConfig.xml";
    //正常播放机节目单
    public static final String LOCAL_PROGRAM_LIST_PATH = LOCAL_PROGRAM_MEDIACFG_PATH + "/program.xml";
    //插播播放节目单
    public static final String LOCAL_INSERT_PROGRAM_LIST_PATH = LOCAL_PROGRAM_MEDIACFG_PATH + "/insert_program.xml";
    //模板we
    public static final String LOCAL_MODEL_LIST_PATH = LOCAL_PROGRAM_MEDIACFG_PATH + "/model.xml";
    //正常播放 播放资源下载列表
    public static final String LOCAL_RESOURCE_LIST_PATH = LOCAL_PROGRAM_MEDIACFG_PATH + "/resource.xml";
    //插播播放 播放资源下载列表
    public static final String LOCAL_INSERT_RESOURCE_LIST_PATH = LOCAL_PROGRAM_MEDIACFG_PATH + "/insert_resource.xml";

    public static final String LICENSE_KEY_FOOT = "Ho0cUp";

    //解密完成后生成序列号路径
    public static String CREAT_DES_FILE_PATH = "/data/data/com.hc.posterccb/files";
    //解密完成后生成的文本文件
    public static String CREAT_DES_FILE = CREAT_DES_FILE_PATH + "/DesSerial.txt";


    //--------------secnumber
    public static final String RESULT_PATH = Environment.getRootDirectory().getPath() + "/bin/sleep.bkup";
    public static final String DEBUG_PATH = Environment.getRootDirectory().getPath() + "/etc/debug.txt";
    public static final String LOGCAT_PATH = Environment.getRootDirectory().getPath() + "/etc/security/logcat.txt";
    //Serial_Number path
    public static final String SERIAL_PATH = Environment.getRootDirectory().getPath() + "/etc/security/BSN.txt";

    //项目包名
    public static final String PACKAGE_APPLICATION = "com.hc.posterccb";

    //序列号
    public static final String SERIAL_NUMBER = getSerialNumber();

    public static final String MAC = NetworkUtil.getLocalMacAddressFromIp();

    //播放模式
    public static final String PROGRAM_MODE_NORMAL = "normal";
    public static final String PROGRAM_MODE_INTER = "inter";
    public static final String PROGRAM_MODE_DEF ="default";

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
