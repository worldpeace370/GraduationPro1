package com.lebron.graduationpro1.controlpage.model;

import android.os.Handler;
import android.os.Message;

import com.lebron.graduationpro1.controlpage.contracts.ControlContracts;
import com.lebron.graduationpro1.service.OkHttpUploadService;
import com.lebron.mvp.model.BaseModel;

import java.util.Map;

/**
 * Created by wuxiangkun on 2017/2/27.
 * Contact way wuxiangkun2015@163.com
 */

public class ControlModel extends BaseModel<ControlContracts.Model.CallBack>
        implements ControlContracts.Model {
    private static final String mHostTemptUrl = "http://114.215.117.169/thinkphp/Home/ControlTemp/";
    private static final String mHostRateUrl = "http://114.215.117.169/thinkphp/Home/ControlRate/";
    private OkHttpUploadService mUploadService = new OkHttpUploadService();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String infoType = (String) msg.obj;
                    handleSuccess(infoType);
                    break;
                case 1:
                    String error = (String) msg.obj;
                    handleFail(-1, error);
                    break;
            }
        }
    };

    @Override
    public void uploadControlInfo(final Map<String, String> paramsMap) {
        final String url = paramsMap.containsKey("temperature") ? mHostTemptUrl : mHostRateUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUploadService.requestPostBySynWithForm(url, paramsMap,
                        new OkHttpUploadService.UploadCompleteListener() {
                            @Override
                            public void success(String success) {
                                Message message = mHandler.obtainMessage();
                                message.what = 0;
                                message.obj = paramsMap.containsKey("temperature") ? "temperature" : "rate";
                                mHandler.sendMessage(message);
                            }

                            @Override
                            public void fail(int retCode, String error) {
                                Message message = mHandler.obtainMessage();
                                message.what = 1;
                                message.obj = error;
                                mHandler.sendMessage(message);
                            }
                        });
            }
        }).start();
    }

    private void handleSuccess(String infoType) {
        for (CallBack callBack : getCallBacks()) {
            callBack.onSuccess(infoType);
        }
    }

    private void handleFail(int retCode, String retDesc) {
        for (CallBack callBack : getCallBacks()) {
            callBack.onFail(retCode, retDesc);
        }
    }
}
