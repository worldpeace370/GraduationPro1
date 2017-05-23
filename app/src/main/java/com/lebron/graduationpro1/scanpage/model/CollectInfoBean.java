package com.lebron.graduationpro1.scanpage.model;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class CollectInfoBean {
    private String temp_out;
    private String temp_in;
    private String water_press;
    private String create_time;

    public String getTemp_out() {
        return temp_out;
    }

    public void setTemp_out(String temp_out) {
        this.temp_out = temp_out;
    }

    public String getTemp_in() {
        return temp_in;
    }

    public void setTemp_in(String temp_in) {
        this.temp_in = temp_in;
    }

    public String getWater_press() {
        return water_press;
    }

    public void setWater_press(String water_press) {
        this.water_press = water_press;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "temp_out = " + temp_out + ", temp_in = " + temp_out
                + ", water_press = " + water_press + ", create_time = " + create_time;
    }
}
