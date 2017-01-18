package com.lebron.graduationpro1.scanpage.model;

import com.lebron.graduationpro1.scanpage.contracts.ScanContracts;
import com.lebron.mvp.model.BaseModel;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class ScanRequestModel extends BaseModel<ScanContracts.Model.Callback>
        implements ScanContracts.Model {
    @Override
    public void getCollectInfo(String date) {
        // 网络请求

        //接口回调
    }

    private void handleSuccess(List<CollectInfoBean> infoList) {
        for (Callback callback : getCallBacks()) {
            callback.onSuccess(infoList);
        }
    }

    private void handleFail(int retCode, String retDesc) {
        for (Callback callback : getCallBacks()) {
            callback.onFail(retCode, retDesc);
        }
    }
}
