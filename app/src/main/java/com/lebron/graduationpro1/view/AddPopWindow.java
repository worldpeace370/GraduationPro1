package com.lebron.graduationpro1.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lebron.graduationpro1.R;

/**
 *自定义popupWindow
 */
public class AddPopWindow extends PopupWindow {
	private OnPopupWindowItemClickListener mListener;
	private final int mScreenWidth;

	public void setOnPopupWindowItemClickListener(OnPopupWindowItemClickListener listener) {
		mListener = listener;
	}

	public AddPopWindow(final Activity context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.add_popup_dialog, null);
		mScreenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
		//设置AddPopWindow的View
		this.setContentView(contentView);
		//设置AddPopWindow的弹出窗体的宽
		this.setWidth(mScreenWidth / 2 - 70);
		//设置AddPopWindow的弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置AddPopWindow的弹出窗体的可点击
		this.setFocusable(true);
		//刷新状态
		this.update();
		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0);
//		this.setBackgroundDrawable(dw);
		//设置SelectPicPopWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupWindowAnimation);
		LinearLayout selectNewNode = (LinearLayout) contentView
				.findViewById(R.id.select_new_node);
		LinearLayout saveImageSdCard = (LinearLayout) contentView
				.findViewById(R.id.save_image_sd_card);
		LinearLayout refreshData = (LinearLayout) contentView.findViewById(R.id.refresh_data);
		//选择节点
		selectNewNode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener != null){
					mListener.onItemClick(view.getId());
				}
				AddPopWindow.this.dismiss();
			}
		});
		//保存SD卡
		saveImageSdCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener != null){
					mListener.onItemClick(view.getId());
				}
				AddPopWindow.this.dismiss();
			}
		});
		refreshData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mListener != null){
					mListener.onItemClick(view.getId());
				}
				AddPopWindow.this.dismiss();
			}
		});
	}

	/**
	 * 显示popupWindow
	 * @param parent
	 */
	public void showPopupWindow(View parent) {
		if (!this.isShowing()) {
			//以下拉方式显示PopupWindow
			this.showAsDropDown(parent, mScreenWidth - getWidth(), 0);
		} else {
			this.dismiss();
		}
	}

	public interface OnPopupWindowItemClickListener{
		void onItemClick(int id);
	}
}
