package com.hc.posterccb.bean.program;

import android.support.annotation.NonNull;

/**
 * Created by alex on 2017/7/25.
 */

public class ProgramRes implements Comparable<ProgramRes> {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProgramRes)) return false;

        ProgramRes res = (ProgramRes) o;

        if (getResnam() != null ? !getResnam().equals(res.getResnam()) : res.getResnam() != null)
            return false;
        if (getResid() != null ? !getResid().equals(res.getResid()) : res.getResid() != null)
            return false;
        if (getArea() != null ? !getArea().equals(res.getArea()) : res.getArea() != null)
            return false;
        if (getStdtime() != null ? !getStdtime().equals(res.getStdtime()) : res.getStdtime() != null)
            return false;
        if (getEdtime() != null ? !getEdtime().equals(res.getEdtime()) : res.getEdtime() != null)
            return false;
        if (getPlaycnt() != null ? !getPlaycnt().equals(res.getPlaycnt()) : res.getPlaycnt() != null)
            return false;
        return getPriority() != null ? getPriority().equals(res.getPriority()) : res.getPriority() == null;

    }

    @Override
    public int compareTo(@NonNull ProgramRes o) {
        int i = Integer.parseInt(this.getPriority()) - Integer.parseInt(o.getPriority());
        return i;
    }
}
