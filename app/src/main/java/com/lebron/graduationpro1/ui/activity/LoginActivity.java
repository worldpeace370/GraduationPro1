package com.lebron.graduationpro1.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.view.ClearEditText;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ClearEditText mUserName;
    private ClearEditText mPassWord;
    private Button mBtnLogin;
    private TextView mTvReg;
    private TextView mTvForgetPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setListener();
        init();
    }

    @Override
    protected void bindViews() {
        initToolbar("登录");
        mUserName = (ClearEditText) findViewById(R.id.editText_userName);
        mPassWord = (ClearEditText) findViewById(R.id.editText_pwd);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTvReg = (TextView) findViewById(R.id.tv_reg);
        mTvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
    }

    @Override
    protected void setListener() {
        mBtnLogin.setOnClickListener(this);
        mTvReg.setOnClickListener(this);
        mTvForgetPwd.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                break;
            case R.id.tv_reg:

                break;
            case R.id.tv_forget_pwd:

                break;
            default:
                break;
        }
    }
}
