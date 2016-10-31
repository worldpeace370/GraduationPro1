package com.lebron.graduationpro1.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**用于获取app的版本信息,装逼用的,版本信息放在了build.gradle中
 * Created by lebron on 16-10-30.
 * Contact by wuxiangkun2015@163.com
 */

public class AppInfoUtils {
    /**
     * @param activity 传入当前Activity
     * @return 返回版本信息
     */
    public static String getVersionName(Activity activity){
        //获取packageManager的实例
        PackageManager packageManager = activity.getPackageManager();
        //getPackageName()是你当前类的包名,0 代表是获取版本信息
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(activity.getPackageName()
                    , 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }
}
