package com.lebron.graduationpro1.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wuxiangkun on 2017/2/7.
 * Contact way wuxiangkun2015@163.com
 */

public class Tools {
    //dp 转 px
    public static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    //px 转 dp
    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 根据传入的Long型数据获得格式化的时间
     * @param time
     * @return
     */
    public static String getModifiedTimeTextNew(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm:ss");
        return format.format(new Date(time));
    }

    /**
     * 判断字符串是否为空
     * @param string
     * @return
     */
    public static boolean isEmpty(CharSequence string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }
}
