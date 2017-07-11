package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.acitivity.MainActivity;
import com.hc.posterccb.ui.contract.MainContract;
import com.hc.posterccb.ui.model.MainModel;
import com.hc.posterccb.util.LogUtils;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/8.
 */

public class MainPresenter extends BasePresenter<MainActivity>
        implements MainContract.MainPresenter{

    @Override
    public void pollingTask(String command, String mac) {
        if (!getIView().checkPostParamNull()){
            ((MainModel)getiModelMap().get("polling")).pollingTask(command, mac, new MainModel.InfoHint() {
                @Override
                public void successInfo(String str) {
                    getIView().pollingSuccess(str);
                }

                @Override
                public void failInfo(String str) {
                    LogUtils.e("MainPresenter.failInfo", str);
                    getIView().pollingFail(str);
                }
            });
        }
    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new MainModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String,IModel> map=new HashMap<>();
        map.put("polling",models[0]);
        return map;
    }


}
