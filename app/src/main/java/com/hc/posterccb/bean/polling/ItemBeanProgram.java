package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanProgram{
    public int csize; //文件大小 单位byte
    public String link; //下载相对路径:/dir/filename    文件的相对下载地址，终端负责和配置信息中的下载服务器组合成完整url
    public String md5;//文件的md5值
    public String playmode;//>播放类型：插播=0、正常播=1

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

    public String getPlaymode() {
        return playmode;
    }

    public void setPlaymode(String playmode) {
        this.playmode = playmode;
    }

    @Override
    public String toString() {
        return "PlayTask{" +
                "csize=" + csize +
                ", link='" + link + '\'' +
                ", md5='" + md5 + '\'' +
                ", playmode='" + playmode + '\'' +
                '}';
    }
}
