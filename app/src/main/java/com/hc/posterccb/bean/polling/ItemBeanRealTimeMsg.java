package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanRealTimeMsg{
    public String fontsize;//字体大小
    public String bgcolor;//#FFFFFF
    public String fontcolor;//#FFFFFF
    public String position;//显示位置  top:上方  under:下方
    public String speed;//滚动速度 0：慢；1：正常；2快
    public String starttime;//开始时间
    public String endtime;//结束时间
    public String count;//播放次数: 用数字表示，约定0表示一直播放
    public String timelength;//播放时长 单位(s),约定0一直播放
    public String message;//消息内容

    public String getFontsize() {
        return fontsize;
    }

    public void setFontsize(String fontsize) {
        this.fontsize = fontsize;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTimelength() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength = timelength;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ItemBeanRealTimeMsg{" +
                "fontsize='" + fontsize + '\'' +
                ", bgcolor='" + bgcolor + '\'' +
                ", fontcolor='" + fontcolor + '\'' +
                ", position='" + position + '\'' +
                ", speed='" + speed + '\'' +
                ", starttime='" + starttime + '\'' +
                ", endtime='" + endtime + '\'' +
                ", count='" + count + '\'' +
                ", timelength='" + timelength + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
