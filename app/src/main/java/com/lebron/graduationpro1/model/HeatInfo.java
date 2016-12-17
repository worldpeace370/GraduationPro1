package com.lebron.graduationpro1.model;

/**供暖节点的数据信息模型
 * Created by wuxia on 2016/12/17.
 * Contacts by wuxiangkun2015@163.com
 */

public class HeatInfo {
    /**
     * 水温的平均值，最大值，最小值
     * 阈值 最大，最小
     */
    private String mAverageTemp;
    private String mMaxTemp;
    private String mMinTemp;
    private String mThresholdTopTemp;
    private String mThresholdBottomTemp;

    /**
     * 转速(流量)的平均值，最大值，最小值
     * 阈值 最大，最小
     */
    private String mAverageRate;
    private String mMaxRate;
    private String mMinRate;
    private String mThresholdTopRate;
    private String mThresholdBottomRate;

    /**
     * 服务器计算上面那些值，并存储的时间点
     */
    private String mCurrentTime;

    public  HeatInfo() {

    }

    public String getAverageTemp() {
        return mAverageTemp;
    }

    public void setAverageTemp(String averageTemp) {
        mAverageTemp = averageTemp;
    }

    public String getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        mMaxTemp = maxTemp;
    }

    public String getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(String minTemp) {
        mMinTemp = minTemp;
    }

    public String getThresholdTopTemp() {
        return mThresholdTopTemp;
    }

    public void setThresholdTopTemp(String thresholdTopTemp) {
        mThresholdTopTemp = thresholdTopTemp;
    }

    public String getThresholdBottomTemp() {
        return mThresholdBottomTemp;
    }

    public void setThresholdBottomTemp(String thresholdBottomTemp) {
        mThresholdBottomTemp = thresholdBottomTemp;
    }

    public String getAverageRate() {
        return mAverageRate;
    }

    public void setAverageRate(String averageRate) {
        mAverageRate = averageRate;
    }

    public String getMaxRate() {
        return mMaxRate;
    }

    public void setMaxRate(String maxRate) {
        mMaxRate = maxRate;
    }

    public String getMinRate() {
        return mMinRate;
    }

    public void setMinRate(String minRate) {
        mMinRate = minRate;
    }

    public String getThresholdTopRate() {
        return mThresholdTopRate;
    }

    public void setThresholdTopRate(String thresholdTopRate) {
        mThresholdTopRate = thresholdTopRate;
    }

    public String getThresholdBottomRate() {
        return mThresholdBottomRate;
    }

    public void setThresholdBottomRate(String thresholdBottomRate) {
        mThresholdBottomRate = thresholdBottomRate;
    }

    public String getCurrentTime() {
        return mCurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        mCurrentTime = currentTime;
    }
}
