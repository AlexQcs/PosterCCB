package com.hc.posterccb.bean.polling;

import java.util.List;

/**
 * Created by alex on 2017/9/8.
 */

public class BasePollingBean {
    private int taskcount;
    private List<AbstractBeanTaskItem> taskitems;

    public int getTaskcount() {
        return taskcount;
    }

    public void setTaskcount(int taskcount) {
        this.taskcount = taskcount;
    }

    public List<AbstractBeanTaskItem> getTaskitems() {
        return taskitems;
    }

    public void setTaskitems(List<AbstractBeanTaskItem> taskitems) {
        this.taskitems = taskitems;
    }

    @Override
    public String toString() {
        return "BasePollingBean{" +
                "taskcount=" + taskcount +
                ", taskitems=" + taskitems +
                '}';
    }
}
