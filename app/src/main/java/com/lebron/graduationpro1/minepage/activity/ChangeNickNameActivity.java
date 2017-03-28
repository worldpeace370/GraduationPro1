package com.lebron.graduationpro1.minepage.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.view.ClearEditText;

public class ChangeNickNameActivity extends BaseActivity {

    private ClearEditText mEditNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick_name);
        bindViews();
        setListener();
        init();
    }

    @Override
    protected void bindViews() {
        initToolbar(R.string.modify_nick_name);
        getToolbar().inflateMenu(R.menu.menu_nick_name_save);
        mEditNickName = (ClearEditText) findViewById(R.id.edt_nick_name);
    }

    @Override
    protected void setListener() {
        getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_save:
                        //业务逻辑
//                        changeNickName();
                }
                return true;
            }
        });
    }

    @Override
    protected void init() {

    }
}
