package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by alex on 2017/8/8.
 */
@XStreamAlias("command")
public class ReportIdReqBean {
    public String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
