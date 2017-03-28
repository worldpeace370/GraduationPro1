package com.lebron.graduationpro1.scanpage.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lebron.graduationpro1.base.AppApplication;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.graduationpro1.scanpage.contracts.ScanContracts;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.scanpage.presenter.ScanPresenter;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.graduationpro1.utils.ConstantValue;
import com.lebron.graduationpro1.utils.LebronPreference;
import com.lebron.graduationpro1.utils.ShowToast;
import com.lebron.graduationpro1.view.AddPopWindow;
import com.lebron.graduationpro1.view.MyMarkerView;
import com.lebron.graduationpro1.view.customcalendarview.CalendarTools;
import com.lebron.graduationpro1.view.customcalendarview.CustomCalendarView;
import com.lebron.mvp.factory.RequiresPresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.lebron.graduationpro1.R.id.lineChart;

/**
 * 概览界面
 */
@RequiresPresenter(ScanPresenter.class)
public class ScanFragment extends BaseFragment<ScanPresenter>
        implements View.OnClickListener, ScanContracts.View
        , CustomCalendarView.OnDateSelectedListener, CustomCalendarView.OnMonthChangedListener {
    public final static int POSITION_FIRST_DAY = 1;
    public final static int POSITION_SECOND_DAY = 2;
    public final static int POSITION_THIRD_DAY = 3;

    private static final int COLOR_SELECTED = ContextCompat.getColor(AppApplication.getAppContext()
            , R.color.toolBarBackground);
    private static final int COLOR_NOT_SELECTED = ContextCompat.getColor(AppApplication.getAppContext()
            , R.color.color_std_light_black);
    private static final SimpleDateFormat SDF_M_D = new SimpleDateFormat("MM.dd", Locale.getDefault());
    private LineChart mLineChart;
    private RelativeLayout mShowSelectDateLayout; // 日历选择提示图标
    private LinearLayout mLayoutDay1, mLayoutDay2, mLayoutDay3;
    private TextView mTvWeek1, mTvDay1, mTvWeek2, mTvDay2, mTvWeek3, mTvDay3;
    private PopupWindow mSelectDateWindow;
    private TextView mTvBackToday;
    //节点名字,放置到mTextViewTitle中
    private String mNodeName;

    private static final String TAG = "ScanFragment";
    private MainActivity mMainActivity;
    private View mRootView;
    private CustomCalendarView mCalendarView;

    public ScanFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        } else {
            throw new IllegalArgumentException("The context must to be instanceof MainActivity");
        }
        AppLog.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNodeName = LebronPreference.getInstance().getNodeChoice();
        if (mNodeName.equals("")) {
            mNodeName = "朝阳区节点1";
        }
        AppLog.i(TAG, "onCreate: 执行了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_scan, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        AppLog.i(TAG, "onCreateView: 执行了");
        return mRootView;
    }

    @Override
    protected void bindViews(View view) {
        initToolBarUI(view);
        initSelectDateUI(view);
        initLineChartUI(view);
        initAxisUI();
        initNoStandardUI(view);
    }

    /**
     * findViewById 日期切换那块的View
     *
     * @param view mRootView
     */
    private void initSelectDateUI(View view) {
        mShowSelectDateLayout = ((RelativeLayout) view.findViewById(R.id.show_select_date_layout));
        mLayoutDay1 = (LinearLayout) view.findViewById(R.id.layout_day1);
        mLayoutDay2 = (LinearLayout) view.findViewById(R.id.layout_day2);
        mLayoutDay3 = (LinearLayout) view.findViewById(R.id.layout_day3);
        mTvWeek1 = (TextView) view.findViewById(R.id.tv_week1);
        mTvWeek2 = (TextView) view.findViewById(R.id.tv_week2);
        mTvWeek3 = (TextView) view.findViewById(R.id.tv_week3);
        mTvDay1 = (TextView) view.findViewById(R.id.tv_day1);
        mTvDay2 = (TextView) view.findViewById(R.id.tv_day2);
        mTvDay3 = (TextView) view.findViewById(R.id.tv_day3);
    }

    /**
     * 初始化 ToolBar 相关UI,title 和 menu
     *
     * @param view mRootView
     */
    private void initToolBarUI(View view) {
        initToolbar(view);
        mToolbar.setTitle(mNodeName);
        mToolbar.setTitleTextAppearance(mMainActivity, R.style.ToolBarTitleAppearance);
        mToolbar.inflateMenu(R.menu.menu_scan_fragment);
    }

    /**
     * 初始化LineChart的相关视觉属性
     */
    private void initLineChartUI(View view) {
        mLineChart = ((LineChart) view.findViewById(lineChart));
        mLineChart.setDescription("节点信息");//右下角说明
        mLineChart.setNoDataTextDescription("You need to provide data for the chart");
        mLineChart.setTouchEnabled(true);//enable touch gesture
        mLineChart.setDragEnabled(true);//enable scaling and dragging
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.setDoubleTapToZoomEnabled(true);
        //create a custom MarkerView (extends MarkerView) and specify the layout
        mLineChart.setMarkerView(new MyMarkerView(mMainActivity, R.layout.custom_marker_view));
    }

    /**
     * 设置x,y轴和Legend显示的属性
     */
    private void initAxisUI() {
        //获取右侧Y轴
        YAxis rightAxis = mLineChart.getAxisRight();
        //取消右侧Y轴显示
        rightAxis.setEnabled(false);
        //获取左侧Y轴
        YAxis leftAxis = mLineChart.getAxisLeft();
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
        //limitLineWater.enableDashedLine(6f, 10f, 0f);
        leftAxis.addLimitLine(limitLineWater);
        LimitLine limitLineRotateSpeed = new LimitLine(40, "转速阈值");
        limitLineRotateSpeed.setLineColor(Color.GREEN);
        limitLineRotateSpeed.setLineWidth(1f);
        limitLineRotateSpeed.setTextColor(Color.BLACK);
        limitLineRotateSpeed.setTextSize(12f);
        limitLineRotateSpeed.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        //设置虚线
        //limitLineRotateSpeed.enableDashedLine(6f, 10f, 0f);
        leftAxis.addLimitLine(limitLineRotateSpeed);
        //X轴设置在底部
        XAxis xAxis = mLineChart.getXAxis();
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
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        //legend.setTextColor(Color.RED);
    }

    @Override
    protected void setListener() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        getPresenter().showAddMenuWindow();
                        break;
                }
                return false;
            }
        });
        mShowSelectDateLayout.setOnClickListener(this);
        mLayoutDay1.setOnClickListener(this);
        mLayoutDay2.setOnClickListener(this);
        mLayoutDay3.setOnClickListener(this);
        mLayoutError.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_select_date_layout: //显示
                getPresenter().showSelectDateWindow();
                break;
            case R.id.iv_hide_select_date: //隐藏
            case R.id.layout_pop_window_bottom:
                dismissSelectDateWindow();
                break;
            case R.id.tv_back_today:
                getPresenter().backToToday();
                break;
            case R.id.layout_day1:
                getPresenter().changeToFirstPositionDay();
                break;
            case R.id.layout_day2:
                getPresenter().changeToSecondPositionDay();
                break;
            case R.id.layout_day3:
                getPresenter().changeToThirdPositionDay();
                break;
            case R.id.layout_common_network_error:
                getPresenter().initTodayData();
                break;
            default:
                break;
        }
    }

    /**
     * 让日历选择窗体消失
     */
    private void dismissSelectDateWindow() {
        if (mSelectDateWindow != null && mSelectDateWindow.isShowing()) {
            mSelectDateWindow.dismiss();
        }
    }

    @Override
    protected void init() {
        getPresenter().initTodayData();
    }

    @Override
    public void showLoading() {
        showCommonLoadingLayout();
    }

    @Override
    public void showError() {
        showCommonNetErrorLayout();
    }

    @Override
    public void showEmpty() {
        showCommonEmptyLayout();
    }

    @Override
    public void showCommon() {
        showCommonLayout();
    }

    @Override
    public void showContent(List<CollectInfoBean> infoList) {
        showCommon();
        showLineChartData(infoList);
    }

    private void showLineChartData(List<CollectInfoBean> infoList) {
        ArrayList<LineDataSet> dataSetList = new ArrayList<>();
        List<Entry> entriesTemp = new ArrayList<>();
        List<Entry> entriesRate = new ArrayList<>();
        int infoSize = infoList.size();
        for (int i = 0; i < infoSize; i++) {
            entriesTemp.add(new Entry(Float.parseFloat(infoList.get(i).getTemperature()), i));
            entriesRate.add(new Entry(Float.parseFloat(infoList.get(i).getRate()), i));
        }
        addTempEntriesToDataSetList(entriesTemp, dataSetList);
        addRateEntriesToDataSetList(entriesRate, dataSetList);
        //初始化x轴
        String xValues[] = new String[infoSize];
        for (int i = 0; i < infoSize; i++) {
            xValues[i] = i + "";
        }
        LineData lineData = new LineData(xValues, dataSetList);
        mLineChart.setData(lineData);
        //X方向动画效果
        mLineChart.animateX(1000, Easing.EasingOption.EaseInOutQuart);
        //X,Y方向同时动画
        //mLineChart.animateXY(3000, 3000);
        mLineChart.invalidate();
    }

    private void addTempEntriesToDataSetList(List<Entry> entries, ArrayList<LineDataSet> dataSetList) {
        //LineDataSet可以看作是一条线
        LineDataSet tempDataSet = new LineDataSet(entries, "水温℃");
        //设置折线的颜色
        tempDataSet.setColor(Color.RED);
        //设置折线上点的字体颜色
        tempDataSet.setValueTextColor(Color.RED);
        //设置折线圆点的颜色
        tempDataSet.setCircleColor(Color.RED);
        //图上不描述点的值
        tempDataSet.setDrawValues(false);
        //设置折线圆点中心的颜色
        //dataSet.setCircleColorHole(Color.GRAY);
        //设置圆点的大小
        tempDataSet.setCircleSize(4f);
        //设置点击某个点时,横竖两条线的颜色
        tempDataSet.setHighLightColor(Color.YELLOW);
        dataSetList.add(tempDataSet);
    }

    private void addRateEntriesToDataSetList(List<Entry> entries, ArrayList<LineDataSet> dataSetList) {
        //LineDataSet可以看作是一条线
        LineDataSet rateDataSet = new LineDataSet(entries, "转速n/s");
        //设置折线的颜色
        rateDataSet.setColor(Color.GREEN);
        //设置折线上点的字体颜色
        rateDataSet.setValueTextColor(Color.GREEN);
        //设置折线圆点的颜色
        rateDataSet.setCircleColor(Color.GREEN);
        //设置折线圆点中心的颜色
        //        dataSet.setCircleColorHole(Color.GRAY);
        //设置圆点的大小
        rateDataSet.setCircleSize(4f);
        //设置点击某个点时,横竖两条线的颜色
        rateDataSet.setHighLightColor(Color.YELLOW);
        rateDataSet.setDrawValues(false);
        dataSetList.add(rateDataSet);
    }

    @Override
    public void saveLineImageToSDCard() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String dateString = format.format(date);
        if (mLineChart != null) {
            if (mLineChart.saveToGallery("lineChart-" + dateString + ".jpg", 100)) {
                ShowToast.shortTime("保存成功!");
            } else {
                ShowToast.shortTime("保存失败!");
            }
        } else {
            Log.i("ScanFragment", "saveLineChartToSDCard: mLineChart is null");
            ShowToast.shortTime("mLineChart is null!");
        }
    }

    @Override
    public void showAddMenuWindow() {
        AddPopWindow addPopWindow = new AddPopWindow(mMainActivity);
        addPopWindow.showPopupWindow(getToolbar());
        addPopWindow.setOnPopupWindowItemClickListener(new AddPopWindow.OnPopupWindowItemClickListener() {
            @Override
            public void onItemClick(int id) {
                if (id == R.id.watch_to_detail) {
                    startInfoDetailActivity();
//                    startNodeChoiceActivity();
                } else if (id == R.id.save_image_sd_card) {
                    getPresenter().saveLineImageToSDCard();
                } else if (id == R.id.refresh_data) {
                    getPresenter().refreshTodayData();
                }
            }
        });
    }

    @Override
    public void showRefreshing() {
        showCustomToast(R.mipmap.toast_done_icon, "刷新中...", Toast.LENGTH_SHORT);
    }

    @Override
    public void showCannotRefresh() {
        showCustomToast(R.mipmap.input_clean, "历史数据，不需要刷新...", Toast.LENGTH_SHORT);
    }

    @Override
    public void showSelectDateWindow(int year, int month, int day) {
        if (mSelectDateWindow == null) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_pop_window_select_date, null);
            mSelectDateWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mCalendarView = (CustomCalendarView) view.findViewById(R.id.custom_calendar_view);
            LinearLayout layoutBottom = (LinearLayout) view.findViewById(R.id.layout_pop_window_bottom);
            ImageView ivHideSelectDate = (ImageView) view.findViewById(R.id.iv_hide_select_date);
            mTvBackToday = (TextView) view.findViewById(R.id.tv_back_today);
            mCalendarView.setMonthChangedListener(this);
            mCalendarView.setDateSelectedListener(this);
            layoutBottom.setOnClickListener(this);
            ivHideSelectDate.setOnClickListener(this);
            mTvBackToday.setOnClickListener(this);
            mSelectDateWindow.setAnimationStyle(R.style.PopupSelectDateAnimation);
            mSelectDateWindow.setBackgroundDrawable(new BitmapDrawable());
            mSelectDateWindow.setTouchable(true);
            mSelectDateWindow.setOutsideTouchable(true);
            mSelectDateWindow.setFocusable(true);
            mCalendarView.state().edit().setMinimumDate(CalendarTools.createCalendar(2016, 8, 7)).commit();  //设置最小日期为2016年9月7号
        }
        mCalendarView.setCurrentDate(CalendarTools.createCalendar(year, month, day));
        mCalendarView.setSelectedDate(CalendarTools.createCalendar(year, month, day));
        mSelectDateWindow.showAsDropDown(getToolbar());
        mSelectDateWindow.update();
    }

    @Override
    public void backToToday(int year, int month, int day) {
        if (mCalendarView != null) {
            mCalendarView.setCurrentDate(CalendarTools.createCalendar(year, month, day));
        }
    }

    @Override
    public void setBackTodayVisibility(boolean visible) {
        if (visible) {
            mTvBackToday.setVisibility(View.VISIBLE);
        } else {
            mTvBackToday.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setTopDateLayout(int year, int month, int day) {
        Calendar calendar = CalendarTools.createCalendar(year, month, day);
        mTvDay3.setText(SDF_M_D.format(calendar.getTime()));
        mTvWeek3.setText(CalendarTools.getWeek(calendar));
        calendar.add(Calendar.DATE, -1);
        mTvDay2.setText(SDF_M_D.format(calendar.getTime()));
        mTvWeek2.setText(CalendarTools.getWeek(calendar));
        calendar.add(Calendar.DATE, -1);
        mTvDay1.setText(SDF_M_D.format(calendar.getTime()));
        mTvWeek1.setText(CalendarTools.getWeek(calendar));
    }

    @Override
    public void changeTopDateColor(int selectDatePosition) {
        switch (selectDatePosition) {
            case POSITION_FIRST_DAY:
                mTvDay1.setTextColor(COLOR_SELECTED);
                mTvWeek1.setTextColor(COLOR_SELECTED);
                mTvDay2.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek2.setTextColor(COLOR_NOT_SELECTED);
                mTvDay3.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek3.setTextColor(COLOR_NOT_SELECTED);
                break;
            case POSITION_SECOND_DAY:
                mTvDay1.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek1.setTextColor(COLOR_NOT_SELECTED);
                mTvDay2.setTextColor(COLOR_SELECTED);
                mTvWeek2.setTextColor(COLOR_SELECTED);
                mTvDay3.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek3.setTextColor(COLOR_NOT_SELECTED);
                break;
            case POSITION_THIRD_DAY:
                mTvDay1.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek1.setTextColor(COLOR_NOT_SELECTED);
                mTvDay2.setTextColor(COLOR_NOT_SELECTED);
                mTvWeek2.setTextColor(COLOR_NOT_SELECTED);
                mTvDay3.setTextColor(COLOR_SELECTED);
                mTvWeek3.setTextColor(COLOR_SELECTED);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDateSelected(@NonNull CustomCalendarView widget, @NonNull Calendar calendar) {
        getPresenter().changeToSelectDay(calendar);
        dismissSelectDateWindow();
    }

    //主要是设置 "回到今天" 的可见性
    @Override
    public void onMonthChanged(CustomCalendarView widget, Calendar calendar) {
        getPresenter().setBackTodayVisibility(calendar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppLog.i(TAG, "onActivityCreated: 执行了");
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLog.i(TAG, "onStart: 执行了");
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.i(TAG, "onResume: 执行了");
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.i(TAG, "onPause: 执行了");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLog.i(TAG, "onStop: 执行了");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.i(TAG, "onDestroyView: 执行了");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i(TAG, "onDestroy: 执行了");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLog.i(TAG, "onDetach: 执行了");
    }

    /**
     * 跳转到带有时间戳的转速、温度详情Activity中去
     */
    private void startInfoDetailActivity() {
        //跳转到供暖节点选择Activity
        Intent intent = new Intent(mMainActivity, InfoDetailActivity.class);
        startActivity(intent);
        //Activity启动动画
        mMainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }

    /**
     * 跳转到节点选择的Activity中去
     */
    private void startNodeChoiceActivity() {
        //跳转到供暖节点选择Activity
        Intent intent = new Intent(mMainActivity, NodeChoiceActivity.class);
        startActivityForResult(intent, ConstantValue.NODE_CHOICE_REQUEST_CODE);
        //Activity启动动画
        mMainActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }

    //得到选择的节点,设置到ToolBat title中
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获得 mNodeName
        if (requestCode == ConstantValue.NODE_CHOICE_REQUEST_CODE
                && resultCode == ConstantValue.NODE_CHOICE_RESULT_CODE) {
            if (data != null) {
                mNodeName = data.getStringExtra("nodeName");
                LebronPreference.getInstance().saveNodeChoice(mNodeName);
                mToolbar.setTitle(mNodeName);
            }
        }
    }

}
