package com.lebron.graduationpro1.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lebron.graduationpro1.R;
import com.lebron.mvp.presenter.Presenter;
import com.lebron.mvp.view.LebronMvpFragment;

/**
 * Created by wuxiangkun on 2017/1/8.
 * Contact way wuxiangkun2015@163.com
 */

public abstract class BaseFragment<P extends Presenter> extends LebronMvpFragment<P> {
    public Toolbar mToolbar;
    private LinearLayout mLayoutLoading;
    private LinearLayout mLayoutEmpty;
    private LinearLayout mLayoutError;

    /**
     * bind views
     */
    protected abstract void bindViews(View view);

    /**
     * set listeners
     */
    protected abstract void setListener();

    /**
     * init data
     */
    protected abstract void init();

    /**
     * 初始化当前状态不是正常的时候的UI,比如加载中,空内容,网络错误
     * 子类实现{@link BaseFragment#bindViews(View)}时需要调用该方法
     *
     * @param rootView 根View
     */
    protected void initNoStandardUI(@NonNull View rootView) {
        if (!(getActivity() instanceof BaseActivity)) {
            return;
        }
        mLayoutLoading = ((LinearLayout) rootView.findViewById(R.id.layout_common_loading));
        mLayoutEmpty = ((LinearLayout) rootView.findViewById(R.id.layout_common_empty));
        mLayoutError = ((LinearLayout) rootView.findViewById(R.id.layout_common_network_error));
    }

    /**
     * 显示加载
     */
    protected void showCommonLoadingLayout() {
        mLayoutLoading.setVisibility(View.VISIBLE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    /**
     * 显示空内容
     */
    protected void showCommonEmptyLayout() {
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    /**
     * 显示网络错误
     */
    protected void showCommonNetErrorLayout() {
        mLayoutError.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
    }

    /**
     * 初始化Toolbar
     *
     * @param rootView 根View
     */
    public void initToolbar(@NonNull View rootView) {
        initToolbar(rootView, R.string.app_name);
    }

    /**
     * 初始化Toolbar
     *
     * @param rootView   根View
     * @param titleResId title的string.xml资源id
     */
    public void initToolbar(@NonNull View rootView, @StringRes int titleResId) {
        initToolbar(rootView, getResources().getString(titleResId));
    }

    /**
     * 初始化Toolbar
     *
     * @param rootView 根View
     * @param title    title的String
     */
    public void initToolbar(@NonNull View rootView, String title) {
        initToolbar(rootView, title, R.id.toolbar);
    }

    /**
     * 初始化Toolbar
     *
     * @param rootView  {@link android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param title     mToolbar title
     * @param toolbarId layout中的id
     */
    public void initToolbar(@NonNull View rootView, String title, @IdRes int toolbarId) {
        if (!(getActivity() instanceof BaseActivity)) {
            return;
        }
        mToolbar = (Toolbar) rootView.findViewById(toolbarId);
        mToolbar.setTitle(title);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 获取Toolbar
     *
     * @return mToolbar
     */
    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * 设置Toolbar的颜色（color）
     *
     * @param color 颜色值(转为整形)
     */
    public void setToolbarColor(@ColorInt int color) {
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(color);
        }
    }

    /**
     * 设置Toolbar的颜色（colorResid）
     *
     * @param resId The identifier of the resource
     */
    public void setToolbarResource(@DrawableRes int resId) {
        if (mToolbar != null) {
            mToolbar.setBackgroundResource(resId);
        }
    }

    public void showCustomToast(@DrawableRes int iconRes, @StringRes int msgRes) {
        if (getActivity() != null) {
            showCustomToast(iconRes, getActivity().getResources().getString(msgRes));
        }

    }

    public void showCustomToast(@DrawableRes int iconRes, String msg) {
        if (getActivity() != null) {
            showCustomToast(iconRes, msg, Toast.LENGTH_SHORT);
        }
    }

    public void showCustomToast(@DrawableRes int iconRes, @StringRes int msgRes, int duration) {
        if (getActivity() != null) {
            showCustomToast(iconRes, getActivity().getResources().getString(msgRes),
                    duration);
        }
    }

    /**
     * show custom view Toast with iconRes,msg,duration
     *
     * @param iconRes  DrawableRes
     * @param msg      String
     * @param duration 0 or 1
     */
    public void showCustomToast(@DrawableRes int iconRes, String msg, int duration) {
        if (getActivity() != null) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.layout_common_toast,
                    (ViewGroup) getActivity().findViewById(R.id.layout_toast));
            ImageView image = (ImageView) layout.findViewById(R.id.iv_icon);
            image.setImageResource(iconRes);
            TextView text = (TextView) layout.findViewById(R.id.tv_msg);
            text.setText(msg);
            Toast toast = new Toast(getActivity().getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(layout);
            toast.show();
        }
    }
}
