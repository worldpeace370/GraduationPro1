package com.lebron.graduationpro1.utils;

import android.widget.Toast;

import com.lebron.graduationpro1.base.AppApplication;

/**封装的Toast类,简化操作.单例模式的Toast
 * 可以快速的显示吐司而不必等待之前的显示完才显示下一个
 * Created by lebron on 16-10-28.
 * Contact by wuxiangkun2015@163.com
 */

public class ShowToast {
    private static Toast sToast;
    /**
     * 短时间显示吐司
     * @param sequence 要显示的字符串
     */
    public static void shortTime(CharSequence sequence){
        if (sToast == null){
            sToast = Toast.makeText(AppApplication.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        sToast.setText(sequence);
        sToast.show();
    }

    /**
     * 长时间显示吐司
     * @param sequence 要显示的字符串
     */
    public static void longTime(CharSequence sequence){
        if (sToast == null){
            sToast = Toast.makeText(AppApplication.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        sToast.setText(sequence);
        sToast.show();
    }
}
