package com.hc.posterccb.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Created by alex on 2017/6/30.
 */
@XStreamAlias("Demo")
public class Demo implements Serializable {
    private int taskid;
    private int tasktype;
    private int status;
    @XStreamImplicit(itemFieldName="Adresses")//这个节点可以写成类也可以写成集合,一般还是要写成类比较好理解
    private List<Adress> mAdresses;
    public int getTaskid() {
        return taskid;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getTasktype() {
        return tasktype;
    }

    public void setTasktype(int tasktype) {
        this.tasktype = tasktype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Adress> getAdresses() {
        return mAdresses;
    }

    public void setAdresses(List<Adress> adresses) {
        mAdresses = adresses;
    }


}
