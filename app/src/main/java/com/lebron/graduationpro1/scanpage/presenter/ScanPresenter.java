package com.lebron.graduationpro1.scanpage.presenter;

import com.lebron.graduationpro1.scanpage.contracts.ScanContracts;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.scanpage.model.ScanRequestModel;
import com.lebron.mvp.presenter.Presenter;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/14.
 * Contact way wuxiangkun2015@163.com
 */

public class ScanPresenter extends Presenter<ScanContracts.View> implements
        ScanContracts.Presenter, ScanContracts.Model.Callback{
    private ScanRequestModel mRequestModel;

    public ScanPresenter() {
        mRequestModel = new ScanRequestModel();
        mRequestModel.addCallBack(this);
    }

    @Override
    public void getCollectInfo(String date) {
        if (getView() != null) {
            getView().showLoading();
            mRequestModel.getCollectInfo(date);
        }
    }

    @Override
    public void onSuccess(List<CollectInfoBean> infoList) {

    }

    @Override
    public void onFail(int retCode, String retDesc) {

    }
}
