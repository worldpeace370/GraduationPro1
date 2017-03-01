package com.lebron.graduationpro1.minepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.minepage.activity.SettingActivity;
import com.lebron.graduationpro1.minepage.activity.UserCenterActivity;
import com.lebron.graduationpro1.view.CustomSettingItem;

/**
 * 个人中心界面
 */
public class MineFragment extends BaseFragment implements View.OnClickListener{
    private View mRootView;
    private RelativeLayout mUserAccountLayout;
    private CustomSettingItem mMineDeviceItem;
    private CustomSettingItem mMineCollectItem;
    private CustomSettingItem mMineNoteItem;
    private CustomSettingItem mMineContractsItem;
    private CustomSettingItem mMineInfoItem;
    private CustomSettingItem mSystemSettingItem;
    private CustomSettingItem mNightModeItem;

    public MineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
