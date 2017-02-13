package com.lebron.graduationpro1.minepage.activity;

import android.os.Bundle;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;

/**
 * 用户中心Activity
 */
public class UserCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        bindViews();
        setListener();
        init();
    }

    @Override
    protected void bindViews() {
        initToolbar(R.string.mine_account);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {

    }
}
