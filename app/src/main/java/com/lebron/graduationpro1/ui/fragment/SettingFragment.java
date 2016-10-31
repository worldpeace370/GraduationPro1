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

    private Preference clear_cache;
    private CheckBoxPreference net_mode;
    private CheckBoxPreference scan_mode;
    private Preference about_app;
    private Preference app_version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initView();
        initListener();
    }

    private void initView() {
        clear_cache = findPreference(CLEAR_CACHE);
        net_mode = (CheckBoxPreference) findPreference(NET_MODE);
        scan_mode = (CheckBoxPreference) findPreference(SCAN_MODE);
        about_app = findPreference(ABOUT_APP);
        app_version = findPreference(APP_VERSION);
        app_version.setTitle(AppInfoUtils.getVersionName(getActivity()));

    }

    private void initListener() {
        net_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    ShowToast.shortTime(" ");
                }else {
                    ShowToast.shortTime(" ");
                }
                return true;
            }
        });

        scan_mode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if((Boolean) newValue){
                    ShowToast.shortTime(" ");
                }else {
                    ShowToast.shortTime(" ");
                }
                return true;
            }
        });

        clear_cache.setOnPreferenceClickListener(this);
        about_app.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}
