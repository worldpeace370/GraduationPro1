package com.lebron.graduationpro1.service;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lebron.graduationpro1.base.AppApplication;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/2/20.
 * Contact way wuxiangkun2015@163.com
 */

public class VolleyService {
    public <T> void getDataFromServer(String urlStr, final Class<T> type
            , final RequestCompleteListener<T> listener) {
        StringRequest request = new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("")) {
                    List<T> list = JSONArray.parseArray(response, type);
                    listener.success(list);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error(error.getMessage());
            }
        });
        AppApplication.getRequestQueue().add(request);
    }

    public interface RequestCompleteListener<T> {
        void success(List<T> list);

        void error(String error);
    }
}
