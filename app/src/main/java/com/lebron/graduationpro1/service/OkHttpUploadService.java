package com.lebron.graduationpro1.service;

import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
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
     * * okHttp post同步请求表单提交,需要在子线程中去调用该方法
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     * @param listener  成功or失败接口回调
     */
    public void requestPostBySynWithForm(String actionUrl, Map<String, String> paramsMap,
                                         final UploadCompleteListener listener) {
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
            //异步的方式去执行请求,将call加入调度队列，然后等待任务执行完成，我们在Callback中即可得到结果
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    listener.fail(-1, e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    listener.success(response.message());
                }
            });
        } catch (Exception e) {
            Log.i("OkHttpUploadService", e.toString());
        }
    }

    public interface UploadCompleteListener {
        void success(String success);

        void fail(int retCode, String error);
    }
}
