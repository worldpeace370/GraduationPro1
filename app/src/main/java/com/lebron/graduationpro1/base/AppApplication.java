package com.lebron.graduationpro1.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;

/** 该App的唯一的上下文,继承自Application,别的地方引用
 * Created by lebron on 16-10-28.
 * Contact by wuxiangkun2015@163.com
 */

public class AppApplication extends Application{
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext(){
        return mContext;
    }

    /**
     * 是否手机的系统版本>=19 (android 4.4)
     * @return true or false
     */
    public static boolean isMoreThanKitKat(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
    /**
     * 是否手机的系统版本>=21 (android 5.0)
     * @return true or false
     */
    public static boolean isMoreThanLollipop(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}