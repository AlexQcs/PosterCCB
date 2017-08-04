package com.hc.posterccb.bean.program;

import java.util.ArrayList;

/**
 * Created by alex on 2017/7/25.
 */

public class Program {
    public String type;
    public String areatype;
    public String stdtime;
    public String edtime;
    public ArrayList<ProgramRes> mList;

    public ArrayList<ProgramRes> getList() {
        return mList;
    }

    public void setList(ArrayList<ProgramRes> list) {
        mList = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAreatype() {
        return areatype;
    }

    public void setAreatype(String areatype) {
        this.areatype = areatype;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Program)) return false;

        Program program = (Program) o;

        if (getType() != null ? !getType().equals(program.getType()) : program.getType() != null)
            return false;
        if (getAreatype() != null ? !getAreatype().equals(program.getAreatype()) : program.getAreatype() != null)
            return false;
        if (getStdtime() != null ? !getStdtime().equals(program.getStdtime()) : program.getStdtime() != null)
            return false;
        if (getEdtime() != null ? !getEdtime().equals(program.getEdtime()) : program.getEdtime() != null)
            return false;
        return getList() != null ? getList().equals(program.getList()) : program.getList() == null;

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getAreatype() != null ? getAreatype().hashCode() : 0);
        result = 31 * result + (getStdtime() != null ? getStdtime().hashCode() : 0);
        result = 31 * result + (getEdtime() != null ? getEdtime().hashCode() : 0);
        result = 31 * result + (getList() != null ? getList().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Program{" +
                "type='" + type + '\'' +
                ", areatype='" + areatype + '\'' +
                ", stdtime='" + stdtime + '\'' +
                ", edtime='" + edtime + '\'' +
                ", mList=" + mList +
                '}';
    }
}
