package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hc.posterccb.Constant;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.subscriber.CommonSubscriber;
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
                                            resStr = response.string();
                                            String type=XmlUtils.getXmlType(resStr);
                                            PostResult postResult=XmlUtils.getTaskBean(type,resStr);//通过返回的响应xml报文解析出是哪个任务
                                            Log.e("MainModel",postResult.toString());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        infoHint.successInfo(resStr);
//                            LogUtils.e("MainModel", resStr);
                                    }

                                    @Override
                                    public void onError(ApiException e) {
                                        super.onError(e);
                                        infoHint.failInfo(e.message);
                                        LogUtils.e("MainModel", e.getMessage());
                                    }
                                });
                    }
                });

    }

    public void downLoadFile(@NonNull String path){

    }

    public void resResult(String taskType,PostResult postResult){
        if (postResult.getBean()==null){
            return;
        }
        switch (taskType){
            case Constant.POLLING_PROGRAM:{
                ArrayList<ProgramBean> list=new ArrayList<>();
                resProgram(list);
                break;
            }

        }
    }

    private void resProgram(ArrayList<ProgramBean> list) {

    }


    //通过接口产生信息回调
    public interface InfoHint {
        //请求有返回
        void successInfo(String str);

        //请求没有返回
        void failInfo(String str);

    }
}
