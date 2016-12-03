package com.lebron.graduationpro1.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**用于主面板的LinearLayout,当主面板拖拽到右边缩小了时,
 * 希望对其中的子view不能进行操作,且当轻触点击后松手,抽屉布局能自动关闭
 * 需要重写拦截和消费方法.在此类中需要获得DragLayout对象,可以通过set方法
 * Created by lebron on 16-11-9.
 * Contact by wuxiangkun2015@163.com
 */

public class MainLinearLayout extends LinearLayout {
    private DragLayout mDragLayout;

    public void setDragLayout(DragLayout dragLayout) {
        mDragLayout = dragLayout;
    }

    public MainLinearLayout(Context context) {
        super(context);
    }

    public MainLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //如果抽屉打开了,拦截事件
        if (mDragLayout.getStatus() == DragLayout.Status.Opened){
            return true;
        }else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果抽屉打开了,由于事件被拦截,需要由当前view处理事件.点击后松手,关闭抽屉,其他的操作不做
        if (mDragLayout.getStatus() == DragLayout.Status.Opened){
            if (event.getAction() == MotionEvent.ACTION_UP){
                if (mDragLayout != null){
                    mDragLayout.closeDrag();
                }else {
                    throw new NullPointerException("没有调用setDragLayout方法!");
                }
            }
            return true;
        }else {
            return super.onTouchEvent(event);
        }
    }
}
