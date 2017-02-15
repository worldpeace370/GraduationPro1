package com.lebron.graduationpro1.minepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.minepage.activity.SettingActivity;
import com.lebron.graduationpro1.minepage.activity.UserCenterActivity;
import com.lebron.graduationpro1.view.CustomSettingItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MineFragment.
     */
    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        return mRootView;
    }

    private void bindViews(View rootView) {
        mUserAccountLayout = ((RelativeLayout) rootView.findViewById(R.id.user_account_layout));
        mMineDeviceItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_device));
        mMineCollectItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_collect));
        mMineNoteItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_note));
        mMineContractsItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_contracts));
        mMineInfoItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_info));
        mSystemSettingItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_system_setting));
        mNightModeItem = ((CustomSettingItem) rootView.findViewById(R.id.item_mine_night_mode));
    }

    private void setListener() {
        mUserAccountLayout.setOnClickListener(this);
        mMineDeviceItem.setOnClickListener(this);
        mMineCollectItem.setOnClickListener(this);
        mMineNoteItem.setOnClickListener(this);
        mMineContractsItem.setOnClickListener(this);
        mMineInfoItem.setOnClickListener(this);
        mSystemSettingItem.setOnClickListener(this);
        mNightModeItem.setOnClickListener(this);
    }

    private void init() {

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

    private <T extends BaseActivity> void startActivityByClassName(Class<T> tClass) {
        Intent intent = new Intent(getActivity(), tClass);
        startActivity(intent);
    }
}
