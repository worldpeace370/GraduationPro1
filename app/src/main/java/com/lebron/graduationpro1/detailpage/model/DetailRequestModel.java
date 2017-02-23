package com.lebron.graduationpro1.detailpage.model;

import com.lebron.graduationpro1.detailpage.contracts.DetailContracts;
import com.lebron.graduationpro1.service.VolleyService;
import com.lebron.mvp.model.BaseModel;

import java.util.List;

/**
 * Created by wuxiangkun on 2017/2/22.
 * Contact way wuxiangkun2015@163.com
 */

public class DetailRequestModel extends BaseModel<DetailContracts.Model.CallBack>
        implements DetailContracts.Model {
    private static final String mHostDetailUrl =
            "http://114.215.117.169/thinkphp/Home/ApiGrad/pageSearch/page/%d";

    @Override
    public void getHeatInfo(int page) {
        // 网络请求，接口回调
        new VolleyService().getDataFromServer(String.format(mHostDetailUrl, page), HeatInfo.class,
                new VolleyService.RequestCompleteListener<HeatInfo>() {
                    @Override
                    public void success(List<HeatInfo> list) {
                        handleSuccess(list);
                    }

                    @Override
                    public void error(String error) {
                        handleFail(-200, error);
                    }
                });
    }

    private void handleSuccess(List<HeatInfo> infoList) {
        for (DetailContracts.Model.CallBack callback : getCallBacks()) {
            callback.onSuccess(infoList);
        }
    }

    private void handleFail(int retCode, String retDesc) {
        for (DetailContracts.Model.CallBack callback : getCallBacks()) {
            callback.onFail(retCode, retDesc);
        }
    }
}
