package com.lebron.graduationpro1.detailpage.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lebron.graduationpro1.R;
import com.lebron.graduationpro1.base.BaseFragment;
import com.lebron.graduationpro1.detailpage.presenter.DetailPresenter;
import com.lebron.graduationpro1.main.MainActivity;
import com.lebron.graduationpro1.utils.AppLog;
import com.lebron.mvp.factory.RequiresPresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresPresenter(DetailPresenter.class)
public class DetailFragment extends BaseFragment<DetailPresenter> {
    private static final String ARG_PARAM = "param";
    private String mParam;
    private MainActivity mMainActivity;
    private static final String TAG = "DetailFragment";
    private Unbinder mBind;
    private View mRootView;

    public DetailFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param param Parameter.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(String param) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mMainActivity = ((MainActivity) getActivity());
        }else {
            throw new IllegalArgumentException("The context must to be instanceof MainActivity");
        }
        AppLog.i(TAG, "onAttach: 执行了");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
        AppLog.i(TAG, "onCreate: 执行了");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_detail, container, false);
        }
        bindViews(mRootView);
        AppLog.i(TAG, "onCreateView: 执行了");
        return mRootView;
    }

    @Override
    protected void bindViews(View view) {
        mBind = ButterKnife.bind(this, mRootView);
        initNoStandardUI(view);
        initToolbar(view);
        getToolbar().setTitle("");
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void init() {

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
        mBind.unbind();
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
