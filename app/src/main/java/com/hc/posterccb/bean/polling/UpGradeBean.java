package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 * 软件升级任务
 */

public class UpGradeBean {
    public String taskcount;
    public String tasktype;
    public String taskid;
    public String version;//版本
    public String link;//升级文件下载链接

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "UpGradeBean{" +
                "taskcount='" + taskcount + '\'' +
                ", tasktype='" + tasktype + '\'' +
                ", taskid='" + taskid + '\'' +
                ", version='" + version + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
