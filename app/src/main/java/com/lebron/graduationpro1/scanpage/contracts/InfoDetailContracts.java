package com.lebron.graduationpro1.scanpage.contracts;


import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;

import java.util.List;

public interface InfoDetailContracts {
    interface View {
        /**
         * 显示正在加载页面
         */
        void showLoading();

        /**
         * 显示网络错误页面
         */
        void showError();

        /**
         * 显示空内容页面
         */
        void showEmpty();

        /**
         * 显示除了 加载中,网络错误,空内容的正常页面
         */
        void showCommon();

        /**
         * 显示正常加载出来的内容
         */
        void showContent(List<CollectInfoBean> infoList);

        /**
         * 显示正在刷新
         */
        void showRefreshing();
    }

    interface Presenter {
        /**
         * 根据日期向服务器请求数据,然后根据请求的json解析得到List之后通过 onSuccess()回调回来
         */
        void getCollectInfo(String date);

        /**
         * 初始化今天相关的数据
         */
        void initTodayData();

        /**
         * 重新刷新今天相关的数据
         */
        void refreshTodayData();
    }

    interface Model {

        /**
         * 根据日期向服务器请求数据,然后根据请求的json解析得到List之后通过 onSuccess()回调回来
         */
        void getCollectInfo(String date);

        interface Callback {
            /**
             * 返回成功请求的日历内容
             */
            void onSuccess(List<CollectInfoBean> infoList);

            /**
             * 处理网络错误的返回
             */
            void onFail(int retCode, String retDesc);
        }
    }
}
