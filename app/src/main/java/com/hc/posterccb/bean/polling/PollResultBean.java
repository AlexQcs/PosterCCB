package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 轮询响应描述类
 */

public class PollResultBean {
    public String taskcount;//任务数量
    public String tasktype;//任务类型
    public String taskid;//任务编号    uuid

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

    @Override
    public String toString() {
        return "PollBean{" +
                "taskcount=" + taskcount +
                ", tasktype='" + tasktype + '\'' +
                ", taskid='" + taskid + '\'' +
                '}';
    }
}
