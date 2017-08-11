package com.hc.posterccb.bean.report;

/**
 * Created by alex on 2017/8/11.
 */

public class UpgradeReportBean {
    public String taskid;
    public String version;
    public String status;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
