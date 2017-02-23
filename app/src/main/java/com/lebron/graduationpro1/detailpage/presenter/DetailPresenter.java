package com.lebron.graduationpro1.detailpage.presenter;

import com.lebron.graduationpro1.detailpage.contracts.DetailContracts;
import com.lebron.graduationpro1.detailpage.model.DetailRequestModel;
import com.lebron.graduationpro1.detailpage.model.HeatInfo;
import com.lebron.mvp.presenter.Presenter;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/8.
 * Contact way wuxiangkun2015@163.com
 */

public class DetailPresenter extends Presenter<DetailContracts.View>
        implements DetailContracts.Presenter, DetailContracts.Model.CallBack {
    private DetailRequestModel mRequestModel;
    private boolean mShowLoadingOnce = true;
    public DetailPresenter() {
        mRequestModel = new DetailRequestModel();
        mRequestModel.addCallBack(this);
    }

    @Override
    public void getHeatInfo(int page) {
        if (page < 1) {
            throw new RuntimeException("数据库页数不能小于1...");
        }
        if (getView() != null) {
            if (mShowLoadingOnce) {
                getView().showLoading(); // 只有刚进入界面的时候显示该界面
                mShowLoadingOnce = false;
            }
            mRequestModel.getHeatInfo(page); //请求数据,然后回调到onSuccess中,在这里面更新UI
        }
    }

    @Override
    public void onSuccess(List<HeatInfo> infoList) {
        if (getView() != null) {
            if (infoList.isEmpty()) {
                getView().showEmpty();
            } else {
                getView().showContent(infoList);
            }
        }
    }

    @Override
    public void onFail(int retCode, String retDesc) {
        if (getView() != null) {
            getView().showError();
        }
    }
}
