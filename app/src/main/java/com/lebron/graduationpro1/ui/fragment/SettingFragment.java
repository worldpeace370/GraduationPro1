package com.lebron.graduationpro1.ui.fragment;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.utils.AppInfoUtils;
import com.lebron.graduationpro1.utils.ShowToast;

/**设置界面的Fragment,继承自PreferenceFragment
 * Created by lebron on 16-10-30.
 * Contact by wuxiangkun2015@163.com
 */

public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

    private static final String CLEAR_CACHE = "clear_cache";
    private static final String NET_MODE = "net_mode";
    private static final String SCAN_MODE = "scan_mode";
    private static final String ABOUT_APP = "about_app";
    private static final String APP_VERSION = "app_version";

    private Preference mClearCache;
    private CheckBoxPreference mNetMode;
    private CheckBoxPreference mScanMode;
    private Preference mAboutApp;
    private Preference mAppVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initView();
        initListener();
    }

    private void initView() {
        mClearCache = findPreference(CLEAR_CACHE);
        mNetMode = (CheckBoxPreference) findPreference(NET_MODE);
        mScanMode = (CheckBoxPreference) findPreference(SCAN_MODE);
        mAboutApp = findPreference(ABOUT_APP);
        mAppVersion = findPreference(APP_VERSION);
        mAppVersion.setTitle("版本:" + AppInfoUtils.getVersionName(getActivity()));
    }

    private void initListener() {
        mNetMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    ShowToast.shortTime("已开启2G/3G/4G无图模式");
                }else {
                    ShowToast.shortTime("已关闭2G/3G/4G无图模式");
                }
                return true;
            }
        });

        mScanMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    ShowToast.shortTime("已开启夜间模式");
                }else {
                    ShowToast.shortTime("已关闭夜间模式");
                }
                return true;
            }
        });

        mClearCache.setOnPreferenceClickListener(this);
        mAboutApp.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return true;
    }
}
