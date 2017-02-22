package com.lebron.graduationpro1.detailpage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.detailpage.contracts.DetailContracts;
import com.lebron.graduationpro1.detailpage.presenter.DetailPresenter;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.mvp.factory.RequiresPresenter;

/**
 * 详情页
 */
@RequiresPresenter(DetailPresenter.class)
public class DetailFragment extends BaseFragment<DetailPresenter> implements
        SwipeRefreshLayout.OnRefreshListener, DetailContracts.View {
    private MainActivity mMainActivity;
    private static final String TAG = "DetailFragment";
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

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
        mRecyclerView = ((RecyclerView) view.findViewById(R.id.recycler_view_details));
        mRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mMainActivity, R.color.colorAccent),
                ContextCompat.getColor(mMainActivity, R.color.green),
                ContextCompat.getColor(mMainActivity, R.color.toolBarBackground));
        // 第一次进入页面的时候显示刷新圈圈
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    protected void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.i(TAG, "hidden = " + hidden); // 该方法第二次可见时 hidden = false,不可见时不一定会执行，执行就为true。
    }

    @Override
    protected void init() {
        getPresenter().getHeatInfo(1);
    }

    @Override
    public void onRefresh() {

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
