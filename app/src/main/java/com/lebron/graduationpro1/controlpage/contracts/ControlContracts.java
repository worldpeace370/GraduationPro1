package com.lebron.graduationpro1.controlpage.contracts;

import java.util.Map;

/**
 * Created by wuxiangkun on 2017/2/27.
 * Contact way wuxiangkun2015@163.com
 */

public interface ControlContracts {
    interface View {
        /**
         * 上传控制信息成功后的回调，界面显示
         *
         * @param infoType 信息种类 temperature or rate
         */
        void showUploadSuccess(String infoType);

        /**
         * 上传控制信息失败后的回调，界面显示
         *
         * @param retCode 返回码
         * @param retDesc 返回错误描述
         */
        void showUploadFail(int retCode, String retDesc);
    }

    interface Presenter {
        /**
         * 上传控制信息
         *
         * @param infoType  信息种类 temperature or rate
         * @param infoValue 信息值
         */
        void uploadControlInfo(String infoType, String infoValue);
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
             * @param infoType 温度类型 or 转速类型
             */
            void onSuccess(String infoType);

            /**
             * 处理网络错误的返回
             */
            void onFail(int retCode, String retDesc);
        }
    }
}
