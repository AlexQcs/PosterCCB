package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 播放控制类任务
 */

public class ControlProgramBean {
    public String taskcount;//任务数量
    public String tasktype;//任务类型
    public String taskid;//任务id
    public String cmd;//任务指令 1：暂停：停止终端播放 2：恢复：暂停终端播放 3：删除：删除当前播放列表；4: 取消插播：取消当前插播任务

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

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
