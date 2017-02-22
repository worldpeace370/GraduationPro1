package com.lebron.graduationpro1.detailpage.contracts;

import com.lebron.graduationpro1.detailpage.model.HeatInfo;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/2/22.
 * Contact way wuxiangkun2015@163.com
 */

public interface DetailContracts {

    interface View {

    }

    interface Presenter {
        /**
         * 请求一页的信息(包含5天的)
         *
         * @param page 数据库页 >= 1
         */
        void getHeatInfo(int page);
    }

    interface Model {

        /**
         * 请求一页的信息(包含5天的)
         *
         * @param page 数据库页 >= 1
         */
        void getHeatInfo(int page);

        interface CallBack {
            /**
             * 返回成功请求的内容
             *
             * @param infoList size为5 不够5的话就 <= 5
             */
            void onSuccess(List<HeatInfo> infoList);

            /**
             * 处理网络错误的返回
             */
            void onFail(int retCode, String retDesc);
        }
    }
}
