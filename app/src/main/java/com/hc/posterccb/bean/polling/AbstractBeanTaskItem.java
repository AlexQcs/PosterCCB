package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class AbstractBeanTaskItem <T>{
    private String tasktype;
    private String taskid;
    private T t;

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

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "AbstractBeanTaskItem{" +
                "tasktype='" + tasktype + '\'' +
                ", taskid='" + taskid + '\'' +
                ", t=" + t +
                '}';
    }
}
