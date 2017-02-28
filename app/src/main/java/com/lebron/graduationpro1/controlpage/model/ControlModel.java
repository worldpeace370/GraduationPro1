package com.lebron.graduationpro1.controlpage.model;

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
    private static final String mHostInsertUrl = "http://114.215.117.169/thinkphp/Home/InsertGrad/";

    @Override
    public void uploadControlInfo(Map<String, String> paramsMap) {
        new OkHttpUploadService().requestPostBySynWithForm(mHostInsertUrl, paramsMap,
                new OkHttpUploadService.UploadCompleteListener<String>() {
                    @Override
                    public void success(String s) {
                        handleSuccess(s);
                    }

                    @Override
                    public void error(int retCode, String error) {
                        handleFail(retCode, error);
                    }
                }, "success");
    }

    private void handleSuccess(String retDesc) {
        for (CallBack callBack : getCallBacks()) {
            callBack.onSuccess(retDesc);
        }
    }

    private void handleFail(int retCode, String retDesc) {
        for (CallBack callBack : getCallBacks()) {
            callBack.onFail(retCode, retDesc);
        }
    }
}
