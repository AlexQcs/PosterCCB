package com.hc.posterccb.bean;

/**
 * Created by alex on 2017/9/7.
 */

public class IntervalBean {
    public static volatile IntervalBean singleton;

    private int time;

    /**
     * 构造函数私有，禁止外部实例化
     */
    private IntervalBean() {};

    public static IntervalBean getInstance() {
        if (singleton == null) {
            synchronized (IntervalBean.class) {
                if (singleton == null) {
                    singleton = new IntervalBean();
                }
            }
        }
        return singleton;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
