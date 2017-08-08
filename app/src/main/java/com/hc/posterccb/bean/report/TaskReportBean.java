package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 上报任务id
 * Created by alex on 2017/7/26.
 */

@XStreamAlias("command")
public class TaskReportBean {
    String taskid;
    String tasktype;
    String status;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
