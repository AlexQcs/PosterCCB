package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by alex on 2017/8/12.
 */
@XStreamAlias("command")
public class ReportWorkStatusBean {
    public String taskid;
    public String playerstatus;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPlayerstatus() {
        return playerstatus;
    }

    public void setPlayerstatus(String playerstatus) {
        this.playerstatus = playerstatus;
    }
}
