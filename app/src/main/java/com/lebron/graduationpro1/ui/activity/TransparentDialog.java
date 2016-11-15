package com.lebron.graduationpro1.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lebron.graduationpro1.R;

/**
 * <activity android:name=".ui.activity.TransparentDialog"
 * android:theme="@style/Theme.AppCompat.Dialog"/>
 */
public class TransparentDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent_dialog);
        try {
            Thread.sleep(10);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
