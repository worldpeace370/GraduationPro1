package com.lebron.graduationpro1.interfaces;

/**json数据下载成功或者失败的接口回调,在Activity or Fragment中实现
 * Created by lebron on 16-11-11.
 * Contact by wuxiangkun2015@163.com
 */

public interface RequestFinishedListener {
    void onRequestSucceed(String responseString);
    void onRequestError(String errorString);
}
