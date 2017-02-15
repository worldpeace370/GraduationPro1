package com.lebron.graduationpro1.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lebron.graduationpro1.ui.activity.LoginActivity;
import com.lebron.graduationpro1.utils.LebronPreference;

/**
 * 没有布局的Activity
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LebronPreference.getInstance().getHasLogin()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
