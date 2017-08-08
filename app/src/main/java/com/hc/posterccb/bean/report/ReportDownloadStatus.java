package com.hc.posterccb.bean.report;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by alex on 2017/7/26.
 */
@XStreamAlias("command")
public class ReportDownloadStatus {
    public String status;
    @XStreamImplicit(itemFieldName = "detail")
    public List<DetailBean> mDetailList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DetailBean> getDetailList() {
        return mDetailList;
    }

    public void setDetailList(List<DetailBean> detailList) {
        mDetailList = detailList;
    }
}
