package com.lebron.graduationpro1.service;

import android.util.Log;

import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wuxiangkun on 2017/2/28.
 * Contact way wuxiangkun2015@163.com
 */

public class OkHttpUploadService {
    private OkHttpClient mClient = new OkHttpClient();

    /**
     * * okHttp post同步请求表单提交
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param listener  成功or失败接口回调
     * @param t         泛型参数
     * @param <T>       泛型参数, 请求后的返回类型
     */
    public <T> void requestPostBySynWithForm(String actionUrl, Map<String, String> paramsMap,
                                             UploadCompleteListener<T> listener, T t) {
        try {
            //创建一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                //追加表单信息
                builder.add(key, paramsMap.get(key));
            }
            //生成表单实体对象
            RequestBody formBody = builder.build();
            //创建一个请求
            final Request request = new Request.Builder().url(actionUrl).post(formBody).build();
            //创建一个Call
            final Call call = mClient.newCall(request);
            //执行请求
            Response response = call.execute();
            if (response.isSuccessful()) {
                listener.success(t);
            } else {
                listener.error(-1, "fail");
            }
        } catch (Exception e) {
            Log.i("OkHttpUploadService", e.toString());
        }
    }

    public interface UploadCompleteListener<T> {
        void success(T t);

        void error(int retCode, String error);
    }
}
