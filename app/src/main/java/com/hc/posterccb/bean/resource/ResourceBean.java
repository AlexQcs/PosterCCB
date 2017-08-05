package com.hc.posterccb.bean.resource;

/**
 * Created by alex on 2017/8/5.
 */

public class ResourceBean {
    public String ftpAdd; //ftp下载地址
    public String href;//http下载地址
    public String md5;//md5值
    public String filesize;//文件大小
    public String resname;//文件名
    public String resid;//文件id
    public String type;//文件类型


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFtpAdd() {
        return ftpAdd;
    }

    public void setFtpAdd(String ftpAdd) {
        this.ftpAdd = ftpAdd;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getFilesize() {
        return filesize;
    }

    public void setFilesize(String filesize) {
        this.filesize = filesize;
    }

    public String getResname() {
        return resname;
    }

    public void setResname(String resname) {
        this.resname = resname;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    @Override
    public String toString() {
        return "ResourceBean{" +
                "ftpAdd='" + ftpAdd + '\'' +
                ", href='" + href + '\'' +
                ", md5='" + md5 + '\'' +
                ", filesize='" + filesize + '\'' +
                ", resname='" + resname + '\'' +
                ", resid='" + resid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
