package com.hc.posterccb.util.ccbutils;

/**
 * Created by alex on 2017/8/5.
 */

public class Protocol {

    /**
     * 获取下载链接中的文件所在目录
     * @param link
     * @return
     */
    public static String getLinkRemotePath(String link){
        String[] tempArray=link.split("/");
        if (tempArray.length>0){
            return tempArray[tempArray.length-2];
        }else {
            return "";
        }
    }

    /**
     * 获取下载链接中的文件名
     * @param link
     * @return
     */
    public static String getLinkFileName(String link){
        String[] tempArray=link.split("/");
        if (tempArray.length>0){
            return tempArray[tempArray.length-1];
        }else {
            return "";
        }
    }
}
