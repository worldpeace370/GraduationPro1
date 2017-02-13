package com.lebron.graduationpro1.minepage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bindViews();
    }

    @Override
    protected void bindViews() {
        initToolbar(R.string.mine_system_setting);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {

    }
}
