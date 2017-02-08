package com.lebron.graduationpro1.scanpage.contracts;

import com.lebron.graduationpro1.scanpage.model.CollectInfoBean;

import java.util.Calendar;
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
         * 点击菜单项 + , 弹出PopWindow.根据选择项进行不同操作
         */
        void showAddMenuWindow();

        /**
         * 点击日历,弹出PopWindow,选择不同的日期
         */
        void showSelectDateWindow(int year, int month, int day);

        /**
         * 隐藏popWindow"返回今天"的文字
         */
        void setBackTodayVisibility(boolean visible);

        /**
         * 回到今天
         */
        void backToToday(int year, int month, int day);

        /**
         * 根据年月日设置顶部三天的日期和年月
         */
        void setTopDateLayout(int year, int month, int day);

        /**
         * 根据选中日期的位置设置三天的颜色改变
         */
        void changeTopDateColor(int selectDatePosition);
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
         * 变为第一天，处理相关显示和数据
         */
        void changeToFirstPositionDay();

        /**
         * 变为第二天，处理相关显示和数据
         */
        void changeToSecondPositionDay();

        /**
         * 变为第三天，处理相关显示和数据
         */
        void changeToThirdPositionDay();

        /**
         * 变为选定的日期，处理相关数据和显示
         */
        void changeToSelectDay(Calendar calendar);

        /**
         * 根据所选择的年或者月只要有一个不属于当前日期所在的月, 就设置"回到今天"可见,否则不可见
         */
        void setBackTodayVisibility(Calendar calendar);

        /**
         * 通过点击交互导致日历切换到了别的月份中,回到今天
         */
        void backToToday();

        /**
         * 显示日期选择的PopupWindow
         */
        void showSelectDateWindow();

        /**
         * 点击菜单项 + , 弹出PopWindow.根据选择项进行不同操作
         */
        void showAddMenuWindow();
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
