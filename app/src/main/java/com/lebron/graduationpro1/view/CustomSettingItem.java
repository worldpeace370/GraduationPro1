package com.lebron.graduationpro1.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lebron.graduationpro1.R;

/**
 * Created by wuxiangkun on 2017/2/11.
 * Contact way wuxiangkun2015@163.com
 */

public class CustomSettingItem extends LinearLayout {
    private TextView tvText;
    private ImageView ivIcon;
    private View lineTop;
    private View lineBottom;
    private TextView tvSubText;
    private ImageView ivIndicator;

    public CustomSettingItem(Context context) {
        super(context);
    }

    public CustomSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CustomSetting);
        boolean isTopLineVisible = typedArray.getBoolean(
                R.styleable.CustomSetting_top_line_visibility, false);
        boolean isBottomLineVisible = typedArray.getBoolean(
                R.styleable.CustomSetting_bottom_line_visibility, true);
        boolean isIndicatorLineVisible = typedArray.getBoolean(
                R.styleable.CustomSetting_indicator_visibility, true);
        int textRes = typedArray.getResourceId(R.styleable.CustomSetting_item_text, 0);
        float textSize = typedArray.getDimension(R.styleable.CustomSetting_text_size, 0);
        int textColor = typedArray.getColor(R.styleable.CustomSetting_text_color, 0);
        int iconRes = typedArray.getResourceId(R.styleable.CustomSetting_item_icon, 0);
        float iconWidth = typedArray.getDimension(R.styleable.CustomSetting_icon_width, 0);
        float iconHeight = typedArray.getDimension(R.styleable.CustomSetting_icon_height, 0);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_custom_setting_item, this);
        tvText = (TextView) findViewById(R.id.tv_text);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        lineTop = findViewById(R.id.line_top);
        lineBottom = findViewById(R.id.line_bottom);
        tvSubText = (TextView) findViewById(R.id.tv_subtext);
        ivIndicator = (ImageView) findViewById(R.id.iv_indicator);

        if (textRes == 0) {
            tvText.setVisibility(View.GONE);
        } else {
            tvText.setText(textRes);
        }

        if (textSize > 0) {
            tvText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }

        if (textColor == 0) {
            tvText.setTextColor(Color.BLACK);
        } else {
            tvText.setTextColor(textColor);
        }

        boolean isSetIconImageResource = false;
        if (iconRes == 0) {
            ivIcon.setVisibility(View.GONE);
        } else {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(iconRes);
            isSetIconImageResource = true;
        }
        //增加设置ivIcon的布局属性
        if (iconWidth == 0 || iconHeight == 0) {
            ivIcon.setVisibility(View.GONE);
        } else if (isSetIconImageResource) {
            ViewGroup.LayoutParams params = ivIcon.getLayoutParams();
            params.width = (int) iconWidth;
            params.height = (int) iconHeight;
            ivIcon.setLayoutParams(params);
        }

        if (isTopLineVisible) {
            lineTop.setVisibility(View.VISIBLE);
        } else {
            lineTop.setVisibility(View.GONE);
        }
        if (isBottomLineVisible) {
            lineBottom.setVisibility(View.VISIBLE);
        } else {
            lineBottom.setVisibility(View.GONE);
        }
        if (isIndicatorLineVisible) {
            ivIndicator.setVisibility(View.VISIBLE);
        } else {
            ivIndicator.setVisibility(View.GONE);
        }
        typedArray.recycle();
    }

    public void setText(String text) {
        tvText.setText(text);
    }

    public String getText() {
        return tvText.getText().toString().trim();
    }

    public void setText(@StringRes int textRes) {
        tvText.setText(textRes);
    }

    public void setIconRes(@DrawableRes int iconRes) {
        ivIcon.setVisibility(View.VISIBLE);
        ivIcon.setImageResource(iconRes);
    }

    public void setSubText(String text) {
        tvSubText.setVisibility(View.VISIBLE);
        tvSubText.setText(text);
    }

    public String getSubText() {
        return tvSubText.getText().toString().trim();
    }

    public void setSubText(@StringRes int textRes) {
        tvSubText.setVisibility(View.VISIBLE);
        tvSubText.setText(textRes);
    }

    public void setIvIndicatorVisibility(boolean isVisible) {
        if (isVisible) {
            ivIndicator.setVisibility(View.VISIBLE);
        } else {
            ivIndicator.setVisibility(View.GONE);
        }
    }

    public void setTopLineVisible(boolean isVisible) {
        if (isVisible) {
            lineTop.setVisibility(View.VISIBLE);
        } else {
            lineTop.setVisibility(View.GONE);
        }
    }

    public void setBottomLineVisible(boolean isVisible) {
        if (isVisible) {
            lineBottom.setVisibility(View.VISIBLE);
        } else {
            lineBottom.setVisibility(View.GONE);
        }
    }

}
