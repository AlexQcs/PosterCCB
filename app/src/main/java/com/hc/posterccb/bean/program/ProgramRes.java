package com.hc.posterccb.bean.program;

/**
 * Created by alex on 2017/7/25.
 */

public class ProgramRes {

    public String resnam;
    public String resid;
    public String area;
    public String stdtime;
    public String edtime;
    public String playcnt;
    public String priority;

    public String getResnam() {
        return resnam;
    }

    public void setResnam(String resnam) {
        this.resnam = resnam;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStdtime() {
        return stdtime;
    }

    public void setStdtime(String stdtime) {
        this.stdtime = stdtime;
    }

    public String getEdtime() {
        return edtime;
    }

    public void setEdtime(String edtime) {
        this.edtime = edtime;
    }

    public String getPlaycnt() {
        return playcnt;
    }

    public void setPlaycnt(String playcnt) {
        this.playcnt = playcnt;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "ProgramRes{" +
                "resnam='" + resnam + '\'' +
                ", resid='" + resid + '\'' +
                ", area='" + area + '\'' +
                ", stdtime='" + stdtime + '\'' +
                ", edtime='" + edtime + '\'' +
                ", playcnt='" + playcnt + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
