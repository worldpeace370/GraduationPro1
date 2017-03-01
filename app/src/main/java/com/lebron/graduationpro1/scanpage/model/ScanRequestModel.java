package com.lebron.graduationpro1.scanpage.model;

import com.lebron.graduationpro1.scanpage.contracts.ScanContracts;
import com.lebron.graduationpro1.service.VolleyRequestService;
import com.lebron.mvp.model.BaseModel;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/1/15.
 * Contact way wuxiangkun2015@163.com
 */

public class ScanRequestModel extends BaseModel<ScanContracts.Model.Callback>
        implements ScanContracts.Model {
    private static final String URL = "http://114.215.117.169/thinkphp/Home/ApiGrad/dateSearch/date/%s";
    private VolleyRequestService mRequestService = new VolleyRequestService();
    @Override
    public void getCollectInfo(String date) {
        // 网络请求，接口回调
        mRequestService.getDataFromServer(String.format(URL, date), CollectInfoBean.class,
                new VolleyRequestService.RequestCompleteListener<CollectInfoBean>() {
            @Override
            public void success(List<CollectInfoBean> list) {
                handleSuccess(list);
            }

            @Override
            public void error(String error) {
                handleFail(-200, error);
            }
        });
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
