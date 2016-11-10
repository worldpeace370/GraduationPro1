package com.lebron.graduationpro1.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 自定义可以滑动的RelativeLayout,类似于IOS的滑动删除页面效果,当我们要使用
 * 此功能的时候,需要将该Activity的顶层布局设置为SlidingFinishLayout,
 * 然后需要调用setTouchView()方法来设置需要滑动的View
 * Created by lebron on 16-11-10. 被验证不好用,哭瞎(不好用但是不会改变啊,那个主题问题)
 * 见博客:http://blog.csdn.net/xiaanming/article/details/20934541
 * Contact by wuxiangkun2015@163.com
 */

public class SlidingFinishLayout extends RelativeLayout{
    private static final String TAG = "SlidingFinishLayout";
    private OnSlidingFinishListener mSlidingFinishListener;

    private ViewDragHelper mViewDragHelper;
    private ViewGroup mMainContent;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mRange;

    /**
     * 设置监听器,在onSlidingFinished方法中finish Activity
     * @param slidingFinishListener 监听器实例
     */
    public void setOnSlidingFinishListener(OnSlidingFinishListener slidingFinishListener) {
        mSlidingFinishListener = slidingFinishListener;
    }

    public interface OnSlidingFinishListener {
        void onSlidingFinished();
    }

    public SlidingFinishLayout(Context context) {
        this(context, null);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingFinishLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mMeasuredWidth;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
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
                }else if (left > mMeasuredWidth){//如果超过mRange,最大到mRange
                    return mMeasuredWidth;
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
                newLeft = mMainContent.getLeft() + dx;
                Log.i(TAG, "onViewPositionChanged: newLeft = " + newLeft + "mRange = " + mRange);
                //处理拖拽时候的动画和事件监听
//                dealDragEventAnimationAndListener(newLeft);
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
                if (xvel == 0 && releasedChild.getLeft() >= mRange){
                    //移动到最右侧然后消失
                    openDrag();
                    //接口回调
                    if (mSlidingFinishListener != null){
                        mSlidingFinishListener.onSlidingFinished();
                    }
                }else {
                    closeDrag();
                }
            }
        };
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
        if (getChildCount() < 1){
            throw new IllegalStateException("Your ViewGroup must have two children at least!");
        }
        if (!(getChildAt(0) instanceof ViewGroup)){
            throw new IllegalArgumentException("Your child view must be instanceof ViewGroup");
        }
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
        mRange = (int) (mMeasuredWidth * 0.5f);
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
        int finalLeft = mMeasuredWidth;
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
