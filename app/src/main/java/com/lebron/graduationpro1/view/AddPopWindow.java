package com.lebron.graduationpro1.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
	private View conentView;
	private OnPopupWindowItemClickListener mListener;

	public void setOnPopupWindowItemClickListener(OnPopupWindowItemClickListener listener) {
		mListener = listener;
	}

	public AddPopWindow(final Activity context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.add_popup_dialog, null);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		//设置SelectPicPopupWindow的View
		this.setContentView(conentView);
		//设置SelectPicPopupWindow的弹出窗体的宽
		this.setWidth(w / 2 - 70);
		//设置SelectPicPopupWindow的弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow的弹出窗体的可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		//刷新状态
		this.update();
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0000000000);
		//点back键和其他地方使其消失,设置了这个才能触发OnDismissListener,设置其他控件变化等操作
		this.setBackgroundDrawable(dw);
		//设置SelectPicPopWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupWindowAnimation);
		LinearLayout addTaskLayout = (LinearLayout) conentView
				.findViewById(R.id.select_new_node);
		LinearLayout teamMemberLayout = (LinearLayout) conentView
				.findViewById(R.id.save_image_sd_card);
		//选择节点
		addTaskLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (arg0.getId() == R.id.select_new_node && mListener != null){
					mListener.onItemClick(arg0.getId());
				}
				AddPopWindow.this.dismiss();
			}
		});
		//保存SD卡
		teamMemberLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.save_image_sd_card && mListener != null){
					mListener.onItemClick(v.getId());
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
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
		} else {
			this.dismiss();
		}
	}

	public interface OnPopupWindowItemClickListener{
		void onItemClick(int id);
	}
}
