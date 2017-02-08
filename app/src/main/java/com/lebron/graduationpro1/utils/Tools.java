package com.lebron.graduationpro1.utils;

import android.content.Context;

/**
 * Created by wuxiangkun on 2017/2/7.
 * Contact way wuxiangkun2015@163.com
 */

public class Tools {
    //dp 转 px
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
    //px 转 dp
    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
