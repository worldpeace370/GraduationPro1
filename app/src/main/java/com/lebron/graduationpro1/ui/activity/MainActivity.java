package com.lebron.graduationpro1.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.utils.ConstantValue;
import com.lebron.graduationpro1.utils.MyActivityManager;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.DragLayout;
import com.lebron.graduationpro1.view.MainLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity{
    private static final String TAG = "MainActivity";
    @BindView(R.id.drawer_layout)
    DragLayout mDragLayout;
    @BindView(R.id.main_linearLayout)
    MainLinearLayout mMainLinearLayout;
    @BindView(R.id.tab_title)
    TextView mTextViewTitle;
    @BindView(R.id.image_head_main)
    CircleImageView mImageView;
    //用于按下两次返回键退出程序用
    private long mExitTime;
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
                ShowToast.shortTime("opened");
            }

            @Override
            public void onDragging(float percent) {
                //设置在拖拽过程中的头像透明度渐变效果
                mImageView.setAlpha(1 - percent);
            }
        });
        mMainLinearLayout.setDragLayout(mDragLayout);
        //跳转到供暖节点选择Activity
        mTextViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到供暖节点选择Activity
                Intent intent = new Intent(MainActivity.this, NodeChoiceActivity.class);
                startActivityForResult(intent, ConstantValue.NODE_CHOICE_CODE);
                //Activity启动动画
                MainActivity.this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
            }
        });
    }

    @Override
    protected void initData() {
        MyActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
}
