package com.lebron.graduationpro1.minepage.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.minepage.activity.SettingActivity;
import com.lebron.graduationpro1.minepage.activity.UserCenterActivity;
import com.lebron.graduationpro1.utils.LebronPreference;
import com.lebron.graduationpro1.view.CustomSettingItem;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人中心界面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{
    private View mRootView;
    private RelativeLayout mUserAccountLayout;
    private CircleImageView mImgHead;
    private CustomSettingItem mMineDeviceItem;
    private CustomSettingItem mMineCollectItem;
    private CustomSettingItem mMineNoteItem;
    private CustomSettingItem mMineContractsItem;
    private CustomSettingItem mMineInfoItem;
    private CustomSettingItem mSystemSettingItem;
    private CustomSettingItem mNightModeItem;
    private MyReceiver mReceiver;

    public MineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter("picture.has.cropped");
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        return mRootView;
    }

    @Override
    protected void bindViews(View rootView) {
        mUserAccountLayout = ((RelativeLayout) rootView.findViewById(R.id.user_account_layout));
        mImgHead = ((CircleImageView) rootView.findViewById(R.id.image_user_head));
        mMineDeviceItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_device));
        mMineCollectItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_collect));
        mMineNoteItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_note));
        mMineContractsItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_contracts));
        mMineInfoItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_info));
        mSystemSettingItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_system_setting));
        mNightModeItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_night_mode));
    }

    @Override
    protected void setListener() {
        mUserAccountLayout.setOnClickListener(this);
        mMineDeviceItem.setOnClickListener(this);
        mMineCollectItem.setOnClickListener(this);
        mMineNoteItem.setOnClickListener(this);
        mMineContractsItem.setOnClickListener(this);
        mMineInfoItem.setOnClickListener(this);
        mSystemSettingItem.setOnClickListener(this);
        mNightModeItem.setOnClickListener(this);
    }

    @Override
    protected void init() {
        if (null != LebronPreference.getInstance().getHeadImageUrl()){
            String urlStr = LebronPreference.getInstance().getHeadImageUrl();
            Bitmap bitmap = BitmapFactory.decodeFile(urlStr);
            mImgHead.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_account_layout:
            case R.id.item_mine_info:
                startActivityByClassName(UserCenterActivity.class);
                break;
            case R.id.item_mine_system_setting:
                startActivityByClassName(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    class  MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            init();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(mReceiver);
    }
}
