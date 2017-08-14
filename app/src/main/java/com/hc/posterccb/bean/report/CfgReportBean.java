package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by alex on 2017/8/11.
 */
@XStreamAlias("command")
public class CfgReportBean {
    public String ip;
    public String mac;
    public String geteway;
    public String dns;
    public String mask;
    public String appversion;
    public String startuptime;
    public String shutdowntime;
    public String diskspacealarm;
    public String serverconfig;
    public String selectinterval;
    public String volume;
    public String ftpserver;
    public String httpserver;
    public String downloadrate;
    public String downloadtime;
    public String logserver;
    public String uploadlogtime;
    public String keeplogtime;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getGeteway() {
        return geteway;
    }

    public void setGeteway(String geteway) {
        this.geteway = geteway;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getStartuptime() {
        return startuptime;
    }

    public void setStartuptime(String startuptime) {
        this.startuptime = startuptime;
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
