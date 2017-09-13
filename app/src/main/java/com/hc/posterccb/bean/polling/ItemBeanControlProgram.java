package com.hc.posterccb.bean.polling;

/**
 * Created by alex on 2017/9/8.
 */

public class ItemBeanControlProgram{
    public String cmd;//任务指令 1：暂停：停止终端播放 2：恢复：暂停终端播放 3：删除：删除当前播放列表；4: 取消插播：取消当前插播任务

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "ItemBeanControlProgram{" +
                "cmd='" + cmd + '\'' +
                '}';
    }
}
