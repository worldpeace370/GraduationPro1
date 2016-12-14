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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.ui.fragment.ControlFragment;
import com.lebron.graduationpro1.ui.fragment.DetailFragment;
import com.lebron.graduationpro1.ui.fragment.ScanFragment;
import com.lebron.graduationpro1.ui.fragment.VideoFragment;
import com.lebron.graduationpro1.utils.ConstantValue;
import com.lebron.graduationpro1.utils.MyActivityManager;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.AddPopWindow;
import com.lebron.graduationpro1.view.DragLayout;
import com.lebron.graduationpro1.view.MainLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.id;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    private static final String TAG = "MainActivity";
    @BindView(R.id.drawer_layout)
    DragLayout mDragLayout;
    @BindView(R.id.main_linearLayout)
    MainLinearLayout mMainLinearLayout;
    @BindView(R.id.image_head_main)
    CircleImageView mImageView;
    @BindView(R.id.tab_title)
    TextView mTextViewTitle;
    @BindView(R.id.add_menu)
    ImageView mImageViewAddMenu;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
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
    //节点名字,放置到mTextViewTitle中
    private String mNodeName = "";

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

    /**
     * 通过类名寻找曾经保存下来的Fragment对象
     * @param type 类名
     * @param <T> 泛型
     * @return 返回Fragment对象
     */
    private <T extends Fragment> T findFragmentByClassName(Class<T> type) {
        return (T) getSupportFragmentManager().findFragmentByTag(type.getSimpleName());
    }

    /**
     *
     * @param id
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

    private void changeTargetFragment(int checkedId){
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
            mTextViewTitle.setText(mNodeName);
            mImageViewAddMenu.setVisibility(View.VISIBLE);
        }
    }

    private void change2Video(FragmentTransaction ft) {
        if (mVideoFragment == null) {
            mVideoFragment = new VideoFragment();
            addFragment(R.id.content_container, mVideoFragment, ft);
        } else {
            ft.show(mVideoFragment).commitAllowingStateLoss();
            mTextViewTitle.setText("监控视频");
            mImageViewAddMenu.setVisibility(View.INVISIBLE);
        }
    }

    private void change2Control(FragmentTransaction ft) {
        if (mControlFragment == null) {
            mControlFragment = new ControlFragment();
            addFragment(R.id.content_container, mControlFragment, ft);
        } else {
            ft.show(mControlFragment).commitAllowingStateLoss();
            mTextViewTitle.setText("远程控制");
            mImageViewAddMenu.setVisibility(View.INVISIBLE);
        }
    }

    private void change2Details(FragmentTransaction ft) {
        if (mDetailFragment == null) {
            mDetailFragment = new DetailFragment();
            addFragment(R.id.content_container, mDetailFragment, ft);
        } else {
            ft.show(mDetailFragment).commitAllowingStateLoss();
            mTextViewTitle.setText("详情日志");
            mImageViewAddMenu.setVisibility(View.INVISIBLE);
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
                //关闭抽屉的时候让头像晃动
                ObjectAnimator animator = ObjectAnimator.ofFloat(mImageView, "translationX", 12.0f);
                animator.setInterpolator(new CycleInterpolator(4.0f));
                animator.setDuration(500);
                animator.start();
            }

            @Override
            public void onOpened() {
                //do nothing
            }

            @Override
            public void onDragging(float percent) {
                //设置在拖拽过程中的头像透明度渐变效果
                mImageView.setAlpha(1 - percent);
            }
        });
        mMainLinearLayout.setDragLayout(mDragLayout);
        changeTargetFragment(currentSelectedTab);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 切换Fragment,可以当前Activity调用,也可以在attach的Fragment中调用
     * @param containerId 当前FrameLayout的布局id
     * @param fragment 替换到当前布局中的Fragment
     */
    public void replaceFragment(int containerId, Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.commit();
    }

    @Override
    protected void initData() {
        //貌似没有卵用,将所有activity加入链表队列中,有的activity需要销毁的时候反而不能销毁
        MyActivityManager.getInstance().addActivity(this);
    }

    public void click(View view){
        int id = view.getId();
        switch (id){
            case R.id.image_head:
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
            case R.id.image_head_main:
                //打开抽屉布局
                mDragLayout.openDrag();
                break;
            case R.id.add_menu:
                //弹出PopWindow,选择新节点or保存当前节点图到内存卡
                showPopWindowAndDealEvent();
                break;
        }
    }

    /**
     * 弹出PopupWindow,然后执行对应的操作
     */
    private void showPopWindowAndDealEvent(){
        AddPopWindow addPopWindow = new AddPopWindow(this);
        addPopWindow.showPopupWindow(mImageViewAddMenu);
        addPopWindow.setOnPopupWindowItemClickListener(new AddPopWindow.OnPopupWindowItemClickListener() {
            @Override
            public void onItemClick(int id) {
                if (id == R.id.select_new_node){
                    startNodeChoiceActivity();
                }else if (id == R.id.save_image_sd_card){
                    //保存折线图到SD卡
                    ScanFragment fragment = (ScanFragment) getSupportFragmentManager().findFragmentById(R.id.content_container);
                    fragment.saveLineChartToSDCard();
                }else if (id == R.id.refresh_data){
                    ScanFragment fragment = (ScanFragment) getSupportFragmentManager().findFragmentById(R.id.content_container);
                    fragment.refreshData();
                }
            }
        });
    }

    /**
     * 跳转到节点选择的Activity中去
     */
    private void startNodeChoiceActivity() {
        //跳转到供暖节点选择Activity
        Intent intent = new Intent(MainActivity.this, NodeChoiceActivity.class);
        startActivityForResult(intent, ConstantValue.NODE_CHOICE_REQUEST_CODE);
        //Activity启动动画
        MainActivity.this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }
    //得到选择的节点,设置到mTextViewTitle中
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获得 mNodeName
        if (requestCode == ConstantValue.NODE_CHOICE_REQUEST_CODE && resultCode == ConstantValue.NODE_CHOICE_RESULT_CODE){
            if (data != null){
                mNodeName = data.getStringExtra("nodeName");
                mTextViewTitle.setText(mNodeName);
            }
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
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            if (System.currentTimeMillis() - mExitTime > 2000){
                ShowToast.shortTime("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            }else {
                MyActivityManager.getInstance().finishAllActivityAndExit();
            }
            return true;//此处返回true or false 貌似没有意义了,两个都能正常的退出程序
        }
        return super.onKeyDown(keyCode, event);
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
}
