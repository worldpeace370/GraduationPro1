package com.lebron.graduationpro1.scanpage.model;

import com.lebron.graduationpro1.scanpage.contracts.InfoDetailContracts;
import com.lebron.graduationpro1.service.VolleyRequestService;
import com.lebron.mvp.model.BaseModel;

import java.util.List;


public class InfoDetailRequestModel extends BaseModel<InfoDetailContracts.Model.Callback>
        implements InfoDetailContracts.Model {
    private static final String URL = "http://114.215.117.169/thinkphp/Home/ApiMiniData/dateSearch/date/%s";
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
        for (InfoDetailContracts.Model.Callback callback : getCallBacks()) {
            callback.onSuccess(infoList);
        }
    }

    private void handleFail(int retCode, String retDesc) {
        for (InfoDetailContracts.Model.Callback callback : getCallBacks()) {
            callback.onFail(retCode, retDesc);
        }
    }
}
