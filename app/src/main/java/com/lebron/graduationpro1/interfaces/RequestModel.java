package com.lebron.graduationpro1.interfaces;

/**从服务器请求json数据的接口,给实现类以不同的技术实现
 * 比如Volley,OKHttp等
 * Created by lebron on 16-11-11.
 * Contact by wuxiangkun2015@163.com
 */

public interface RequestModel {
    void getJsonFromServer(String urlString, RequestFinishedListener listener);
}
