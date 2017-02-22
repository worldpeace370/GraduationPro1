package com.lebron.graduationpro1.detailpage.model;

/**供暖节点的数据信息模型
 * Created by wuxia on 2016/12/17.
 * Contacts by wuxiangkun2015@163.com
 */

public class HeatInfo {
    /**
     * 水温的平均值，最大值，最小值
     */
    private String average_temp;
    private String max_temp;
    private String min_temp;

    /**
     * 转速(流量)的平均值，最大值，最小值
     */
    private String average_rate;
    private String max_rate;
    private String min_rate;

    /**
     * 最后上传的时间
     */
    private String last_time;

    public String getAverage_temp() {
        return average_temp;
    }

    public void setAverage_temp(String average_temp) {
        this.average_temp = average_temp;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public void setMax_temp(String max_temp) {
        this.max_temp = max_temp;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public void setMin_temp(String min_temp) {
        this.min_temp = min_temp;
    }

    public String getAverage_rate() {
        return average_rate;
    }

    public void setAverage_rate(String average_rate) {
        this.average_rate = average_rate;
    }

    public String getMax_rate() {
        return max_rate;
    }

    public void setMax_rate(String max_rate) {
        this.max_rate = max_rate;
    }

    public String getMin_rate() {
        return min_rate;
    }

    public void setMin_rate(String min_rate) {
        this.min_rate = min_rate;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }
}
