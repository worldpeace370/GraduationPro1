package com.lebron.graduationpro1.scanpage.model;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class CollectInfoBean {
    private String mtemperature;
    private String mrate;
    private String mcreateTime;

    public String getMtemperature() {
        return mtemperature;
    }

    public void setMtemperature(String mtemperature) {
        this.mtemperature = mtemperature;
    }

    public String getMrate() {
        return mrate;
    }

    public void setMrate(String mrate) {
        this.mrate = mrate;
    }

    public String getMcreateTime() {
        return mcreateTime;
    }

    public void setMcreateTime(String mcreateTime) {
        this.mcreateTime = mcreateTime;
    }

    @Override
    public String toString() {
        return "mtemperature = " + mtemperature + ", mrate = " + mrate
                + ", mcreateTime = " + mcreateTime;
    }
}
