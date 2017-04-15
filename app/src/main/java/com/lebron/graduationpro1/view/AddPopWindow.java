package com.lebron.graduationpro1.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.lebron.graduationpro1.R;

/**
 *自定义popupWindow
 */
public class AddPopWindow extends PopupWindow implements View.OnClickListener{
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
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		//设置AddPopWindow的弹出窗体的可点击
		this.setFocusable(true);
		//刷新状态
		this.update();
		this.setBackgroundDrawable(new BitmapDrawable()); //此句话不设置的话,按下返回键和点击空白区域都不会引起window的消失
		//设置SelectPicPopWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupWindowAnimation);
		LinearLayout refreshData = (LinearLayout) contentView.findViewById(R.id.refresh_data);
		LinearLayout watchDetail = (LinearLayout) contentView
				.findViewById(R.id.watch_to_detail);
		LinearLayout choiceNode = (LinearLayout) contentView
				.findViewById(R.id.choice_node);
		LinearLayout saveImageSdCard = (LinearLayout) contentView
				.findViewById(R.id.save_image_sd_card);
		//刷新数据
		refreshData.setOnClickListener(this);
		//查看细节
		watchDetail.setOnClickListener(this);
		//节点选择
		choiceNode.setOnClickListener(this);
		//保存SD卡
		saveImageSdCard.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		if (mListener != null){
			mListener.onItemClick(view.getId());
		}
		AddPopWindow.this.dismiss();
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
