package com.lebron.graduationpro1.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.lebron.graduationpro1.base.AppApplication;

/**Volley的网络请求manager,可以为请求增加标签,根据标签取消请求
 * Created by lebron on 16-11-1.
 * Contact by wuxiangkun2015@163.com
 */

public class RequestManager {

    private static RequestQueue sRequestQueue = Volley.newRequestQueue(AppApplication.getAppContext());
    private RequestManager(){

    }

    /**
     * 将请求加入请求队列
     * @param request 待发起的请求
     * @param tag 给请求加的标签,用于取消请求
     */
    public static void addRequestQueue(Request<?> request, Object tag){
        if (tag != null){
            request.setTag(tag);
        }
        sRequestQueue.add(request);
    }

    /**
     * 根据tag取消请求
     * @param tag 请求的标签
     */
    public static void cancelRequestByTag(Object tag){
        sRequestQueue.cancelAll(tag);
    }
}
