package com.lebron.graduationpro1.detailpage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.adapter.RecyclerViewAdapter;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.detailpage.contracts.DetailContracts;
import com.lebron.graduationpro1.detailpage.model.HeatInfo;
import com.lebron.graduationpro1.detailpage.presenter.DetailPresenter;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.mvp.factory.RequiresPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页
 */
@RequiresPresenter(DetailPresenter.class)
public class DetailFragment extends BaseFragment<DetailPresenter> implements
        SwipeRefreshLayout.OnRefreshListener, DetailContracts.View, View.OnClickListener{
    private MainActivity mMainActivity;
    private static final String TAG = "DetailFragment";
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private List<HeatInfo> totalList = new ArrayList<>();
    private int mCurrentPage = 1;
    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mIsRefreshByManual = false;

    public DetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mMainActivity = ((MainActivity) getActivity());
        } else {
            throw new IllegalArgumentException("The context must to be instanceof MainActivity");
        }
        AppLog.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainActivity = (MainActivity) getActivity();
        }
        AppLog.i(TAG, "onCreate: 执行了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
            bindViews(mRootView);
            setListener();
            init();
        }
        AppLog.i(TAG, "onCreateView: 执行了");
        return mRootView;
    }

    @Override
    protected void bindViews(View view) {
        initToolbar(view);
        getToolbar().setTitle("");
        initNoStandardUI(view);
        mRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mMainActivity, R.color.colorAccent),
                ContextCompat.getColor(mMainActivity, R.color.green),
                ContextCompat.getColor(mMainActivity, R.color.toolBarBackground));
        mRecyclerView = ((RecyclerView) view.findViewById(R.id.recycler_view_details));
        mLayoutManager = new LinearLayoutManager(mMainActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerViewAdapter(mMainActivity, totalList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mRefreshLayout.setRefreshing(true);
                    getPresenter().getHeatInfo(++mCurrentPage);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mLayoutError.setOnClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.i(TAG, "hidden = " + hidden); // 该方法第二次可见时 hidden = false,不可见时不一定会执行，执行就为true。
    }

    @Override
    protected void init() {
        getPresenter().getHeatInfo(mCurrentPage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_common_network_error:
                getPresenter().getHeatInfo(mCurrentPage);
                showCustomToast(R.mipmap.toast_done_icon, "重新加载中...", Toast.LENGTH_SHORT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mIsRefreshByManual = true;
        getPresenter().getHeatInfo(1);
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
    public void showContent(List<HeatInfo> infoList) {
        showCommon();
        if (mIsRefreshByManual) {
            refreshDataToRecyclerView(infoList);
            mIsRefreshByManual = false;
        } else {
            addDataToRecyclerView(infoList);
        }
    }

    /**
     * 将数据加入到RecyclerView中，初始化和上拉加载更多时调用
     *
     * @param infoList 数据集合
     */
    private void addDataToRecyclerView(List<HeatInfo> infoList) {
        totalList.addAll(infoList);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false); // 加载更多时加载成功, 取消加载的动画
        if (mCurrentPage == 1) { // 只有刚进入页面的时候显示, 上拉加载不需要显示
            showCustomToast(R.mipmap.toast_done_icon, "加载成功!", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 将数据加入到RecyclerView中，刷新数据时调用
     *
     * @param infoList 数据集合
     */
    private void refreshDataToRecyclerView(List<HeatInfo> infoList) {
        totalList.clear();
        totalList.addAll(infoList);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false); // 数据重新加载成功, 取消刷新的动画
        showCustomToast(R.mipmap.toast_done_icon, "刷新成功!", Toast.LENGTH_SHORT);
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
        AppLog.i(TAG, "onDestroyView: 执行了");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLog.i(TAG, "onDestroyView: 执行了");
    }
}
