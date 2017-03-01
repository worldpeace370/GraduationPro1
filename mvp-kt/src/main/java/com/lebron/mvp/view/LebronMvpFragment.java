package com.lebron.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lebron.mvp.factory.PresenterFactory;
import com.lebron.mvp.factory.ReflectionPresenterFactory;
import com.lebron.mvp.presenter.Presenter;

/**
 * This view is example of how a view should control it's presenter.
 * You can inherit from this class or copy/paste this class's code
 * to your own view' implementation.Abstract class implements interface
 * class can not implements it's method.(抽象类实现接口的时候可以不去实现接口的方法)
 * <p>
 * Created by wuxiangkun on 2017/1/2.
 * Contact way wuxiangkun2015@163.com
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 */

public abstract class LebronMvpFragment<P extends Presenter> extends Fragment implements ViewWithPresenter<P> {
    private static final String PRESENTER_STATE_KEY = "presenter_state";
    // 通过getClass()得到了当前Fragment,然后在ReflectionPresenterFactory中得到了当前Fragment对应的Presenter
    // ReflectionPresenterFactory对象作为PresenterLifecycleDelegate的参数，代理自然也得到了Presenter
    private PresenterLifecycleDelegate<P> mPresenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass())); // getClass()为运行时实际的Fragment实例class

    /**
     * Returns a current presenter factory.
     */
    @Override
    public PresenterFactory<P> getPresenterFactory() {
        return mPresenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        mPresenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    @Override
    public P getPresenter() {
        return mPresenterDelegate.getPresenter();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenterDelegate.onTakeView(this); // 将Presenter控制的实际View(Fragment or Activity)传入, this表示当前的子Fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenterDelegate.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mPresenterDelegate.onVisible();
        } else {
            mPresenterDelegate.onInVisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() != null) {
            mPresenterDelegate.onDestroy(!getActivity().isChangingConfigurations());
        } else {
            mPresenterDelegate.onDestroy(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, mPresenterDelegate.onSaveInstanceState());
    }
}
