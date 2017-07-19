package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 配置终端参数任务
 */

public class ConfigBean {
    public String taskcount;
    public String taskitem;
    public String taskid;
    public String staruptime;//开机时间 HH:MM:SS
    public String shutdowntime;//关机时间 HH:MM:SS
    public String diskspacealarm;//硬盘告警阀值，单位（MB）
    public String serverconfig;//服务器信息 http://ip:port/appname
    public String selectinterval;//轮询时间间隔，单位（s） 默认值10秒
    public String volume;//终端音量0-100
    public String ftpserver;//ftp下载服务器地址列表
    public String httpserver;//http下载服务器地址列表
    public String downloadrate;//终端下载速率：kb/s
    public String downloadtime;//下载时间段：HH:MM:SS- HH:MM:SS，
    public String logserver;//ftp://user:pwd@serverip:port/logdir
    public String uploadlogtime;//日志定时上传时间 HH:MM:SS
    public String keeplogtime;//日志保留时间 默认7天

    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String taskcount) {
        this.taskcount = taskcount;
    }

    public String getTaskitem() {
        return taskitem;
    }

    public void setTaskitem(String taskitem) {
        this.taskitem = taskitem;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getStaruptime() {
        return staruptime;
    }

    public void setStaruptime(String staruptime) {
        this.staruptime = staruptime;
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
}
