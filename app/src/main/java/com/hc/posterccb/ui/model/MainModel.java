package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.hc.posterccb.Constant;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.UpGradeBean;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.subscriber.CommonSubscriber;
import com.hc.posterccb.util.JsonUtils;
import com.hc.posterccb.util.ListDataSave;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.XmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by alex on 2017/7/10.
 */

public class MainModel extends BaseModel {


    public void pollingTask(@NonNull final String command, @NonNull final String mac, @NonNull final InfoHint infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");
        Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        httpService.polling(command, mac)
                                .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                                    @Override
                                    public void onNext(ResponseBody response) {

                                        String resStr = null;
                                        try {
                                            //获取返回的xml 字符串
                                            resStr = response.string();
                                            //获取返回的任务类型
                                            String type = XmlUtils.getXmlType(resStr);
                                            //获取任务类型实体类
                                            PostResult postResult = XmlUtils.getTaskBean(type, resStr);//通过返回的响应xml报文解析出是哪个任务
                                            Log.e("MainModel", postResult.toString());
                                            //处理任务类型
                                            resResult(type,postResult);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //view显示成功信息
                                        infoHint.successInfo(resStr);
//                            LogUtils.e("MainModel", resStr);
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        //view显示失败信息
                                        infoHint.failInfo(e.message);
                                        LogUtils.e("MainModel", e.getMessage());
                                    }
                                });
                    }
                });

    }

    public void downLoadFile(@NonNull String path) {

    }


    public void resResult(String taskType, PostResult postResult) {
        if (postResult.getBean() == null) {
            return;
        }
        switch (taskType) {
            //播放类任务
            case Constant.POLLING_PROGRAM: {
                ArrayList<ProgramBean> list = postResult.getList();
                resProgram(list);
                break;
            }
            case Constant.POLLING_UPGRADE:{
                UpGradeBean bean= (UpGradeBean) postResult.getBean();
                resResUpgrade(bean);
                break;
            }

            case Constant.POLLING_CONTROL:{
                ControlBean bean= (ControlBean) postResult.getBean();
                break;
            }

            case Constant.POLLING_REALTIMEMSG:{
                RealTimeMsgBean bean= (RealTimeMsgBean) postResult.getBean();
                resResRealTimeMsg(bean);
                break;
            }


        }
    }

    //播放类任务
    private void resProgram(ArrayList<ProgramBean> list) {

        for (ProgramBean bean : list) {
            LogUtils.e("resProgram",bean.toString());
        }
        Gson gson=new Gson();
        String arrayStr= JsonUtils.ArrayList2JsonStr(list);
//        JSONArray array= gson.fromObject(list);
        LogUtils.e("resProgram",arrayStr);
        ArrayList<ProgramBean> resList=new ArrayList<>();
        resList=JsonUtils.JsonStr2ArrayList(arrayStr,resList);
        for (ProgramBean programBean : resList) {
            LogUtils.e("resProgram",programBean.toString());
        }


        ListDataSave listDataSave=new ListDataSave(ProApplication.getmContext(),"CCB");
        listDataSave.setDataList("program",list);
    }

    //升级任务
    private void resResUpgrade(UpGradeBean bean) {
       LogUtils.e("resResUpgrade", bean.toString());
    }

    //即时消息类任务
    private void resResRealTimeMsg(RealTimeMsgBean bean){

    }


    //通过接口产生信息回调
    public interface InfoHint {
        //请求有返回
        void successInfo(String str);

        //请求没有返回
        void failInfo(String str);

    }
}
