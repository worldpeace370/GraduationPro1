package com.lebron.graduationpro1.controlpage.presenter;

import com.lebron.graduationpro1.controlpage.contracts.ControlContracts;
import com.lebron.graduationpro1.controlpage.model.ControlModel;
import com.lebron.mvp.presenter.Presenter;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要在服务器中再建立接口，新的数据库表
 * Created by wuxiangkun on 2017/2/8.
 * Contact way wuxiangkun2015@163.com
 */

public class ControlPresenter extends Presenter<ControlContracts.View>
        implements ControlContracts.Presenter, ControlContracts.Model.CallBack {
    private ControlModel mControlModel;

    public ControlPresenter() {
        mControlModel = new ControlModel();
        mControlModel.addCallBack(this);
    }

    @Override
    public void uploadTempInfo(String tempStr) {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("mTemperature", tempStr);
        mControlModel.uploadControlInfo(tempMap);
    }

    @Override
    public void uploadRateInfo(String rateStr) {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("mRate", rateStr);
        mControlModel.uploadControlInfo(tempMap);
    }

    @Override
    public void onSuccess(String retDesc) {
        if (getView() != null) {
            getView().showUploadResult(retDesc);
        }
    }

    @Override
    public void onFail(int retCode, String retDesc) {
        if (getView() != null) {
            getView().showUploadResult(retDesc);
        }
    }
}
