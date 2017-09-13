package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanConfig{
    private String startuptime;//开机时间 HH:MM:SS
    private String shutdowntime;//关机时间 HH:MM:SS
    private String diskspacealarm;//硬盘告警阀值，单位（MB）
    private String serverconfig;//服务器信息 http://ip:port/appname
    private String selectinterval;//轮询时间间隔，单位（s） 默认值10秒
    private String volume;//终端音量0-100
    private String ftpserver;//ftp下载服务器地址列表
    private String httpserver;//http下载服务器地址列表
    private String downloadrate;//终端下载速率：kb/s
    private String downloadtime;//下载时间段：HH:MM:SS- HH:MM:SS，
    private String logserver;//ftp://user:pwd@serverip:port/logdir
    private String uploadlogtime;//日志定时上传时间 HH:MM:SS
    private String keeplogtime;//日志保留时间 默认7天

    public String getStaruptime() {
        return startuptime;
    }

    public void setStaruptime(String staruptime) {
        this.startuptime = staruptime;
    }

    public String getShutdowntime() {
        return shutdowntime;
    }

    public void setShutdowntime(String shutdowntime) {
        this.shutdowntime = shutdowntime;
    }

    public String getDiskspacealarm() {
        return diskspacealarm;
    }

    public void setDiskspacealarm(String diskspacealarm) {
        this.diskspacealarm = diskspacealarm;
    }

    public String getServerconfig() {
        return serverconfig;
    }

    public void setServerconfig(String serverconfig) {
        this.serverconfig = serverconfig;
    }

    public String getSelectinterval() {
        return selectinterval;
    }

    public void setSelectinterval(String selectinterval) {
        this.selectinterval = selectinterval;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFtpserver() {
        return ftpserver;
    }

    public void setFtpserver(String ftpserver) {
        this.ftpserver = ftpserver;
    }

    public String getHttpserver() {
        return httpserver;
    }

    public void setHttpserver(String httpserver) {
        this.httpserver = httpserver;
    }

    public String getDownloadrate() {
        return downloadrate;
    }

    public void setDownloadrate(String downloadrate) {
        this.downloadrate = downloadrate;
    }

    public String getDownloadtime() {
        return downloadtime;
    }

    public void setDownloadtime(String downloadtime) {
        this.downloadtime = downloadtime;
    }

    public String getLogserver() {
        return logserver;
    }

    public void setLogserver(String logserver) {
        this.logserver = logserver;
    }

    public String getUploadlogtime() {
        return uploadlogtime;
    }

    public void setUploadlogtime(String uploadlogtime) {
        this.uploadlogtime = uploadlogtime;
    }

    public String getKeeplogtime() {
        return keeplogtime;
    }

    public void setKeeplogtime(String keeplogtime) {
        this.keeplogtime = keeplogtime;
    }

    @Override
    public String toString() {
        return "ItemBeanConfig{" +
                "startuptime='" + startuptime + '\'' +
                ", shutdowntime='" + shutdowntime + '\'' +
                ", diskspacealarm='" + diskspacealarm + '\'' +
                ", serverconfig='" + serverconfig + '\'' +
                ", selectinterval='" + selectinterval + '\'' +
                ", volume='" + volume + '\'' +
                ", ftpserver='" + ftpserver + '\'' +
                ", httpserver='" + httpserver + '\'' +
                ", downloadrate='" + downloadrate + '\'' +
                ", downloadtime='" + downloadtime + '\'' +
                ", logserver='" + logserver + '\'' +
                ", uploadlogtime='" + uploadlogtime + '\'' +
                ", keeplogtime='" + keeplogtime + '\'' +
                '}';
    }
}
