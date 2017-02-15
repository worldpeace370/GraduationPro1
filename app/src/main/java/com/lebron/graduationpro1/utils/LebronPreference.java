package com.lebron.graduationpro1.utils;

import android.content.SharedPreferences;

import com.lebron.graduationpro1.base.BasePreference;
import com.lebron.graduationpro1.minepage.model.UserInfo;

/**
 * Created by wuxiangkun on 2017/2/9.
 * Contact way wuxiangkun2015@163.com
 */

public class LebronPreference extends BasePreference {
    private final static String PREFERENCE_KEY_NODE_CHOICE = "node_choice";
    private final static String PREFERENCE_KEY_USER_INFO = "USER_INFO";
    private final static String PREFERENCE_KEY_HAS_LOGIN = "has_login";
    private static LebronPreference instance;

    public static LebronPreference getInstance() {
        if (instance == null) {
            instance = new LebronPreference();
        }
        return instance;
    }

    /**
     * 保存上次节点选择
     *
     * @param nodeName 供暖节点名称
     */
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

    /**
     * 保存用户简单的个人信息
     *
     * @param userInfo 用户简单的个人信息
     */
    public void saveUserInfo(UserInfo userInfo) {
        checkPrefs();
        if (sPrefs != null) {
            SharedPreferences.Editor editor = sPrefs.edit();
            String json = JsonSerializer.getInstance().serialize(userInfo);
            editor.putString(PREFERENCE_KEY_USER_INFO, json);
            if (mUseApply) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
    }

    public UserInfo getUserInfo() {
        checkPrefs();
        UserInfo userInfo = null;
        if (sPrefs != null) {
            String json = sPrefs.getString(PREFERENCE_KEY_USER_INFO, "");
            userInfo = JsonSerializer.getInstance().deserialize(json, UserInfo.class);
        }
        return userInfo;
    }

    /**
     * 保存是否登录过
     *
     * @param isLogin hasLogin = true
     */
    public void saveHasLogin(boolean isLogin) {
        checkPrefs();
        if (sPrefs != null) {
            SharedPreferences.Editor editor = sPrefs.edit();
            editor.putBoolean(PREFERENCE_KEY_HAS_LOGIN, isLogin);
            if (mUseApply) {
                editor.apply();
            } else {
                editor.commit();
            }
        }
    }

    public boolean getHasLogin() {
        checkPrefs();
        boolean hasLogin = false;
        if (sPrefs != null) {
            hasLogin = sPrefs.getBoolean(PREFERENCE_KEY_HAS_LOGIN, false);
        }
        return hasLogin;
    }
}
