package com.lebron.graduationpro1.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lebron.graduationpro1.R;
import com.lebron.mvp.presenter.Presenter;
import com.lebron.mvp.view.LebronMvpActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseActivity<P extends Presenter> extends LebronMvpActivity<P> {
    protected Toolbar mToolbar;
    protected SystemBarTintManager mSystemBarTintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 此段代码获取SystemBarTintManager对象之前执行,否则SystemBarTintManager中的mStatusBarAvailable
         * 一直为false,导致setStatusBarTintResource(int resid)无效,(遇到了坑)
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
        }
        try {
            mSystemBarTintManager = new SystemBarTintManager(this);
        } catch (NoSuchMethodError e) {
            if (e.toString().equalsIgnoreCase("xposed")) {
                showLongToast("不支持xposed框架！");
                finish();
            } else {
                throw e;
            }
        }
    }

    /**
     * bind views
     */
    protected abstract void bindViews();

    /**
     * set listeners
     */
    protected abstract void setListener();

    /**
     * init data
     */
    protected abstract void init();

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        initToolbar(getResources().getString(R.string.app_name));
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar(@StringRes int titleResId) {
        initToolbar(getResources().getString(titleResId));
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar(String title) {
        initToolbar(title, R.id.toolbar);
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar(String title, int toolbarId) {
        mToolbar = (Toolbar) findViewById(toolbarId);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
                finish();
            }
        });
        mToolbar.setNavigationIcon(R.mipmap.icon_toolbar_navigation);
        mToolbar.setTitle(title);
        setStatusStyleResource(R.color.colorStatusBar);
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

    /**
     * 短暂显示Toast提示(来自res)
     **/
    public void showShortToast(@StringRes int resId) {
        Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    protected void showLongToast(@StringRes int resId) {
        Toast.makeText(this, getResources().getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    public void showCustomToast(@DrawableRes int iconRes, @StringRes int msgRes) {
        showCustomToast(iconRes, this.getResources().getString(msgRes));
    }

    public void showCustomToast(@DrawableRes int iconRes, String msg) {
        showCustomToast(iconRes, msg, Toast.LENGTH_SHORT);
    }

    public void showCustomToast(@DrawableRes int iconRes, @StringRes int msgRes, int duration) {
        showCustomToast(iconRes, this.getResources().getString(msgRes),
                duration);
    }

    /**
     * show custom view Toast with iconRes,msg,duration
     *
     * @param iconRes  DrawableRes
     * @param msg      String
     * @param duration 0 or 1
     */
    public void showCustomToast(@DrawableRes int iconRes, String msg, int duration) {
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_common_toast,
                (ViewGroup) this.findViewById(R.id.layout_toast));
        ImageView image = (ImageView) layout.findViewById(R.id.iv_icon);
        image.setImageResource(iconRes);
        TextView text = (TextView) layout.findViewById(R.id.tv_msg);
        text.setText(msg);
        Toast toast = new Toast(this.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    /**
     * 设置状态栏的颜色（color）
     *
     * @param color @ColorInt
     */
    public void setStatusbarColor(@ColorInt int color) {
        setStatusStyleColor(color);
    }

    /**
     * 设置状态栏的颜色
     *
     * @param resid @ColorRes or @DrawableRes
     */
    public void setStatusbarResource(int resid) {
        setStatusStyleResource(resid);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    protected void setStatusStyleColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mSystemBarTintManager != null) {
                mSystemBarTintManager.setStatusBarTintEnabled(true);
                mSystemBarTintManager.setStatusBarTintColor(color);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    protected void setStatusStyleResource(int resId) { // @ColorRes or @DrawableRes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, resId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mSystemBarTintManager != null) {
                mSystemBarTintManager.setStatusBarTintEnabled(true);
                mSystemBarTintManager.setStatusBarTintResource(resId);
            }
        }
    }

    protected int getInsertTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && mSystemBarTintManager != null) {
            SystemBarTintManager.SystemBarConfig config = mSystemBarTintManager.getConfig();
            return config.getPixelInsetTop(false);
        }
        return 0;
    }

    protected int getStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && mSystemBarTintManager != null) {
            SystemBarTintManager.SystemBarConfig config = mSystemBarTintManager.getConfig();
            return config.getStatusBarHeight();
        }
        return 0;
    }

    /**
     * 子fragment获取activity的SystemBarTintManager.在fragment中使用
     *
     * @return mSystemBarTintManager
     */
    public SystemBarTintManager getSystemBarTintManager() {
        return mSystemBarTintManager;
    }

    protected int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && mSystemBarTintManager != null) {
            SystemBarTintManager.SystemBarConfig config = mSystemBarTintManager.getConfig();
            return config.getNavigationBarHeight();
        }
        return 0;
    }

    @Override
    public void onBackPressed() {
        onBack();
        super.onBackPressed();
    }

    protected void onBack() {
    }

    /**
     * 更改窗口透明度
     *
     * @param alpha 透明度值:0.0~1.0
     */
    public void changeWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = alpha;
        if (alpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(params);
    }

}
