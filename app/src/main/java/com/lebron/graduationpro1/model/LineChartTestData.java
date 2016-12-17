package com.lebron.graduationpro1.model;

/**
 * Created by lebron on 16-11-12.
 * Contact by wuxiangkun2015@163.com
 */

public class LineChartTestData {
    private int xValue;
    private int yValue;

    public LineChartTestData(int xValue, int yValue){
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public int getxValue() {
        return xValue;
    }

    public void setxValue(int xValue) {
        this.xValue = xValue;
    }

    public int getyValue() {
        return yValue;
    }

    public void setyValue(int yValue) {
        this.yValue = yValue;
    }
}
