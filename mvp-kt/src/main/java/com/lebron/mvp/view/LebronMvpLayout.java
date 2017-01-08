package com.lebron.mvp.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lebron.mvp.factory.PresenterFactory;
import com.lebron.mvp.factory.ReflectionPresenterFactory;
import com.lebron.mvp.presenter.Presenter;

/**
 * This view is example of how a view should control it's presenter.
 * You can inherit from this class or copy/paste this class's code
 * to your own view' implementation.
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 *            Created by wuxiangkun on 2017/1/8.
 *            Contact way wuxiangkun2015@163.com
 */

public class LebronMvpLayout<P extends Presenter> extends FrameLayout implements ViewWithPresenter<P> {
    private static final String PARENT_STATE_KEY = "parent_state";
    private static final String PRESENTER_STATE_KEY = "presenter_state";

    public LebronMvpLayout(Context context) {
        super(context);
    }

    public LebronMvpLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LebronMvpLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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

    /**
     * Returns the unwrapped activity of the view or throws an exception.
     *
     * @return an unwrapped activity
     */
    public Activity getActivity() {
        Context context = getContext(); // View 的方法
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        if (!(context instanceof Activity))
            return null;
        return (Activity) context;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBundle(PRESENTER_STATE_KEY, mPresenterDelegate.onSaveInstanceState());
        bundle.putParcelable(PARENT_STATE_KEY, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        super.onRestoreInstanceState(bundle.getParcelable(PARENT_STATE_KEY));
        mPresenterDelegate.onRestoreInstanceState(bundle.getBundle(PRESENTER_STATE_KEY));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInEditMode()) {
            mPresenterDelegate.onTakeView(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (getActivity() != null) {
            mPresenterDelegate.onDestroy(!getActivity().isChangingConfigurations());
        } else {
            mPresenterDelegate.onDestroy(true);
        }
    }
}
