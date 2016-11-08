package com.lebron.graduationpro1.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**自定义抽屉布局,侧滑面板.
 * 基于ViewDragHelper,google2013年io大会提出的,解决界面控件拖拽问题
 * action_down,action_up,action_move
 * 应用场景:扩展主面板功能
 * 固定模板:a,b,c,d
 * Created by lebron on 16-11-7.
 * Contact by wuxiangkun2015@163.com
 */

public class DragLayout extends FrameLayout {
    private String TAG = "DragLayout";
    private ViewDragHelper mViewDragHelper;
    //左面板
    private ViewGroup mLeftContent;
    //主面板
    private ViewGroup mMainContent;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mRange;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        /**
         * d.重写事件
         */
        ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
            //
            //child 当前被拖拽的view, pointerId 区分多点触摸的id

            /**
             * 1. 根据返回结果决定当前child是否可以拖拽(需要返回true)
             * @param child 当前被拖拽的view
             * @param pointerId 区分多点触摸的id
             * @return 需要返回true(某些条件下的)
             */
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                Log.i(TAG, "tryCaptureView: " + pointerId);
                //为了使左面板不能移动,如果直接设置return child == mMainContent则当拖拽左面板的时候
                //下面的几个方法不会被回调.正确的做法是在此处始终返回true,但是当拖拽左面板的时候再次绘制
                //左面板的位置mLeftContent.layout(0, 0, 0 + mMeasuredWidth, 0 + mMeasuredHeight)
                //即让左面板始终布局在原来的位置
                return true; //当前主面板可以 拖拽return child == mMainContent;直接return true表示两个面板都能拖拽
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                Log.i(TAG, "onViewCaptured: " + activePointerId);
            }
            //水平位置至多可移动的范围,mRange值为屏幕宽带的0.6倍
            //不对拖拽进行真正的生效,仅仅是决定了动画执行速度
            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.i(TAG, "getViewHorizontalDragRange: mRange = " + mRange);
                return mRange;
            }

            /**
             * 2. 根据建议值修正将要移动到的(横向)位置,此时没有发生真正的移动
             * @param child 当前被拖拽的view
             * @param left 新的位置建议值 left = dx + child.getLeft(),表示能移动到的位置
             * @param dx 位置变化量 +表示右移,-表示左移
             * @return left 表示x方向能移动的距离
             */
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.i(TAG, "clampViewPositionHorizontal: " + left);
                if (child == mMainContent){
                    left = fixLeft(left);
                }
                return left;
            }

            /**
             * 往左侧移动到边界的时候,不再移动,返回0
             * 如果left<0,返回0;如果left>mRange,返回mRange
             * @param left 传入的left
             * @return 二选一的值
             */
            private int fixLeft(int left) {
                if (left < 0){
                    return 0;
                }else if (left > mRange){//如果超过mRange,最大到mRange
                    return mRange;
                }
                return left;
            }

            /**
             * 根据建议值修正将要移动到的(纵向)位置,此时没有发生真正的移动
             * @param child 当前被拖拽的view
             * @param top top = dy + child.getTop()
             * @param dy 位置变化量 +表示下移,-表示上移
             * @return 表示y方向能移动的距离
             */
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.i(TAG, "clampViewPositionVertical: " + top);
                return 0;//返回0表示y方向不允许移动
            }
            //当位置改变的时候,处理要做的事情(更新状态,重绘界面,伴随动画)
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                //即让左面板始终布局在原来的位置,为了控制左面板不动
                if (changedView == mLeftContent){
                    mLeftContent.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
                }
                //为了低版本2.2(android 10)的兼容性,每次修改值之后,手动重绘view
                invalidate();
            }
        };


        /**
         * a.初始化操作,静态方法
         */
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }
    /**
     * b.自定义view的一般三个实现方法,在这里传递触摸事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //传递给ViewDragHelper来处理
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //最好加上try catch输出异常信息
        try {
            mViewDragHelper.processTouchEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
        //返回true,表示持久接受事件
        return true;
    }

    /**
     * c.通过onFinishInflate()方法获取子view;
     * 写注释,容错性检查(两个child view 且是能instanceof ViewGroup)
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2){
            throw new IllegalStateException("Your ViewGroup must have two children at least!");
        }
        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("Your child view must be instanceof ViewGroup");
        }
        //左面板
        mLeftContent = ((ViewGroup) getChildAt(0));
        //主面板
        mMainContent = ((ViewGroup) getChildAt(1));
    }
    /**获取屏幕的宽高方法,除了用onMeasure()之外
     * 当尺寸发生变化的时候调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取了屏幕的宽高
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        mRange = (int) (mMeasuredWidth * 0.6f);
    }
}
