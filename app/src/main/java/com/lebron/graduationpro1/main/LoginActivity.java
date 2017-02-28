package com.lebron.graduationpro1.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.minepage.model.UserInfo;
import com.lebron.graduationpro1.utils.LebronPreference;
import com.lebron.graduationpro1.view.ClearEditText;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

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
        mUserName.setText(LebronPreference.getInstance().getUserInfo().getNickName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                handleLogin();
                break;
            case R.id.tv_reg:
                //注册界面
                break;
            case R.id.tv_forget_pwd:
                //找回密码界面
                break;
            default:
                break;
        }
    }

    private void handleLogin() {
        if (mUserName.getText().toString().equals("admin") && mPassWord.getText().toString().equals("123")) {
            showCustomToast(R.mipmap.toast_done_icon, "登陆成功!");
            LebronPreference.getInstance().saveUserInfo(new UserInfo("", "admin", ""));
            LebronPreference.getInstance().saveHasLogin(true);
            startActivityByClassName(MainActivity.class);
            finish();
        }
    }
}
