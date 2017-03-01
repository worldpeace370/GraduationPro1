package com.lebron.graduationpro1.scanpage.model;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class CollectInfoBean {
    private String temperature;
    private String rate;
    private String createtime;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "temperature = " + temperature + ", rate = " + rate
                + ", createtime = " + createtime;
    }
}
