package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 终端控制类任务
 */

public class ControlBean {
    public String taskcount;
    public String tasktype;
    public String taskid;
    public String control;//控制指令 0：重启 1：休眠（远程关机）2：唤醒（远程开机）

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

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }
}
