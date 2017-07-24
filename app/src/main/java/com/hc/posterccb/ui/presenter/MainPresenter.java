package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
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
        implements MainContract.MainPresenter {


    @Override
    public void pollingTask(String command, String mac) {
        if (!getIView().checkPostParamNull()) {
            ((MainModel) getiModelMap().get("polling")).pollingTask(command, mac, new MainModel.InfoHint() {
                @Override
                public void successInfo(String str) {
                    getIView().pollingSuccess(str);
                }

                @Override
                public void failInfo(String str) {
                    LogUtils.e("MainPresenter.failInfo", str);
                    getIView().pollingFail(str);
                }

                @Override
                public void realTimeMessage(RealTimeMsgBean bean) {
                    getIView().setRealTimeText(bean);
                }

                @Override
                public void realtimeStart() {
                    getIView().startRealtimeTask();
                }

                @Override
                public void realTimeStop() {
                    getIView().stopRealtimeTask();
                }

                @Override
                public void realTimeCancle() {
                    getIView().cancleRealtimeTask();
                }

                @Override
                public void videoPause() {

                }

                @Override
                public void videoReplay() {

                }

                @Override
                public void videoDelProgramList() {

                }

                @Override
                public void videoInterruptCancle() {

                }
            });
        }
    }

    @Override
    public void downLoadFile(String command, String mac, String path) {
        if (!getIView().checkPostParamNull()) {
            ((MainModel) getiModelMap().get("downloadfile")).downLoadFile(path);
        }
    }


    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new MainModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("polling", models[0]);
        map.put("downloadfile", models[0]);
        return map;
    }


}
