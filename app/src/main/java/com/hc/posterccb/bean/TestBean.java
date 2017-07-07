package com.hc.posterccb.bean;

/**
 * Created by alex on 2017/7/3.
 */

public class TestBean {
    public String taskid;
    public String tasktype;
    public String status;

    @Override
    public String toString() {
        return "Demo02{" +
                "taskid='" + taskid + '\'' +
                ", tasktype='" + tasktype + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
