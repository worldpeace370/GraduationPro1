package com.lebron.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lebron.mvp.factory.PresenterFactory;
import com.lebron.mvp.factory.PresenterStorage;
import com.lebron.mvp.presenter.Presenter;

/**
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 * This class adopts a View lifecycle to the Presenter`s lifecycle.
 *
 * @param <P> a type of the presenter.
 */

public class PresenterLifecycleDelegate<P extends Presenter> {
    private static final String PRESENTER_KEY = "presenter";
    private static final String PRESENTER_ID_KEY = "presenter_id";
    @Nullable
    private PresenterFactory<P> presenterFactory;
    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    /**
     * PresenterLifecycleDelegate构造方法在Fragment or Activity中的父类中调用
     *
     * @param presenterFactory 该对象可以返回Presenter实例,将会通过ReflectionPresenterFactory
     *                         的fromViewClass方法传入,得到presenterFactory对象
     */
    public PresenterLifecycleDelegate(@Nullable PresenterFactory<P> presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    /**
     * {@link ViewWithPresenter#getPresenterFactory()}
     * Fragment or Activity implements ViewWithPresenter interface and at the same time creating
     * PresenterLifecycleDelegate instance,so in the override method of ViewWithPresenter interface
     * can invoke PresenterLifecycleDelegate's the same method as follows.
     */
    @Nullable
    public PresenterFactory<P> getPresenterFactory() {
        return presenterFactory;
    }

    /**
     * {@link ViewWithPresenter#setPresenterFactory(PresenterFactory)}
     */
    public void setPresenterFactory(@Nullable PresenterFactory<P> presenterFactory) {
        if (presenter != null)
            throw new IllegalArgumentException("setPresenterFactory() should be called before onResume()");
        this.presenterFactory = presenterFactory;
    }

    /**
     * {@link ViewWithPresenter#getPresenter()}
     */
    public P getPresenter() {
        if (presenterFactory != null) {
            if (presenter == null && bundle != null) {
                presenter = PresenterStorage.INSTANCE.getPresenter(bundle.getString(PRESENTER_ID_KEY));
            }

            if (presenter == null) {
                /**
                 * {@link ReflectionPresenterFactory#createPresenter()}
                 */
                presenter = presenterFactory.createPresenter();
                PresenterStorage.INSTANCE.add(presenter);
                presenter.create(bundle == null ? null : bundle.getBundle(PRESENTER_KEY));
            }
            bundle = null;
        }
        return presenter;
    }

    /**
     * {@link android.app.Activity#onSaveInstanceState(Bundle)}, {@link android.app.Fragment#onSaveInstanceState(Bundle)}, {@link android.view.View#onSaveInstanceState()}.
     */
    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        getPresenter();
        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenter.save(presenterBundle);
            bundle.putBundle(PRESENTER_KEY, presenterBundle);
            bundle.putString(PRESENTER_ID_KEY, PresenterStorage.INSTANCE.getId(presenter));
        }
        return bundle;
    }
}
