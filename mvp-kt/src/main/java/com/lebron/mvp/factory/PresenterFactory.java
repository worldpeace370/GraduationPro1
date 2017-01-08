package com.lebron.mvp.factory;

import com.lebron.mvp.presenter.Presenter;

/**
 * the interface to create Presenter object.
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 */

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}
