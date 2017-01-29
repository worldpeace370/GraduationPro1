package com.lebron.graduationpro1.scanpage.presenter;

import com.lebron.graduationpro1.scanpage.contracts.ScanContracts;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.scanpage.model.ScanRequestModel;
import com.lebron.graduationpro1.view.customcalendarview.CalendarTools;
import com.lebron.mvp.presenter.Presenter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.lebron.graduationpro1.scanpage.view.ScanFragment.POSITION_FIRST_DAY;
import static com.lebron.graduationpro1.scanpage.view.ScanFragment.POSITION_SECOND_DAY;
import static com.lebron.graduationpro1.scanpage.view.ScanFragment.POSITION_THIRD_DAY;
import static java.lang.System.currentTimeMillis;

/**
 * Created by wuxiangkun on 2017/1/14.
 * Contact way wuxiangkun2015@163.com
 */

public class ScanPresenter extends Presenter<ScanContracts.View> implements
        ScanContracts.Presenter, ScanContracts.Model.Callback {
    private ScanRequestModel mRequestModel;
    private int mTodayYear, mTodayMonth, mTodayDay; //今天的年月日
    private int mSelectYear, mSelectMonth, mSelectDay; //当前通过日历选中的年月日
    private int mThirdYear, mThirdMonth, mThirdDay; //顶部的第三天的年月日

    public ScanPresenter() {
        mRequestModel = new ScanRequestModel();
        mRequestModel.addCallBack(this);
    }

    @Override
    public void getCollectInfo(String date) {
        if (getView() != null) {
            //            getView().showLoading();
            mRequestModel.getCollectInfo(date);//请求数据,然后回调到onSuccess中,在这里面更新UI
        }
    }

    @Override
    public void initTodayData() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(currentTimeMillis());
        calendar.clear();
        calendar.setTime(date);

        mTodayYear = calendar.get(Calendar.YEAR);
        mTodayMonth = calendar.get(Calendar.MONTH);
        mTodayDay = calendar.get(Calendar.DAY_OF_MONTH);

        mSelectYear = mTodayYear;
        mSelectMonth = mTodayMonth;
        mSelectDay = mTodayDay;

        mThirdYear = mTodayYear;
        mThirdMonth = mTodayMonth;
        mThirdDay = mTodayDay;

        if (getView() != null) {
            getView().changeTopDateColor(POSITION_THIRD_DAY);
            getView().setTopDateLayout(mTodayYear, mTodayMonth, mTodayDay);
            getCollectInfo(CalendarTools.dateToString(mTodayYear, mTodayMonth, mTodayDay));
        }
    }

    @Override
    public void changeToFirstPositionDay() {
        Calendar calendar = CalendarTools.createCalendar(mThirdYear, mThirdMonth, mThirdDay);
        calendar.add(Calendar.DATE, -2);
        mSelectYear = calendar.get(Calendar.YEAR);
        mSelectMonth = calendar.get(Calendar.MONTH);
        mSelectDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (getView() != null) {
            getView().changeTopDateColor(POSITION_FIRST_DAY);
            getCollectInfo(CalendarTools.dateToString(mSelectYear, mSelectMonth, mSelectDay));
        }
    }

    @Override
    public void changeToSecondPositionDay() {
        Calendar calendar = CalendarTools.createCalendar(mThirdYear, mThirdMonth, mThirdDay);
        calendar.add(Calendar.DATE, -1);
        mSelectYear = calendar.get(Calendar.YEAR);
        mSelectMonth = calendar.get(Calendar.MONTH);
        mSelectDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (getView() != null) {
            getView().changeTopDateColor(POSITION_SECOND_DAY);
            getCollectInfo(CalendarTools.dateToString(mSelectYear, mSelectMonth, mSelectDay));
        }
    }

    @Override
    public void changeToThirdPositionDay() {
        mSelectYear = mThirdYear;
        mSelectMonth = mThirdMonth;
        mSelectDay = mThirdDay;
        if (getView() != null) {
            getView().changeTopDateColor(POSITION_THIRD_DAY);
            getCollectInfo(CalendarTools.dateToString(mSelectYear, mSelectMonth, mSelectDay));
        }
    }

    @Override
    public void changeToSelectDay(Calendar calendar) {
        mSelectYear = calendar.get(Calendar.YEAR);
        mSelectMonth = calendar.get(Calendar.MONTH);
        mSelectDay = calendar.get(Calendar.DAY_OF_MONTH);
        mThirdYear = mSelectYear;
        mThirdMonth = mSelectMonth;
        mThirdYear = mSelectDay;
        if (getView() != null) {
            getView().changeTopDateColor(POSITION_THIRD_DAY);
            getView().setTopDateLayout(mThirdYear, mThirdMonth, mThirdYear);
            getCollectInfo(CalendarTools.dateToString(mSelectYear, mSelectMonth, mSelectDay));
        }
    }

    @Override
    public void setBackTodayVisibility(Calendar calendar) {
        if (getView() != null) {
            if (calendar.get(Calendar.YEAR) != mTodayYear || calendar.get(Calendar.MONTH) != mTodayMonth) {
                getView().setBackTodayVisibility(true);
            } else {
                getView().setBackTodayVisibility(false);
            }
        }
    }

    @Override
    public void backToToday() {
        if (getView() != null) {
            getView().backToToday(mTodayYear, mTodayMonth, mTodayDay);
        }
    }

    @Override
    public void showSelectDateWindow() {
        if (getView() != null) {
            getView().showSelectDateWindow(mSelectYear, mSelectMonth, mSelectDay);
            if (mSelectYear != mTodayYear || mSelectMonth != mTodayMonth) {
                getView().setBackTodayVisibility(true);
            } else {
                getView().setBackTodayVisibility(false);
            }
        }
    }

    @Override
    public void onSuccess(List<CollectInfoBean> infoList) {
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

    }
}
