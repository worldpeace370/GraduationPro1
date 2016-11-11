package com.lebron.graduationpro1.net;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lebron.graduationpro1.interfaces.RequestFinishedListener;
import com.lebron.graduationpro1.interfaces.RequestModel;

/**根据MVC原则,封装的Volley类实现了请求模型
 * Created by lebron on 16-11-11.
 * Contact by wuxiangkun2015@163.com
 */

public class VolleyRequest implements RequestModel {
    @Override
    public void getJsonFromServer(String urlString, final RequestFinishedListener listener) {
        StringRequest request = new StringRequest(urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onRequestSucceed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestError(error.toString());
            }
        });
        RequestManager.addRequestQueue(request, "");
    }
}
