package com.lebron.graduationpro1.scanpage.contracts;

import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public interface ScanContracts {
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
         * 显示正常加载出来的内容
         */
        void showContent(List<CollectInfoBean> infoList);

        /**
         * 保存折线图到sd卡
         */
        void saveLineImageToSDCard();

        /**
         * 弹出PopWindow.根据选择项进行不同操作
         */
        void showPopWindowAndHandleChoice();
    }

    interface Presenter {
        /**
         * 根据日期向服务器请求数据,然后根据请求的json解析得到List之后通过 onSuccess()回调回来
         */
        void getCollectInfo(String date);
    }

    interface Model {

        /**
         * 根据日期向服务器请求数据,然后根据请求的json解析得到List之后通过 onSuccess()回调回来
         */
        void getCollectInfo(String date);

        interface Callback{
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
