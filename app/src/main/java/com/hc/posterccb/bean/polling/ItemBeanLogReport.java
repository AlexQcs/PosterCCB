package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanLogReport{
    public String logtype;

    public String getLogtype() {
        return logtype;
    }

    public void setLogtype(String logtype) {
        this.logtype = logtype;
    }

    @Override
    public String toString() {
        return "ItemBeanLogReport{" +
                "logtype='" + logtype + '\'' +
                '}';
    }
}
