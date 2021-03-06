package com.lebron.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.lebron.mvp.factory.PresenterFactory;
import com.lebron.mvp.factory.ReflectionPresenterFactory;
import com.lebron.mvp.presenter.Presenter;

/**
 * This view is example of how a view should control it's presenter.
 * You can inherit from this class or copy/paste this class's code
 * to your own view' implementation.Abstract class implements interface
 * class can not implements it's method.(抽象类实现接口的时候可以不去实现接口的方法)
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 *            Created by wuxiangkun on 2017/1/2.
 *            Contact way wuxiangkun2015@163.com
 */

public abstract class LebronMvpActivity<P extends Presenter> extends AppCompatActivity implements ViewWithPresenter<P> {
    private static final String PRESENTER_STATE_KEY = "presenter_state";
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPresenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));
        }
        mPresenterDelegate.onTakeView(this); // 将Presenter控制的实际View(Fragment or Activity)传入,在Fragment中的onCreateView中调用
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenterDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenterDelegate.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenterDelegate.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenterDelegate.onDestroy(!isChangingConfigurations());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, mPresenterDelegate.onSaveInstanceState());
    }
}
