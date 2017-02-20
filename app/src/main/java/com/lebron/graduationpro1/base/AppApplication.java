package com.lebron.graduationpro1.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 该App的唯一的上下文,继承自Application,别的地方引用
 * Created by lebron on 16-10-28.
 * Contact by wuxiangkun2015@163.com
 */

public class AppApplication extends Application {
    private static Context mContext;
    private static RequestQueue mRequestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        BasePreference.getInstance().setContext(mContext);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    /**
     * 是否手机的系统版本>=19 (android 4.4)
     *
     * @return true or false
     */
    public static boolean isMoreThanKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 是否手机的系统版本>=21 (android 5.0)
     *
     * @return true or false
     */
    public static boolean isMoreThanLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}