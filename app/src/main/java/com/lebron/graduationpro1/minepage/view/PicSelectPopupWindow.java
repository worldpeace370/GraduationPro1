package com.lebron.graduationpro1.minepage.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.lebron.graduationpro1.R;

/**
 * 对外提供的 ClassName:PicSelectPopupWindow 图片选择弹出窗口
 */
public class PicSelectPopupWindow extends PopupWindow
        implements OnClickListener {
    private View mMenuView;
    private OnSelectedListener mOnSelectedListener;

    /**
     * 设置选择监听
     * @param listener
     */
    public void setOnSelectedListener(OnSelectedListener listener) {
        this.mOnSelectedListener = listener;
    }

    public PicSelectPopupWindow(Activity context) {
        super(context);
        mMenuView = View.inflate(context, R.layout.pic_popup, null);
        Button btnPhotograph = (Button) mMenuView.findViewById(R.id.btn_photograph);
        Button btnUserAlbum = (Button) mMenuView.findViewById(R.id.btn_user_album);
        Button btnCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);

        btnPhotograph.setOnClickListener(this);
        btnUserAlbum.setOnClickListener(this);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(mMenuView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0)); // instantiate a ColorDrawable
        update();
        // set touch listener
        mMenuView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photograph:
                if(null != mOnSelectedListener) {
                    mOnSelectedListener.onSelected(v, 0);
                }
                break;
            case R.id.btn_user_album:
                if(null != mOnSelectedListener) {
                    mOnSelectedListener.onSelected(v, 1);
                }
                break;
            case R.id.btn_cancel:
                if(null != mOnSelectedListener) {
                    mOnSelectedListener.onSelected(v, 2);
                }
                break;
        }
    }

    /**
     * 选择监听接口
     */
    public interface OnSelectedListener {
        void onSelected(View view, int position);
    }
}
