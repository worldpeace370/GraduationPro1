package com.lebron.graduationpro1.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.beans.LineChartTestData;
import com.lebron.graduationpro1.interfaces.RequestFinishedListener;
import com.lebron.graduationpro1.net.VolleyRequest;
import com.lebron.graduationpro1.ui.activity.MainActivity;
import com.lebron.graduationpro1.utils.ConstantValue;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.MyMarkerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements RequestFinishedListener, SeekBar.OnSeekBarChangeListener
, View.OnClickListener{
    @BindView(R.id.latest_day)
    Button mButtonDay;
    @BindView(R.id.latest_week)
    Button mButtonWeek;
    @BindView(R.id.latest_month)
    Button mButtonMonth;
    @BindView(R.id.latest_year)
    Button mButtonYear;
    @BindView(R.id.lineChart1)
    LineChart mLineChart;
    @BindView(R.id.seekBar_X)
    SeekBar mSeekBarX;
    @BindView(R.id.seekBar_Y)
    SeekBar mSeekBarY;
    @BindView(R.id.textView_X_MAX)
    TextView mTextViewX;
    @BindView(R.id.textView_Y_MAX)
    TextView mTextViewY;

    private Unbinder mUnbinder;

    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";
    private String mParam1;

    private String mParam2;
    private MainActivity mMainActivity;

    public ScanFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null){
            mMainActivity = (MainActivity) getActivity();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        VolleyRequest volleyRequest = new VolleyRequest();
        volleyRequest.getJsonFromServer(ConstantValue.TESTURL, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViewAndListener();
        return view;
    }

    private void initViewAndListener() {
        mButtonDay.setBackgroundColor(Color.parseColor("#b4afaf"));
        mButtonDay.setOnClickListener(this);
        mButtonWeek.setOnClickListener(this);
        mButtonMonth.setOnClickListener(this);
        mButtonYear.setOnClickListener(this);

        mSeekBarX.setProgress(49);
        mSeekBarY.setProgress(100);
        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mLineChart.setDescription("哈哈");//右下角说明
        mLineChart.setNoDataTextDescription("You need to provide data for the chart");
        mLineChart.setTouchEnabled(true);//enable touch gesture
        mLineChart.setDragEnabled(true);//enable scaling and dragging
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);

        //create a custom MarkerView (extends MarkerView) and specify the layout
        MyMarkerView markerView = new MyMarkerView(mMainActivity, R.layout.custom_marker_view);
        mLineChart.setMarkerView(markerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLineChartData();
    }

    private void initLineChartData() {
        LineChartTestData[] datas = new LineChartTestData[10];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = new LineChartTestData(i, i);
        }
        List<Entry> entries = new ArrayList<>();
        for (LineChartTestData data:datas) {
            entries.add(new Entry(data.getxValue(), data.getyValue()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "温度");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.RED);
        String xVals[] = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月"};
        LineData lineData = new LineData(xVals, dataSet);
        mLineChart.setData(lineData);

        //获取Y轴
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return value + "℃";
            }
        });
        LimitLine limitLine = new LimitLine(4, "Top");
        limitLine.setLineColor(Color.RED);
        limitLine.setLineWidth(4f);
        limitLine.setTextColor(Color.BLACK);
        limitLine.setTextSize(12f);
        leftAxis.addLimitLine(limitLine);
        mLineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onRequestSucceed(String responseString) {

    }

    @Override
    public void onRequestError(String errorString) {

    }

    /**
     * SeekBar三个拖动接口相关方法
     * onProgressChanged
     * onStartTrackingTouch
     * onStopTrackingTouch
     */

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
            case R.id.latest_day:
                ShowToast.shortTime("近一天");
                mButtonDay.setBackgroundColor(Color.parseColor("#b4afaf"));
                mButtonWeek.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonMonth.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonYear.setBackgroundColor(Color.parseColor("#dfdbdb"));
                break;
            case R.id.latest_week:
                ShowToast.shortTime("近一周");
                mButtonWeek.setBackgroundColor(Color.parseColor("#b4afaf"));
                mButtonDay.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonMonth.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonYear.setBackgroundColor(Color.parseColor("#dfdbdb"));
                break;
            case R.id.latest_month:
                ShowToast.shortTime("近一月");
                mButtonMonth.setBackgroundColor(Color.parseColor("#b4afaf"));
                mButtonDay.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonWeek.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonYear.setBackgroundColor(Color.parseColor("#dfdbdb"));
                break;
            case R.id.latest_year:
                ShowToast.shortTime("近一年");
                mButtonYear.setBackgroundColor(Color.parseColor("#b4afaf"));
                mButtonDay.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonWeek.setBackgroundColor(Color.parseColor("#dfdbdb"));
                mButtonMonth.setBackgroundColor(Color.parseColor("#dfdbdb"));
                break;
        }
    }
}
