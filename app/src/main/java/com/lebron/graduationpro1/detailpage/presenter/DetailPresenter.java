package com.lebron.graduationpro1.detailpage.presenter;

import com.lebron.graduationpro1.detailpage.contracts.DetailContracts;
import com.lebron.graduationpro1.detailpage.model.DetailRequestModel;
import com.lebron.graduationpro1.detailpage.model.HeatInfo;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.mvp.presenter.Presenter;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/8.
 * Contact way wuxiangkun2015@163.com
 */

public class DetailPresenter extends Presenter<DetailContracts.View>
        implements DetailContracts.Presenter, DetailContracts.Model.CallBack {
    private DetailRequestModel mRequestModel;

    public DetailPresenter() {
        mRequestModel = new DetailRequestModel();
        mRequestModel.addCallBack(this);
    }

    @Override
    public void getHeatInfo(int page) {
        if (page < 1) {
            throw new RuntimeException("数据库页数不能小于1...");
        }
        mRequestModel.getHeatInfo(page);
    }

    @Override
    public void onSuccess(List<HeatInfo> infoList) {
        if (getView() != null) {
            AppLog.i("hello", infoList.size() + " ");
        }
    }

    @Override
    public void onFail(int retCode, String retDesc) {

    }
}
