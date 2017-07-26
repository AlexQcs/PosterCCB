package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/7/14.
 */

public class DownLoadFileBean {
    public String taskcount;//任务数量
    public String tasktype;//任务类型
    public String taskid;//任务编号    uuid
    private int csize;//文件大小
    private String link;//下载相对路径
    private String md5;//文件的md5值
    private int playmode;//播放模式


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

    public int getCsize() {
        return csize;
    }

    public void setCsize(int csize) {
        this.csize = csize;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getPlaymode() {
        return playmode;
    }

    public void setPlaymode(int playmode) {
        this.playmode = playmode;
    }
}
