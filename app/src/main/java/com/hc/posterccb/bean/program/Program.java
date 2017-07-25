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
