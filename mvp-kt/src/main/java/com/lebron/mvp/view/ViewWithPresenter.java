package com.lebron.mvp.view;

import com.lebron.mvp.factory.PresenterFactory;
import com.lebron.mvp.presenter.Presenter;

/**
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 */

public interface ViewWithPresenter<P extends Presenter> {
    /**
     * @return Return a current presenter factory.
     */
    PresenterFactory<P> getPresenterFactory();

    /**
     * Sets a presenter presenterFactory.
     * Call this method before onCreate/onFinishInflate to override default
     * {@link com.lebron.mvp.factory.ReflectionPresenterFactory} presenter presenterFactory.
     * Use this method for presenter dependency injection.
     */
    void setPresenterFactory(PresenterFactory<P> presenterFactory);

    P getPresenter();
}
