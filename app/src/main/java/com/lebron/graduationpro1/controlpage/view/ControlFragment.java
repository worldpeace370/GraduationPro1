package com.lebron.graduationpro1.controlpage.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.controlpage.contracts.ControlContracts;
import com.lebron.graduationpro1.controlpage.presenter.ControlPresenter;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.service.VolleyRequestService;
import com.lebron.mvp.factory.RequiresPresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 控制页面
 */
@RequiresPresenter(ControlPresenter.class)
public class ControlFragment extends BaseFragment<ControlPresenter> implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener, ControlContracts.View {
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
    private static final String INFO_TYPE_1 = "temperature";
    private static final String INFO_TYPE_2 = "rate";
    private static final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // HH 24小时制
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
        mToolbar.inflateMenu(R.menu.menu_control_fragment);
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
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_revert:
                        mBtnRateUpload.setProgress(0);
                        mBtnTempUpload.setProgress(0);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化SeekBar上面的温度值和转速值，设置label(TextView显示的温度、转速)
     * 从服务器读取最新的数据(此处不涉及到具体的交互只是初始化操作，没有用MVP实现)
     */
    @Override
    protected void init() {
        String url = "http://114.215.117.169/thinkphp/Home/ApiGrad/dateSearch/date/%s";
        Date date = new Date(System.currentTimeMillis());
        String dateStr = mDateFormat.format(date);
        new VolleyRequestService().getDataFromServer(String.format(url, dateStr), CollectInfoBean.class,
                new VolleyRequestService.RequestCompleteListener<CollectInfoBean>() {
                    @Override
                    public void success(List<CollectInfoBean> list) {
                        if (list != null && list.size() > 0) {
                            String temperature = list.get(list.size() - 1).getTemperature();
                            String rate = list.get(list.size() - 1).getRate();
                            mTextViewWaterTemp.setText(temperature + "℃");
                            mSeekBarWaterTemp.setProgress((int)Float.parseFloat(temperature));
                            mTextViewWaterRate.setText(rate + "n/s");
                            mSeekBarWaterRate.setProgress((int)Float.parseFloat(rate));//上传温度大部分为小数，之前转为Integer报了异常
                        }
                    }

                    @Override
                    public void error(String error) {

                    }
                });
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
                createUploadDialog(mBtnTempUpload, mSeekBarWaterTemp, INFO_TYPE_1);
                break;
            case R.id.rate_upload:
                createUploadDialog(mBtnRateUpload, mSeekBarWaterRate, INFO_TYPE_2);
                break;
        }
    }

    @Override
    public void showUploadSuccess(final String infoType) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                if (infoType.equals(INFO_TYPE_1)) {
                    mBtnTempUpload.setProgress(value);
                } else if(infoType.equals(INFO_TYPE_2)){
                    mBtnRateUpload.setProgress(value);
                }
            }
        });
        widthAnimation.start();
    }

    @Override
    public void showUploadFail(int retCode, String retDesc) {
        showCustomToast(R.mipmap.input_clean, retDesc);
    }

    private void createUploadDialog(final CircularProgressButton button, final SeekBar seekBar,
                                    final String infoType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("确认提交上传数据吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (button.getProgress() == 0) {
                    getPresenter().uploadControlInfo(infoType, seekBar.getProgress() + "");
                } else {
                    button.setProgress(0);
                }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) { // 当该页面再次可见的时候，hidden = false，重置上传按键的进度状态
            mBtnRateUpload.setProgress(0);
            mBtnTempUpload.setProgress(0);
            init();
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
