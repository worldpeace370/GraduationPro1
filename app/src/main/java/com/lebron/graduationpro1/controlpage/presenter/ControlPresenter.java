package com.lebron.graduationpro1.controlpage.presenter;

import android.support.annotation.NonNull;

import com.lebron.graduationpro1.controlpage.contracts.ControlContracts;
import com.lebron.graduationpro1.controlpage.model.ControlModel;
import com.lebron.mvp.presenter.Presenter;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // HH 24小时制

    public ControlPresenter() {
        mControlModel = new ControlModel();
        mControlModel.addCallBack(this);
    }

    @Override
    public void uploadControlInfo(@NonNull String infoType, @NonNull String infoValue) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(infoType, infoValue);
        Date date = new Date(System.currentTimeMillis());
        paramsMap.put("createtime", mDateFormat.format(date));
        mControlModel.uploadControlInfo(paramsMap);
    }

    @Override
    public void onSuccess(String infoType) {
        if (getView() != null) {
            getView().showUploadSuccess(infoType);
        }
    }

    @Override
    public void onFail(int retCode, String retDesc) {
        if (getView() != null) {
            getView().showUploadFail(retCode, retDesc);
        }
    }
}
