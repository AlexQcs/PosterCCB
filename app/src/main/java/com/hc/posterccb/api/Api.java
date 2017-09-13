package com.hc.posterccb.api;

import java.io.File;

/**
 * Created by alex on 2017/7/1.
 */

public class Api {
    public static final String LOCALHOST="http://192.168.0.30:8080";
    public static final String XMLSERVER=LOCALHOST+File.separator+"xmlserver";
    public static final String REV_XML=XMLSERVER+ File.separator+"revXml";

    public static final String SFTP_USER="alex";
    public static final String SFTP_PSD="sheng123";
    public static final String SFTP_PATH="192.168.0.30";

    //server-url
    public static String BASE_URL ="http://192.168.0.30:8080/";


    //请求头  command
    public static String  POST_POLLING="getTask";






}
