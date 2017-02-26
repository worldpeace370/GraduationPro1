package com.lebron.graduationpro1.controlpage;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.mvp.factory.RequiresPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 控制页面
 */
@RequiresPresenter(ControlPresenter.class)
public class ControlFragment extends BaseFragment<ControlPresenter> implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    @BindView(R.id.seekBar_water_temp)
    SeekBar mSeekBarWaterTemp;
    @BindView(R.id.seekBar_water_rate)
    SeekBar mSeekBarWaterRate;
    @BindView(R.id.textView_water_temp_MAX)
    TextView mTextViewWaterTemp;
    @BindView(R.id.textView_water_rate_MAX)
    TextView mTextViewWaterRate;

    @BindView(R.id.water_decrease)
    ImageView mButtonWaterDecrease;
    @BindView(R.id.water_increase)
    ImageView mButtonWaterIncrease;
    @BindView(R.id.rate_decrease)
    ImageView mButtonRateDecrease;
    @BindView(R.id.rate_increase)
    ImageView mButtonRateIncrease;
    @BindView(R.id.temp_upload)
    CircularProgressButton mBtnTempUpload;
    @BindView(R.id.rate_upload)
    CircularProgressButton mBtnRateUpload;

    private View mRootView;
    private Unbinder mBind;
    private MainActivity mMainActivity;
    private static final String TAG = "ControlFragment";

    public ControlFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        } else {
            throw new IllegalArgumentException("The context must to be instanceof MainActivity");
        }
        Log.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_control, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        return mRootView;
    }

    @Override
    protected void bindViews(View view) {
        mBind = ButterKnife.bind(this, view);
        initToolbar(view);
        mToolbar.setTitle("");
        mTextViewWaterTemp.setText(mSeekBarWaterTemp.getProgress() + "℃");
        mTextViewWaterRate.setText(mSeekBarWaterRate.getProgress() + "n/s");
    }

    @Override
    protected void setListener() {
        mSeekBarWaterTemp.setOnSeekBarChangeListener(this);
        mSeekBarWaterRate.setOnSeekBarChangeListener(this);
        mButtonWaterDecrease.setOnClickListener(this);
        mButtonWaterIncrease.setOnClickListener(this);
        mButtonRateDecrease.setOnClickListener(this);
        mButtonRateIncrease.setOnClickListener(this);
        mBtnTempUpload.setOnClickListener(this);
        mBtnRateUpload.setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    //下面三个方法是SeekBar拖动回调方法
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(mSeekBarWaterTemp)) {
            mTextViewWaterTemp.setText(progress + "℃");
        } else if (seekBar.equals(mSeekBarWaterRate)) {
            mTextViewWaterRate.setText(progress + "n/s");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.water_decrease:
                mSeekBarWaterTemp.setProgress(mSeekBarWaterTemp.getProgress() - 1);
                break;
            case R.id.water_increase:
                mSeekBarWaterTemp.setProgress(mSeekBarWaterTemp.getProgress() + 1);
                break;
            case R.id.rate_decrease:
                mSeekBarWaterRate.setProgress(mSeekBarWaterRate.getProgress() - 1);
                break;
            case R.id.rate_increase:
                mSeekBarWaterRate.setProgress(mSeekBarWaterRate.getProgress() + 1);
                break;
            case R.id.temp_upload:
                createTempUploadDialog();
                break;
            case R.id.rate_upload:
                createRateUploadDialog();
                break;
        }
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                }
            }
        });
        widthAnimation.start();
    }

    private void createTempUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("确认提交上传数据吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleTempUpload();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void createRateUploadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("确认提交上传数据吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleRateUpload();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void handleTempUpload() {
        if (mBtnTempUpload.getProgress() == 0) {
            simulateSuccessProgress(mBtnTempUpload);
        } else {
            mBtnTempUpload.setProgress(0);
        }
    }

    private void handleRateUpload() {
        if (mBtnRateUpload.getProgress() == 0) {
            simulateSuccessProgress(mBtnRateUpload);
        } else {
            mBtnRateUpload.setProgress(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
