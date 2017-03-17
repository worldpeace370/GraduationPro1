/**
 * RefreshableView.java
 *
 * @author tianli
 * @date 2011-8-25
 * <p>
 * Copyright 2011 NetEase. All rights reserved.
 */
package com.lebron.graduationpro1.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tianli
 */
public class RefreshableView extends LinearLayout {
    private static final int DEFAULT_DURATION_TIME = 500;// 默认过渡效果时间
    private final static float MOVEMENT_FACTOR = (float) 0.3;
    private static final int ARROW_UP = 1;
    private static final int ARROW_DOWN = 0;
    private View refreshView;
    private ImageView refreshIndicator;
    private ImageView progressView;
    private TextView refreshTimeView;
    private LinearLayout layoutRefreshHeader;
    private String refreshTimeText;
    private int touchSlop;
    private int refreshViewHeight;
    private Scroller scroller;
    private RefreshListener refreshListener;
    private boolean isRefreshing;
    private boolean isRefreshEnabled;
    private int arrowDirection = 0;
    private Long refreshTime;
    private int lastY = Integer.MIN_VALUE;
    private boolean isBeingDragged;
    private int startX, startY;
    private Animation rotateDownAni;
    private Animation rotateUpAni;
    private Animation rotateRefreshAni;
    private MoveListener moveListener;
    private List<OnRefreshFinishListener> onRefreshFinishListeners = new ArrayList<OnRefreshFinishListener>();//监听多个并行请求的刷新结束

    private int refreshIndicatorResid = R.mipmap.icon_pulltorefresh_arrow_down;
    private int progressViewResid = R.mipmap.icon_pulltorefresh_refresh;

