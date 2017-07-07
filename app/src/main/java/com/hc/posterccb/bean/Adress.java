package com.hc.posterccb.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by alex on 2017/6/30.
 */
@XStreamAlias("Adress")
public  class Adress {
    private String street;
    private int number;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
