package com.lebron.graduationpro1.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.beans.LineChartTestData;
import com.lebron.graduationpro1.interfaces.RequestFinishedListener;
import com.lebron.graduationpro1.net.VolleyRequest;
import com.lebron.graduationpro1.ui.activity.MainActivity;
import com.lebron.graduationpro1.utils.ConstantValue;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.MyMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private Unbinder mUnbinder;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final String TAG = "ScanFragment";
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
        VolleyRequest volleyRequest = new VolleyRequest();
        volleyRequest.getJsonFromServer(ConstantValue.TESTURL, this);
        Log.i(TAG, "onCreate: 执行了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViewAndListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLineChartData();
        //宽高为0
        Log.i("ScanFragment", "onActivityCreated：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());
    }

    @Override
    public void onStart() {
        super.onStart();
        //宽高为0
        Log.i("ScanFragment", "onStart：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());
    }

    @Override
    public void onResume() {
        super.onResume();
        //宽高为0
        Log.i("ScanFragment", "onResume：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());
    }


    @Override
    public void onPause() {
        super.onPause();
        //宽高不为0
        Log.i("ScanFragment", "onPause：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());

    }


    @Override
    public void onStop() {
        super.onStop();
        //宽高不为0
        Log.i("ScanFragment", "onStop：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());
    }

    public void refreshData(){
        mRefreshLayout.setRefreshing(true);
    }

    private void initViewAndListener() {
        //原生下拉刷新控件
        mRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //重新加载数据,绘制图形
                initLineChartData();
            }
        });


        mButtonDay.setBackgroundColor(Color.parseColor("#b4afaf"));
        mButtonDay.setOnClickListener(this);
        mButtonWeek.setOnClickListener(this);
        mButtonMonth.setOnClickListener(this);
        mButtonYear.setOnClickListener(this);
        mSeekBarX.setProgress(49);
        mSeekBarY.setProgress(40);
        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mLineChart.setDescription("节点信息");//右下角说明
        mLineChart.setNoDataTextDescription("You need to provide data for the chart");
        mLineChart.setTouchEnabled(true);//enable touch gesture
        mLineChart.setDragEnabled(true);//enable scaling and dragging
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.setDoubleTapToZoomEnabled(true);
        //create a custom MarkerView (extends MarkerView) and specify the layout
        MyMarkerView markerView = new MyMarkerView(mMainActivity, R.layout.custom_marker_view);
        mLineChart.setMarkerView(markerView);
        //初始化X,Y轴
        initAxis(mLineChart);
    }


    private void initLineChartData() {
        ArrayList<LineDataSet> dataSetList = new ArrayList<>();
        dataSetList.add(initTemperatureLine(new ArrayList<LineChartTestData>()));
        dataSetList.add(initFlowLine(new ArrayList<LineChartTestData>()));
        String xVals[] = new String[]{"0h","1h","2h","3h","4h","5h","6h","7h","8h","9h","10h","11h","12h"
        , "13h","14h","15h","16h","17h","18h","19h","20h","21h","22h","23h"};
        LineData lineData = new LineData(xVals, dataSetList);
        mLineChart.setData(lineData);
        //X方向动画效果
        mLineChart.animateX(1500, Easing.EasingOption.EaseInOutQuart);
        //X,Y方向同时动画
//        mLineChart.animateXY(3000, 3000);
        mLineChart.invalidate();
    }

    /**
     * 设置x,y轴和Legend显示的属性
     */
    private void initAxis(LineChart lineChart) {
        //获取右侧Y轴
        YAxis rightAxis = lineChart.getAxisRight();
        //取消右侧Y轴显示
        rightAxis.setEnabled(false);
        //获取左侧Y轴
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置网格线虚线模式
        leftAxis.enableGridDashedLine(2f, 3f, 0);
        //设置Y轴最大值
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setDrawLimitLinesBehindData(true);
        //自定义格式的标签,显示温度等
//        leftAxis.setValueFormatter(new YAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, YAxis yAxis) {
//                return value + "℃";
//            }
//        });
        LimitLine limitLineWater = new LimitLine(70, "水温阈值");
        limitLineWater.setLineColor(Color.RED);
        limitLineWater.setLineWidth(1f);
        limitLineWater.setTextColor(Color.BLACK);
        limitLineWater.setTextSize(12f);
        limitLineWater.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        //设置虚线
//        limitLineWater.enableDashedLine(6f, 10f, 0f);
        leftAxis.addLimitLine(limitLineWater);
        LimitLine limitLineRotateSpeed = new LimitLine(40, "转速阈值");
        limitLineRotateSpeed.setLineColor(Color.GREEN);
        limitLineRotateSpeed.setLineWidth(1f);
        limitLineRotateSpeed.setTextColor(Color.BLACK);
        limitLineRotateSpeed.setTextSize(12f);
        limitLineRotateSpeed.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //设置虚线
//        limitLineRotateSpeed.enableDashedLine(6f, 10f, 0f);
        leftAxis.addLimitLine(limitLineRotateSpeed);
        //X轴设置在底部
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置网格线虚线模式
        xAxis.enableGridDashedLine(2f, 3f, 0f);
        //设置标签字符间的空隙,默认是4字符间距
        xAxis.setSpaceBetweenLabels(2);
        //设置在"绘制下一个标签时",要忽略的标签数
        xAxis.setLabelsToSkip(1);
        //避免剪掉x轴上第一个和最后一个坐标项
        xAxis.setAvoidFirstLastClipping(true);
        //设置Legend,左下角的东西,可以是横线,方形
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
//        legend.setTextColor(Color.RED);
    }

    /**
     * 初始化水温折线
     * @param list 传入的数据,网络下载
     * @return 返回LineDataSet
     */
    private LineDataSet initTemperatureLine(ArrayList<LineChartTestData> list){
        LineChartTestData[] datas = new LineChartTestData[24];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = new LineChartTestData(i, 80 + (int) (Math.random() * 10));
        }
        List<Entry> entries = new ArrayList<>();
        for (LineChartTestData data:datas) {
            entries.add(new Entry(data.getyValue(), data.getxValue()));//左:Y值,右:X值
        }

        //LineDataSet可以看作是一条线
        LineDataSet dataSet = new LineDataSet(entries, "水温℃");
        //设置折线的颜色
        dataSet.setColor(Color.RED);
        //设置折线上点的字体颜色
        dataSet.setValueTextColor(Color.RED);
        //设置折线圆点的颜色
        dataSet.setCircleColor(Color.RED);
        //图上不描述点的值
        dataSet.setDrawValues(false);
        //设置折线圆点中心的颜色
        //        dataSet.setCircleColorHole(Color.GRAY);
        //设置圆点的大小
        dataSet.setCircleSize(4f);
        //设置点击某个点时,横竖两条线的颜色
        dataSet.setHighLightColor(Color.YELLOW);
        return dataSet;
    }

    /**
     * 初始化转速折线
     * @param list 传入的数据,网络下载
     * @return 返回LineDataSet
     */
    private LineDataSet initFlowLine(ArrayList<LineChartTestData> list){
        LineChartTestData[] datas = new LineChartTestData[24];
        for (int i = 0; i < datas.length; i++) {
            datas[i] = new LineChartTestData(i, 50 + (int) (Math.random() * 10));
        }
        List<Entry> entries = new ArrayList<>();
        for (LineChartTestData data:datas) {
            entries.add(new Entry(data.getyValue(), data.getxValue()));//左:Y值,右:X值
        }

        //LineDataSet可以看作是一条线
        LineDataSet dataSet = new LineDataSet(entries, "转速n/s");
        //设置折线的颜色
        dataSet.setColor(Color.GREEN);
        //设置折线上点的字体颜色
        dataSet.setValueTextColor(Color.GREEN);
        //设置折线圆点的颜色
        dataSet.setCircleColor(Color.GREEN);
        //设置折线圆点中心的颜色
        //        dataSet.setCircleColorHole(Color.GRAY);
        //设置圆点的大小
        dataSet.setCircleSize(4f);
        //设置点击某个点时,横竖两条线的颜色
        dataSet.setHighLightColor(Color.YELLOW);
        dataSet.setDrawValues(false);
        return dataSet;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //宽高不为0
        Log.i("ScanFragment", "onDestroyView：mLineChart.getWidth() = " + mLineChart.getWidth()
                + ",mLineChart.getHeight() = " + mLineChart.getHeight());
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void saveLineChartToSDCard(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = format.format(date);
        if (mLineChart != null){
            if (mLineChart.saveToGallery("lineChart-" + dateString + ".jpg", 100)){
                ShowToast.shortTime("保存成功!");
            }else {
                ShowToast.shortTime("保存失败!");
            }
        }else {
            Log.i("ScanFragment", "saveLineChartToSDCard: mLineChart is null");
            ShowToast.shortTime("mLineChart is null!");
        }
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
