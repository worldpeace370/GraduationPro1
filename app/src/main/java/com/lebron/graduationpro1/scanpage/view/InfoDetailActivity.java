package com.lebron.graduationpro1.scanpage.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseActivity;
import com.lebron.graduationpro1.scanpage.contracts.InfoDetailContracts;
import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;
import com.lebron.graduationpro1.scanpage.presenter.InfoDetailPresenter;
import com.lebron.mvp.factory.RequiresPresenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RequiresPresenter(InfoDetailPresenter.class)
public class InfoDetailActivity extends BaseActivity<InfoDetailPresenter> implements
        InfoDetailContracts.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private DetailAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<CollectInfoBean> mTotalList = new ArrayList<>();
    protected LinearLayout mLayoutLoading;
    protected LinearLayout mLayoutEmpty;
    protected LinearLayout mLayoutError;
    private MyHandler mHandler;
    private static class MyHandler extends Handler{
        WeakReference<InfoDetailActivity> weakReference;
        MyHandler(InfoDetailActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            InfoDetailActivity activity = weakReference.get();
            if (activity != null){//如果activity仍然在弱引用中,执行...
                switch (msg.what){
                    case 1:
                        activity.getPresenter().refreshTodayData();
                        break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        bindViews();
        init();
        setListener();
    }

    @Override
    protected void bindViews() {
        initToolbar();
        getToolbar().setTitle("");
        mRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.green),
                ContextCompat.getColor(this, R.color.toolBarBackground));
        mRecyclerView = ((RecyclerView) findViewById(R.id.recycler_view_details));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DetailAdapter(this, mTotalList);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutLoading = ((LinearLayout) findViewById(R.id.layout_common_loading));
        mLayoutEmpty = ((LinearLayout) findViewById(R.id.layout_common_empty));
        mLayoutError = ((LinearLayout) findViewById(R.id.layout_common_network_error));
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mLayoutError.setOnClickListener(this);
    }

    @Override
    protected void init() {
        mHandler = new MyHandler(this);
        showLoading();
        getPresenter().initTodayData();
        startRefreshTask();
    }

    @Override
    public void onRefresh() {
        getPresenter().initTodayData();
    }

    @Override
    public void showLoading() {
        mLayoutLoading.setVisibility(View.VISIBLE);
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        mLayoutError.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutEmpty.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        mLayoutEmpty.setVisibility(View.VISIBLE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    @Override
    public void showCommon() {
        mLayoutEmpty.setVisibility(View.GONE);
        mLayoutLoading.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.GONE);
    }

    @Override
    public void showContent(List<CollectInfoBean> infoList) {
        showCommon();
        addDataToRecyclerView(infoList);
    }

    /**
     * 将数据加入到RecyclerView中，初始化和上拉加载更多时调用
     *
     * @param infoList 数据集合
     */
    private void addDataToRecyclerView(List<CollectInfoBean> infoList) {
        mTotalList.clear();
        mTotalList.addAll(infoList);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false); // 加载更多时加载成功, 取消加载的动画
    }

    @Override
    public void showRefreshing() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_common_network_error:
                getPresenter().initTodayData();
                showCustomToast(R.mipmap.toast_done_icon, "重新加载中...", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    private void startRefreshTask() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.setTarget(mHandler);
                msg.sendToTarget();
            }}, 1000, 2000);//每隔2s执行1次
    }
}
