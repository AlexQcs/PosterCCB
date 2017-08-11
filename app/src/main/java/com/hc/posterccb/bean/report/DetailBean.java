package com.hc.posterccb.bean.report;

/**
 * Created by alex on 2017/7/26.
 */

//下载详细信息
public class DetailBean {
    private String resid;
    private String filename;
    private String process;
    private String status;

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
