package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 通知终端配置上报
 */

public class LogReportBean {
    public String taskcount;
    public String tasktype;
    public String taskid;
    public String logtype;

    public String getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(String taskcount) {
        this.taskcount = taskcount;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }
}
