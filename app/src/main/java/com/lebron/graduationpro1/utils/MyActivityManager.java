package com.lebron.graduationpro1.utils;

import android.app.Activity;
import android.os.Process;

import java.util.Stack;

/**该App中所有Activity的管理者
 * Created by lebron on 16-11-2.
 * Contact by wuxiangkun2015@163.com
 */

public class MyActivityManager {

    private static Stack<Activity> sActivityStack;
    private static MyActivityManager sActivityManager;
    private MyActivityManager(){

    }

    /**
     * 最普通的单例模式获取manager
     * @return 单列 manager
     */
    public static MyActivityManager getInstance(){
        if (sActivityManager == null){
            sActivityManager = new MyActivityManager();
        }
        return sActivityManager;
    }

    /**
     * 将activity加入栈中
     * @param activity 当前activity
     */
    public void addActivity(Activity activity){
        if (sActivityStack == null){
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 销毁当前activity，并且从栈中移除
     * @param activity 当前activity
     */
    public void finishActivity(Activity activity){
        if (activity != null){
            sActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁指定类型的Activity
     * @param cls 泛型传入
     */
    public void finishActivity(Class<?> cls){
        for (Activity activity: sActivityStack) {
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    /**
     * 销毁所有的Activity然后退出进程
     */
    public void finishAllActivityAndExit(){
        if (sActivityStack != null){
            for (Activity activity: sActivityStack) {
                if (activity != null && activity.isFinishing()){
                    activity.finish();
                }
            }
            sActivityStack.clear();
        }
        int id = Process.myPid();
        if (id != 0){
            Process.killProcess(id);
        }
    }
}
