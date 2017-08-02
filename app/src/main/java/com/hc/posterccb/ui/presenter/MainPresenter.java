package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.program.Program;
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
                //轮询成功
                @Override
                public void successInfo(String str) {
                    getIView().pollingSuccess(str);
                }

                //轮询成功
                @Override
                public void failInfo(String str) {
                    LogUtils.e("MainPresenter.failInfo", str);
                    getIView().pollingFail(str);
                }

                //设置即时消息内容
                @Override
                public void realTimeMessage(RealTimeMsgBean bean) {
                    getIView().setRealTimeText(bean);
                }

                //即时消息开始
                @Override
                public void realtimeStart() {
                    getIView().startRealtimeTask();
                }

                //即时消息停止
                @Override
                public void realTimeStop() {
                    getIView().stopRealtimeTask();
                }

                //即时消息取消
                @Override
                public void realTimeCancle() {
                    getIView().cancleRealtimeTask();
                }

                @Override
                public void logicNormalProgram(Program program) {
                    getIView().logicNormalProgram(program);
                }

                @Override
                public void logicInterProgram(Program program) {
                    getIView().logicInterProgram(program);
                }

                //暂停播放
                @Override
                public void videoPause() {
                    getIView().pauseVideo();
                }

                //恢复播放
                @Override
                public void videoReplay() {
                    getIView().replayVideo();
                }

                //删除播放列表
                @Override
                public void videoDelProgramList() {
                    getIView().deleteProgramList();
                }

                //取消插播内容
                @Override
                public void videoInterruptCancle() {
                    getIView().cancleInterruptVideo();
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
    public void checkLicense() {
        if (!getIView().checkPostParamNull()) {
            ((MainModel) getiModelMap().get("checklicense")).CheckLisence(new MainModel.Config() {
                @Override
                public void license() {
                    getIView().license();
                }

                @Override
                public void noLicense() {
                    getIView().noLicense();
                }
            });
        }
    }

    @Override
    public void initConfig() {
        if (!getIView().checkPostParamNull()) {
            ((MainModel) getiModelMap().get("initconfig")).init();
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
        map.put("posttaskreport", models[0]);
        map.put("checklicense", models[0]);
        map.put("initconfig", models[0]);
        return map;
    }




}
