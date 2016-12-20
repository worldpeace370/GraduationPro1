package com.lebron.graduationpro1.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.lebron.graduationpro1.R;

/**
 * 当有输入的时候，右侧出现"清除icon"当点击的时候，清除当前EditText的输入信息
 * Created by wuxia on 2016/12/20.
 * Contacts by wuxiangkun2015@163.com
 */

public class ClearEditText extends EditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mDrawable;
    private boolean mHasFocused;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关属性
     */
    private void init() {
        mDrawable = getCompoundDrawables()[2]; //获取drawableRight
        if (mDrawable == null) {
            mDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.input_clean);
        }
        setClearIconVisibility(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHasFocused) {
            boolean isTouched = event.getX() > (getWidth() - getTotalPaddingRight()) &&
                    event.getX() < (getWidth() - getPaddingRight());
            if (isTouched) {
                setText("");
                final MotionEvent cancelEvent = MotionEvent.obtain(event);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                post(new Runnable() {
                    @Override
                    public void run() {
                        dispatchTouchEvent(cancelEvent);
                        cancelEvent.recycle();
                    }
                });
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置"清除icon"是否可见
     *
     * @param isVisible true or false
     */
    private void setClearIconVisibility(boolean isVisible) {
        Drawable drawableRight = isVisible ? mDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                drawableRight, getCompoundDrawables()[3]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mHasFocused = hasFocus;
        if (hasFocus) {
            setClearIconVisibility(getText().length() > 0);
        } else {
            setClearIconVisibility(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mHasFocused) {
            setClearIconVisibility(text.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
