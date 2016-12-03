package com.lebron.graduationpro1.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.ui.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{
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
    @BindView(R.id.water_post)
    Button mButtonWaterPost;
    @BindView(R.id.rate_post)
    Button mButtonRatePost;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Unbinder mBind;
    private MainActivity mMainActivity;
    private String TAG = "ControlFragment";

    public ControlFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ControlFragment.
     */
    public static ControlFragment newInstance(String param1, String param2) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mMainActivity = (MainActivity) getActivity();
        }else {
            throw new IllegalArgumentException("The context must to be instanceof MainActivity");
        }
        Log.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        mBind = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mTextViewWaterTemp.setText(mSeekBarWaterTemp.getProgress() + "℃");
        mTextViewWaterRate.setText(mSeekBarWaterRate.getProgress() + "n/s");
        mSeekBarWaterTemp.setOnSeekBarChangeListener(this);
        mSeekBarWaterRate.setOnSeekBarChangeListener(this);
        mButtonWaterDecrease.setOnClickListener(this);
        mButtonWaterIncrease.setOnClickListener(this);
        mButtonRateDecrease.setOnClickListener(this);
        mButtonRateIncrease.setOnClickListener(this);
        mButtonWaterPost.setOnClickListener(this);
        mButtonRatePost.setOnClickListener(this);
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
    //下面三个方法是SeekBar拖动回调方法
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.equals(mSeekBarWaterTemp)){
            mTextViewWaterTemp.setText(progress + "℃");
        }else if (seekBar.equals(mSeekBarWaterRate)){
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
        switch (id){
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
            case R.id.water_post:
                createDialog();
                break;
            case R.id.rate_post:
                createDialog();
                break;
        }
    }

    private void createDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
        builder.setTitle("确认提交吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
}