    public RefreshableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        refreshView = LayoutInflater.from(getContext())
                .inflate(R.layout.refresh_item, this, false);
        layoutRefreshHeader = (LinearLayout) refreshView
                .findViewById(R.id.layout_refresh_top);
        refreshIndicator = (ImageView) refreshView.findViewById(R.id.indicator);
        refreshIndicator.setImageResource(refreshIndicatorResid);
        arrowDirection = ARROW_DOWN;
        refreshTimeView = (TextView) refreshView.findViewById(R.id.refresh_time);
        progressView = (ImageView) refreshView.findViewById(R.id.progress);
        refreshViewHeight = Tools.dp2Px(getContext(), 60);
        LayoutParams params = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT, refreshViewHeight);
        params.topMargin = -refreshViewHeight;
        refreshView.setLayoutParams(params);
        params.gravity = Gravity.CENTER;
        addView(refreshView);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = configuration.getScaledTouchSlop();
        scrollTo(0, 0);
        rotateDownAni = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_down);
        rotateDownAni.setFillAfter(true);
        rotateUpAni = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_up);
        rotateUpAni.setFillAfter(true);
        rotateRefreshAni = AnimationUtils.loadAnimation(getContext(), R.anim.refresh_rotate);
    }

    public void setRefreshHeaderBackground(int res) {
        if (layoutRefreshHeader != null) {
            layoutRefreshHeader.setBackgroundColor(res);
        }
    }


    public void finishRefresh() {
        if (getScrollY() <= 0) {
            scroller.startScroll(0, getScrollY(), 0, 0 - getScrollY(), 500);
            invalidate();
        }
        isRefreshing = false;
    }

    public void finishRefreshWithoutAnimation() {
        scrollTo(0, 0);
        invalidate();
        isRefreshing = false;
    }

    public void setRefreshEnabled(boolean enabled) {
        isRefreshEnabled = enabled;
    }

    /**
     * @param refreshTimeText the refreshTimeText to set
     */
    public void setRefreshTimeText(String refreshTimeText) {
        this.refreshTimeText = refreshTimeText;
        refreshTimeView.setText(this.refreshTimeText);
        refreshTimeView.setVisibility(View.VISIBLE);
    }

    /**
     * @param time the refreshTimeText to set
     */
    public void setRefreshTime(Long time) {
        if (time == 0) {
            return;
        }
        refreshTimeText = getContext().getString(R.string.last_update_time) + "： ";
        refreshTimeText += Tools.getModifiedTimeTextNew(time);
        refreshTimeView.setText(refreshTimeText);
        refreshTime = time;
        refreshTimeView.setVisibility(View.VISIBLE);
    }

    private void doMovement(int delta) {
        int sy = getScrollY();
        if (delta > 0) {
            // move down
            sy += -(delta * MOVEMENT_FACTOR);
            scrollTo(0, sy);
            // invalidate();
        } else if (delta < 0) {
            // move up
            if (sy < 0) {
                sy += -(delta);
                sy = Math.min(sy, 0);
                scrollTo(0, sy);
                invalidate();
            }
        }
        if (isRefreshing) {
            return;
        }
        if (refreshTime != null) {
            setRefreshTime(refreshTime);
        }
        refreshIndicator.setVisibility(View.VISIBLE);
        progressView.clearAnimation();
        progressView.setVisibility(View.GONE);
        if (getScrollY() < -refreshViewHeight) {
            if (arrowDirection != ARROW_UP) {
                refreshIndicator.clearAnimation();
                refreshIndicator.startAnimation(rotateUpAni);
                arrowDirection = ARROW_UP;
            }
        } else {
            if (arrowDirection != ARROW_DOWN) {
                refreshIndicator.clearAnimation();
                refreshIndicator.startAnimation(rotateDownAni);
                arrowDirection = ARROW_DOWN;
            }
        }
    }

    private boolean canScroll() {
        if (getChildCount() > 1) {
            int childCount = getChildCount();
            for (int i = 1; i < childCount; i++) {
                View child = getChildAt(i);
                if (child instanceof ScrollView) {
                    int scrollY = ((ScrollView) child).getScrollY();
                    if (scrollY != 0) {
                        return true;
                    }
                } else if (child instanceof ListView) {
                    ListView listView = (ListView) child;
                    if (listView.getChildCount() > 0) {
                        View view = listView.getChildAt(0);
                        if (view.getTop() != listView.getListPaddingTop()
                                || listView.getFirstVisiblePosition() != 0) {
                            return true;
                        }
                    }
                } else if (child instanceof Refreshable) {
                    return ((Refreshable) child).canFlickDown();
                }
            }
        }

        return false;
    }

    public void refresh() {
        refresh(DEFAULT_DURATION_TIME);// 默认过渡时间为500毫秒
    }

    /**
     * 设置自动下拉刷新的时间
     *
     * @param duration
     */
    public void refresh(int duration) {
        flingToRefresh(duration);
        refreshIndicator.setImageResource(refreshIndicatorResid);
        arrowDirection = ARROW_DOWN;
        refreshIndicator.clearAnimation();
        refreshIndicator.setVisibility(View.GONE);
        if (Tools.isEmpty(refreshTimeView.getText().toString())) {
            refreshTimeView.setVisibility(View.GONE);
        } else {
            refreshTimeView.setVisibility(View.VISIBLE);
        }
        if (refreshListener != null) {
            progressView.setImageResource(progressViewResid);
            progressView.setVisibility(View.VISIBLE);
            progressView.startAnimation(rotateRefreshAni);
            refreshListener.onRefresh(this);
        }
        isRefreshing = true;
    }

    private void flingToRefresh(int duration) {
        scroller.startScroll(0, getScrollY(), 0, -refreshViewHeight - getScrollY(),
                duration);
        invalidate();
    }

    private void fling() {
        int sy = getScrollY();
        if (sy < -refreshViewHeight) {
            if (isRefreshing) {
                flingToRefresh(DEFAULT_DURATION_TIME);
                return;
            }
            refresh();
        } else if (sy < 0) {
            scroller.startScroll(0, sy, 0, 0 - sy, 500);

            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isRefreshEnabled) {
            return super.onInterceptTouchEvent(ev);
        }

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)) {
            if (Math.abs(ev.getX() - startX) > Math.abs(ev.getY() - startY)) {
                return false;
            }

        }
        if ((action == MotionEvent.ACTION_MOVE) && (isBeingDragged)) {

            return true;
        }
        // LinearLayout.LayoutParams params =
        // (LinearLayout.LayoutParams)refreshView.getLayoutParams();
        int y = (int) ev.getRawY();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int diff = Math.abs(y - lastY);
                if (diff > touchSlop) {
                    isBeingDragged = true;
                    if (y - lastY < 0) {
                        // move up
                        if (getScrollY() >= 0) {
                            isBeingDragged = false;
                        }
                    } else if (y - lastY > 0) {
                        // move down
                        if (canScroll()) {
                            isBeingDragged = false;
                        }
                    }
                }

                break;
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                if (getScrollY() < 0) {
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                    }
                    isBeingDragged = true;
                } else {
                    isBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isBeingDragged = false;
                break;
            default:
                break;
        }
        return isBeingDragged;
    }

    public boolean isBeingDragged() {
        if (getScrollY() == 0 || getScrollY() == refreshViewHeight) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        boolean r = super.onTouchEvent(ev);
        if (!isRefreshEnabled) {
            return r;
        }
        int action = ev.getAction();
        int y = (int) ev.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (getScrollY() == 0) {
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                    }
                    isBeingDragged = true;
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (lastY != Integer.MIN_VALUE) {
                    int delta = y - lastY;
                    // move down should be harder than move up, so delta's threshold
                    // should
                    // not be the same
                    if ((delta >= 6 || delta < -2) && isBeingDragged) {
                        doMovement(delta);
                        if (moveListener != null) {
                            moveListener.onMove();
                        }
                        lastY = y;
                    }
                } else {
                    lastY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                fling();
                break;
            }
            default:
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            if (moveListener != null) {
                moveListener.onMove();
            }

            invalidate();
        }
    }

    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    public void addOnRefreshFinishListener(OnRefreshFinishListener listener) {
        onRefreshFinishListeners.add(listener);
    }

    public void removeOnRefreshFinishListener(OnRefreshFinishListener listener) {
        if (listener != null && onRefreshFinishListeners != null) {
            onRefreshFinishListeners.remove(listener);
            if (onRefreshFinishListeners.size() == 0) {
                this.finishRefresh();
            }
        }
    }

    public void setRefreshIndicatorResid(int resid) {
        refreshIndicatorResid = resid;
        refreshIndicator.setImageResource(resid);
    }

    public void setProgressViewResid(int resid) {
        progressViewResid = resid;
        progressView.setImageResource(resid);
    }

    public void setRefreshTextColor(int colorResid) {
        refreshTimeView.setTextColor(getResources().getColor(colorResid));
    }

    public interface RefreshListener {
        void onRefresh(RefreshableView view);
    }


    public interface MoveListener {
        void onMove();
    }


    public interface Refreshable {
        boolean canFlickDown();
    }

    public interface OnRefreshFinishListener {
        void onRefreshFinish();//暂时没用
    }
}
