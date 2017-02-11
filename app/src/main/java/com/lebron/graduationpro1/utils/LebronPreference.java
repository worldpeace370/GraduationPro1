package com.lebron.graduationpro1.utils;

import android.content.SharedPreferences;

import com.lebron.graduationpro1.base.BasePreference;

/**
 * Created by wuxiangkun on 2017/2/9.
 * Contact way wuxiangkun2015@163.com
 */

public class LebronPreference extends BasePreference {
    private final static String PREFERENCE_KEY_NODE_CHOICE = "node_choice";

    private static LebronPreference instance;

    public static LebronPreference getInstance() {
        if (instance == null) {
            instance = new LebronPreference();
        }
        return instance;
    }

    public void saveNodeChoice(String nodeName) {
        checkPrefs();
        if (sPrefs != null) {
            SharedPreferences.Editor editor = sPrefs.edit();
            editor.putString(PREFERENCE_KEY_NODE_CHOICE, nodeName);
            if (mUseApply) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
    }

    public String getNodeChoice() {
        checkPrefs();
        String nodeName = "";
        if (sPrefs != null) {
            nodeName = sPrefs.getString(PREFERENCE_KEY_NODE_CHOICE, "");
        }
        return nodeName;
    }
}
