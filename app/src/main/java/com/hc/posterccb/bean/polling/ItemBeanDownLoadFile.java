package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanDownLoadFile{
    private int csize;//文件大小
    private String link;//下载相对路径
    private String md5;//文件的md5值
    private int playmode;//播放模式


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

    @Override
    public String toString() {
        return "ItemBeanDownLoadFile{" +
                "csize=" + csize +
                ", link='" + link + '\'' +
                ", md5='" + md5 + '\'' +
                ", playmode=" + playmode +
                '}';
    }
}
