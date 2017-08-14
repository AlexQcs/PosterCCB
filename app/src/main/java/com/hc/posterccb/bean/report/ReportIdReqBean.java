package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by alex on 2017/8/8.
 */
@XStreamAlias("command")
public class ReportIdReqBean {
    public String result;//响应结果代码 0:成功 1:失败
    public String errorinfo;//错误描述

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(String errorinfo) {
        this.errorinfo = errorinfo;
    }
}
