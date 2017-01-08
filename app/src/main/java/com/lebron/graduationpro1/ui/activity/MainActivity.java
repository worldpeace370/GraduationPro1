package com.lebron.graduationpro1.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.CycleInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.ui.fragment.ControlFragment;
import com.lebron.graduationpro1.ui.fragment.DetailFragment;
import com.lebron.graduationpro1.ui.fragment.ScanFragment;
import com.lebron.graduationpro1.ui.fragment.VideoFragment;
import com.lebron.graduationpro1.utils.MyActivityManager;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.DragLayout;
import com.lebron.graduationpro1.view.MainLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.drawer_layout)
    DragLayout mDragLayout;
    @BindView(R.id.main_linearLayout)
    MainLinearLayout mMainLinearLayout;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.image_head_left)
    CircleImageView mImageHeadLeft;

    private final int TAB_SCAN = R.id.scan;
    private final int TAB_VIDEO = R.id.video;
    private final int TAB_CONTROL = R.id.control;
    private final int TAB_DETAILS = R.id.details;
    private int currentSelectedTab = R.id.scan;
    private ScanFragment mScanFragment;
    private VideoFragment mVideoFragment;
    private ControlFragment mControlFragment;
    private DetailFragment mDetailFragment;
    private FragmentManager mFragmentManager;
    //用于按下两次返回键退出程序用
    private long mExitTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mScanFragment = findFragmentByClassName(ScanFragment.class);
            mVideoFragment = findFragmentByClassName(VideoFragment.class);
            mControlFragment = findFragmentByClassName(ControlFragment.class);
            mDetailFragment = findFragmentByClassName(DetailFragment.class);
        }
        /**
         * 开启硬件加速
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        getWindow().setBackgroundDrawable(null);
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
            case TAB_SCAN:
                change2Scan(ft);
                break;
            case TAB_VIDEO:
                change2Video(ft);
                break;
            case TAB_CONTROL:
                change2Control(ft);
                break;
            case TAB_DETAILS:
                change2Details(ft);
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        mDragLayout.setDragStatusChangedListener(new DragLayout.OnDragStatusChangedListener() {
            @Override
            public void onClosed() {

            }

            @Override
            public void onOpened() {
                //打开抽屉的时候让头像晃动
                ObjectAnimator animator = ObjectAnimator.ofFloat(mImageHeadLeft, "translationX", 12.0f);
                animator.setInterpolator(new CycleInterpolator(4.0f));
                animator.setDuration(500);
                animator.start();
            }

            @Override
            public void onDragging(float percent) {
                //设置在拖拽过程中的头像透明度渐变效果
                mImageHeadLeft.setAlpha(percent);
            }
        });
        mMainLinearLayout.setDragLayout(mDragLayout);
        changeTargetFragment(currentSelectedTab);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {

    }

    public void click(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.image_head_left:
                ShowToast.shortTime("image_head");
                break;
            case R.id.nick_name:
                ShowToast.shortTime("nick_name");
                break;
            case R.id.my_device:
                ShowToast.shortTime("my_device");
                break;
            case R.id.my_collect:
                ShowToast.shortTime("my_collect");
                break;
            case R.id.my_note:
                ShowToast.shortTime("my_note");
                break;
            case R.id.my_contracts:
                ShowToast.shortTime("my_contracts");
                break;
            case R.id.my_data:
                ShowToast.shortTime("my_data");
                break;
            case R.id.exit:
                ShowToast.shortTime("exit");
                break;
            case R.id.settings:
                //进入设置Activity
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.night_mode:
                ShowToast.shortTime("night_mode");
                break;
            default:
                break;
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
