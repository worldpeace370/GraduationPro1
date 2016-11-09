package com.lebron.graduationpro1.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
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
    //View的宽高
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    //主面板最远向右拖拽的距离
    private int mRange;

    //初始状态
    private Status mStatus = Status.Closed;
    private OnDragStatusChangedListener mDragStatusChangedListener;
    /**
     * 需要监听的状态枚举
     */
    public enum Status{
        Closed, Opened, Dragging
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    /**
     * 定义回调接口,用于事件监听
     */
    public interface OnDragStatusChangedListener{
        void onClosed();
        void onOpened();
        void onDragging(float percent);
    }

    public void setDragStatusChangedListener(OnDragStatusChangedListener listener) {
        mDragStatusChangedListener = listener;
    }

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
             * 此代码段设置了主面板最多右移的距离
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

            /**
             * 当位置改变的时候,处理要做的事情(更新状态,重绘界面,伴随动画),此时view的位置已经发生了改变
             * @param changedView 发生改变的view
             * @param left 距离左边的位置
             * @param top 距离顶部的位置
             * @param dx x方向的位移
             * @param dy y方向的位移
             */
            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                //将拖拽左面板的动作传递给主面板
                int newLeft = left;
                if (changedView == mLeftContent){
                    newLeft = mMainContent.getLeft() + dx;
                    newLeft = fixLeft(newLeft);
                    //即让左面板始终布局在原来的位置,为了控制左面板不动(强制放回)
                    mLeftContent.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
                    //将左面板的本来有的位移dx传递给主面板
                    mMainContent.layout(newLeft, 0, newLeft + mMeasuredWidth, mMeasuredHeight);
                }
                //处理拖拽时候的动画和事件监听
                dealDragEventAnimationAndListener(newLeft);
                //为了低版本2.2(android 10)的兼容性,每次修改值之后,手动重绘view
                invalidate();
            }

            /**
             * child view释放后执行的操作
             * @param releasedChild 当前释放的view
             * @param xvel x方向的速度
             * @param yvel y方向的速度
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (xvel > 0){
                    openDrag();
                }else if (xvel == 0 && releasedChild.getLeft() > mRange / 2.0f){
                    openDrag();
                }else {
                    closeDrag();
                }
            }
        };

        /**
         * a.初始化操作,静态方法
         */
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    /**
     * 展示根据view移动的left距离的动画效果
     * 1.左面板:缩放动画,平移动画,透明度动画
     * 2.主面板:缩放动画
     * 3.背景动画:亮度变化(颜色变化)
     * @param newLeft view移动的left距离
     */
    private void dealDragEventAnimationAndListener(int newLeft) {
        float percent = newLeft * 1.0f / mRange; // 范围:0.0~100变化
        //更新抽屉布局状态,执行回调
        processListener(percent);
        //执行动画
        runAnimation(percent);
    }

    /**
     * 更新状态,处理回调事件
     * @param percent 当前的拖拽情况
     */
    private void processListener(float percent) {
        //由于能执行此方法说明一直在on dragging,所以一直回调该函数
        if (mDragStatusChangedListener != null){
            mDragStatusChangedListener.onDragging(percent);
        }
        //记录之前的状态,跟现在的状态判断,来决定是否执行close or open
        Status pre = mStatus;
        mStatus = updateStatus(percent);
        if (mStatus != pre){
            if (mStatus == Status.Opened && mDragStatusChangedListener != null){
                mDragStatusChangedListener.onOpened();
            }else if (mStatus == Status.Closed && mDragStatusChangedListener != null){
                mDragStatusChangedListener.onClosed();
            }
        }
    }

    private Status updateStatus(float percent) {
        if (percent == 0f){
            return Status.Closed;
        }else if (percent == 1.0f){
            return Status.Opened;
        }
        return Status.Dragging;
    }

    /**
     * 运行动画
     * @param percent 移动的距离占总距离的百分比
     */
    private void runAnimation(float percent) {
        //> 1.左面板
        //左面板:缩放动画
        mLeftContent.setScaleX(evaluate(percent, 0.5f, 1.0f));// 0.5~1.0放大
        mLeftContent.setScaleY(evaluate(percent, 0.5f, 1.0f));// 0.5~1.0放大
        //左面板:平移动画
        mLeftContent.setTranslationX(evaluate(percent, -mMeasuredWidth / 2.0f, 0));//从-mMeasuredWidth / 2.0f~0移动
        //左面板:透明度动画
        mLeftContent.setAlpha(evaluate(percent, 0.5f, 1.0f));

        //> 2.主面板
        //主面板:缩放动画
        mMainContent.setScaleX(evaluate(percent, 1.0f, 0.8f));
        mMainContent.setScaleY(evaluate(percent, 1.0f, 0.8f));

        //> 3.背景
        //背景动画:亮度变化(颜色变化),第一个参数颜色值,第二个参数是混合模式
        getBackground().setColorFilter((Integer) evaluateColor(percent, Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
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

    /**
     * 动画步骤2,平滑动画相关方法
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //持续平滑动画,高频调用
        if (mViewDragHelper.continueSettling(true)){
            //如果返回true,动画还需要持续执行
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 关闭抽屉,默认平滑
     */
    public void closeDrag() {
        closeDrag(true);
    }

    /**
     * 是否是平滑的关闭抽屉
     * @param isSmooth true or false
     */
    public void closeDrag(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth){
            /**
             * 动画步骤1,触发一个平滑动画
             */
            if (mViewDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                //返回true表示还没有移动到指定的位置,需要刷新界面
                //参数this(child 所在的ViewGroup)
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mMeasuredWidth, mMeasuredHeight);
        }
    }

    /**
     * 打开抽屉
     */
    public void openDrag() {
        openDrag(true);
    }

    private void openDrag(boolean isSmooth) {
        int finalLeft = mRange;
        if (isSmooth){
            /**
             * 动画步骤1,触发一个平滑动画
             */
            if (mViewDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                //返回true表示还没有移动到指定的位置,需要刷新界面
                //参数this(child 所在的ViewGroup)
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mMeasuredWidth, mMeasuredHeight);
        }
    }

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }
}
