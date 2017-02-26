package com.lebron.graduationpro1.main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.detailpage.view.DetailFragment;
import com.lebron.graduationpro1.scanpage.view.ScanFragment;
import com.lebron.graduationpro1.controlpage.ControlFragment;
import com.lebron.graduationpro1.minepage.fragment.MineFragment;
import com.lebron.graduationpro1.ui.fragment.VideoFragment;
import com.lebron.graduationpro1.utils.MyActivityManager;
import com.lebron.graduationpro1.utils.ShowToast;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private RadioGroup mRadioGroup;
    private final int tabScan = R.id.scan;
    private final int tabVideo = R.id.video;
    private final int tabControl = R.id.control;
    private final int tabDetails = R.id.details;
    private final int tabMine = R.id.mine;
    private int currentSelectedTab = R.id.scan;
    private ScanFragment mScanFragment;
    private VideoFragment mVideoFragment;
    private ControlFragment mControlFragment;
    private DetailFragment mDetailFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager;
    //用于按下两次返回键退出程序用
    private long mExitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mScanFragment = findFragmentByClassName(ScanFragment.class);
            mVideoFragment = findFragmentByClassName(VideoFragment.class);
            mControlFragment = findFragmentByClassName(ControlFragment.class);
            mDetailFragment = findFragmentByClassName(DetailFragment.class);
            mMineFragment = findFragmentByClassName(MineFragment.class);
        }
        /**
         * 开启硬件加速
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        getWindow().setBackgroundDrawable(null);
        bindViews();
        setListener();
        init();
    }

    @Override
    protected void bindViews() {
        mRadioGroup = ((RadioGroup) findViewById(R.id.radioGroup));
    }

    @Override
    protected void setListener() {
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void init() {
        changeTargetFragment(currentSelectedTab);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: 执行了");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 通过类名寻找曾经保存下来的Fragment对象
     *
     * @param type 类名
     * @param <T>  泛型
     * @return 返回Fragment对象
     */
    private <T extends Fragment> T findFragmentByClassName(Class<T> type) {
        return (T) getSupportFragmentManager().findFragmentByTag(type.getSimpleName());
    }

    /**
     * @param contentContainerId
     * @param t
     * @param ft
     * @param <T>
     */
    private <T extends Fragment> void addFragment(int contentContainerId, T t, FragmentTransaction ft) {
        ft.add(contentContainerId, t, t.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        goToTargetFragment(checkedId);
    }

    /**
     * 初始化时调用,跳转到默认首页
     *
     * @param checkedId 默认的RadioButton id
     */
    private void changeTargetFragment(int checkedId) {
        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        if (radioButton.isChecked()) {
            goToTargetFragment(checkedId);
        } else {
            radioButton.setChecked(true);
        }
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mScanFragment != null) {
            ft.hide(mScanFragment);
        }
        if (mVideoFragment != null) {
            ft.hide(mVideoFragment);
        }
        if (mControlFragment != null) {
            ft.hide(mControlFragment);
        }
        if (mDetailFragment != null) {
            ft.hide(mDetailFragment);
        }
        if (mMineFragment != null) {
            ft.hide(mMineFragment);
        }
    }

    /**
     * 跳转到目标Fragment
     *
     * @param checkedId RadioButton 的id
     */
    private void goToTargetFragment(int checkedId) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (checkedId) {
            case tabScan:
                change2Scan(ft);
                break;
            case tabVideo:
                change2Video(ft);
                break;
            case tabControl:
                change2Control(ft);
                break;
            case tabDetails:
                change2Details(ft);
                break;
            case tabMine:
                change2Mine(ft);
                break;
            default:
                break;
        }
    }

    private void change2Scan(FragmentTransaction ft) {
        if (mScanFragment == null) {
            mScanFragment = new ScanFragment();
            addFragment(R.id.content_container, mScanFragment, ft);
        } else {
            ft.show(mScanFragment).commitAllowingStateLoss();
        }
    }

    private void change2Video(FragmentTransaction ft) {
        if (mVideoFragment == null) {
            mVideoFragment = new VideoFragment();
            addFragment(R.id.content_container, mVideoFragment, ft);
        } else {
            ft.show(mVideoFragment).commitAllowingStateLoss();
        }
    }

    private void change2Control(FragmentTransaction ft) {
        if (mControlFragment == null) {
            mControlFragment = new ControlFragment();
            addFragment(R.id.content_container, mControlFragment, ft);
        } else {
            ft.show(mControlFragment).commitAllowingStateLoss();
        }
    }

    private void change2Details(FragmentTransaction ft) {
        if (mDetailFragment == null) {
            mDetailFragment = new DetailFragment();
            addFragment(R.id.content_container, mDetailFragment, ft);
        } else {
            ft.show(mDetailFragment).commitAllowingStateLoss();
        }
    }

    private void change2Mine(FragmentTransaction ft) {
        if (mMineFragment == null) {
            mMineFragment = new MineFragment();
            addFragment(R.id.content_container, mMineFragment, ft);
        } else {
            ft.show(mMineFragment).commitAllowingStateLoss();
        }
    }

    //
    //    /**
    //     * 获得输入的ip地址,正则表达式验证合法性之后启动Activity
    //     */
    //    private void openShowVideoActivity() {
    //        String ipString = mEditText.getText().toString();
    //        String ipRegex = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";
    //        Pattern pattern = Pattern.compile(ipRegex);
    //        Matcher matcher = pattern.matcher(ipString);
    //        if (matcher.find()){
    //            Intent intent = new Intent(this, ShowVideoActivity.class);
    //            Bundle bundle = new Bundle();
    //            bundle.putString("video_url", ipString);
    //            intent.putExtras(bundle);
    //            startActivity(intent);
    //        }else {
    //            ShowToast.shortTime("您输入的ip地址不合法!");
    //        }
    //    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ShowToast.shortTime("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                MyActivityManager.getInstance().finishAllActivityAndExit();
            }
            return true;//此处返回true or false 貌似没有意义了,两个都能正常的退出程序
        }
        return super.onKeyDown(keyCode, event);
    }
}