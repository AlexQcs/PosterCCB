package com.hc.posterccb.bean;

/**
 * Created by alex on 2017/7/10.
 */

public class PollingBean {
    String command;
    String mac;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "PollingBean{" +
                "command='" + command + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
