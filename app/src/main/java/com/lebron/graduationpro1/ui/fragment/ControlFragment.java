package com.lebron.graduationpro1.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lebron.graduationpro1.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ControlFragment extends Fragment implements SeekBar.OnSeekBarChangeListener{
    @BindView(R.id.seekBar_water_temp)
    SeekBar mSeekBarWaterTemp;
    @BindView(R.id.seekBar_water_rate)
    SeekBar mSeekBarWaterRate;
    @BindView(R.id.textView_water_temp_MAX)
    TextView mTextViewWaterTemp;
    @BindView(R.id.textView_water_rate_MAX)
    TextView mTextViewWaterRate;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Unbinder mBind;

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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
}
