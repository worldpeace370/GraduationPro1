package com.lebron.graduationpro1.scanpage.model;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class CollectInfoBean {
    private String mTemperature;
    private String mRate;
    private String mCreateTime;

    public String getTemperature() {
        return mTemperature;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

    public String getRate() {
        return mRate;
    }

    public void setRate(String rate) {
        mRate = rate;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    @Override
    public String toString() {
        return "mTemperature = " + mTemperature + ", mRate = " + mRate
                + ", mCreateTime = " + mCreateTime;
    }
}
