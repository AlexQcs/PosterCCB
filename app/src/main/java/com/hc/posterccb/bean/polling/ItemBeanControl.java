package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanControl{
    private int control;//控制指令 0：重启 1：休眠（远程关机）2：唤醒（远程开机）

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "ItemBeanControl{" +
                "control=" + control +
                '}';
    }
}
