package com.lebron.graduationpro1.controlpage.contracts;

import java.util.Map;

/**
 * Created by wuxiangkun on 2017/2/27.
 * Contact way wuxiangkun2015@163.com
 */

public interface ControlContracts {
    interface View {
        /**
         * 显示上传的结果
         */
        void showUploadResult(String resultStr);
    }

    interface Presenter {
        /**
         * 上传温度控制信息
         */
        void uploadTempInfo(String tempStr);

        /**
         * 上传转速控制信息
         */
        void uploadRateInfo(String rateStr);
    }

    interface Model {
        /**
         * 上传控制信息信息
         */
        void uploadControlInfo(Map<String, String> paramsMap);

        interface CallBack {

            /**
             * 返回成功请求的内容
             *
             * @param retDesc 请求成功返回字符串
             */
            void onSuccess(String retDesc);

            /**
             * 处理网络错误的返回
             */
            void onFail(int retCode, String retDesc);
        }
    }
}
