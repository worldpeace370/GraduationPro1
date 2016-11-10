package com.lebron.graduationpro1.ui.activity;

import android.view.View;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;

import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @OnClick(R.id.setting_back)
    public void back(View view){
        if (view.getId() == R.id.setting_back){
            finish();
        }
    }
}
