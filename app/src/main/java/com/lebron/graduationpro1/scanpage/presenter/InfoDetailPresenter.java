package com.lebron.graduationpro1.scanpage.presenter;

import com.lebron.graduationpro1.scanpage.contracts.InfoDetailContracts;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.scanpage.model.InfoDetailRequestModel;
import com.lebron.mvp.presenter.Presenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.System.currentTimeMillis;

public class InfoDetailPresenter extends Presenter<InfoDetailContracts.View>
        implements InfoDetailContracts.Presenter, InfoDetailContracts.Model.Callback {
    private InfoDetailRequestModel mRequestModel;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final String mStrDate;

    public InfoDetailPresenter() {
        mRequestModel = new InfoDetailRequestModel();
        mRequestModel.addCallBack(this);
        Date date = new Date(currentTimeMillis());
        mStrDate = SIMPLE_DATE_FORMAT.format(date);
    }

    @Override
    public void getCollectInfo(String date) {
        if (getView() != null) {
            mRequestModel.getCollectInfo(date);//请求数据,然后回调到onSuccess中,在这里面更新UI
        }
    }

    @Override
    public void initTodayData() {
        if (getView() != null) {
            getCollectInfo(mStrDate);
        }
    }

    @Override
    public void refreshTodayData() {
        if (getView() != null) {
            getView().showRefreshing();
            getCollectInfo(mStrDate);
        }
    }

    @Override
    public void onSuccess(List<CollectInfoBean> infoList) {
        if (getView() != null) {
            if (infoList.isEmpty()) {
                getView().showEmpty();
            } else {
                getView().showContent(revertList(infoList));
            }
        }
    }

    @Override
    public void onFail(int retCode, String retDesc) {
        if (getView() != null) {
            getView().showError();
        }
    }

    private List<CollectInfoBean> revertList(List<CollectInfoBean> infoList) {
        List<CollectInfoBean> tempList = new ArrayList<>();
        for (int i = infoList.size() - 1; i >= 0; i--) {
            tempList.add(infoList.get(i));
        }
        return tempList;
    }
}
