package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanUpGrade{
    public String version;//版本
    public String link;//升级文件下载链接

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "ItemBeanUpGrade{" +
                "version='" + version + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
