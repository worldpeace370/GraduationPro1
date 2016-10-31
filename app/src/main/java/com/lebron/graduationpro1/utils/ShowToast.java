package com.lebron.graduationpro1.utils;

import android.widget.Toast;

import com.lebron.graduationpro1.base.AppApplication;

/**封装的Toast类,简化操作
 * Created by lebron on 16-10-28.
 * Contact by wuxiangkun2015@163.com
 */

public class ShowToast {
    /**
     * 短时间显示吐司
     * @param sequence 要显示的字符串
     */
    public static void shortTime(CharSequence sequence){
        Toast.makeText(AppApplication.getAppContext(), sequence, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示吐司
     * @param sequence 要显示的字符串
     */
    public static void longTime(CharSequence sequence){
        Toast.makeText(AppApplication.getAppContext(), sequence, Toast.LENGTH_LONG).show();
    }
}
