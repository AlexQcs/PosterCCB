package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hc.posterccb.Constant;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.polling.ConfigBean;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.PollResultBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.UpGradeBean;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.subscriber.CommonSubscriber;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.FileUtils;
import com.hc.posterccb.util.JsonUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.XmlUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
                                            resResult(type, postResult, infoHint);
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


    public void resResult(String taskType, PostResult postResult, InfoHint infoHint) {
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
            //升级类任务
            case Constant.POLLING_UPGRADE: {
                UpGradeBean bean = (UpGradeBean) postResult.getBean();
                resResUpgrade(bean);
                break;
            }
            //控制类任务
            case Constant.POLLING_CONTROL: {
                ControlBean bean = (ControlBean) postResult.getBean();
                break;
            }
            //即时消息类任务
            case Constant.POLLING_REALTIMEMSG: {
                RealTimeMsgBean bean = (RealTimeMsgBean) postResult.getBean();
                resResRealTimeMsg(bean, infoHint);
                break;
            }
            case Constant.POLLING_CANCELREALTIMEMSG: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                resResCacnleRealTimeMsg(bean, infoHint);
                break;
            }
            case Constant.POLLING_CONFIG: {
                ConfigBean bean = (ConfigBean) postResult.getBean();
                resResConfig(bean);
                break;
            }

        }
    }

    //播放类任务
    private void resProgram(ArrayList<ProgramBean> list) {

        String arrayStr = JsonUtils.ArrayList2JsonStr(list);
        LogUtils.e("resProgram", arrayStr);
        try {
            FileUtils.coverTxtToFile(arrayStr, Constant.LOCAL_PROGRAM_TXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //升级任务
    private void resResUpgrade(UpGradeBean bean) {
        LogUtils.e("resResUpgrade", bean.toString());
    }

    //即时消息类任务
    private void resResRealTimeMsg(RealTimeMsgBean bean, final InfoHint infoHint) {

        final String startTime = bean.getStarttime();
        final String endtime = bean.getEndtime();
        infoHint.realTimeMessage(bean);
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Date date = new Date();
                        String currentTime = DateFormatUtils.date2String(date);
                        if (currentTime.equals(startTime)) {
                            infoHint.realtimeStart();
                        } else if (currentTime.equals(endtime)) {
                            infoHint.realTimeStop();
                        }
                    }
                });
    }

    //取消即时消息任务
    private void resResCacnleRealTimeMsg(PollResultBean bean, final InfoHint infoHint) {
        infoHint.realTimeCancle();
    }

    //终端配置信息雷任务
    public void resResConfig(ConfigBean bean) {
        String jsonStr = JsonUtils.Bean2JsonStr(bean);
        LogUtils.e("resResConfig", jsonStr);
        try {
            FileUtils.coverTxtToFile(jsonStr, Constant.LOCAL_PROGRAM_TXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //通过接口产生信息回调
    public interface InfoHint {
        //请求有返回
        void successInfo(String str);

        //请求没有返回
        void failInfo(String str);

        /**
         * 此方法用于:即时消息任务
         *
         * @param bean
         *         字体大小
         * @author.Alex.on.2017年7月19日08:49:11
         */
        void realTimeMessage(RealTimeMsgBean bean);

        /**
         * 此方法用于:开始即时消息任务
         */
        void realtimeStart();

        /**
         * 此方法用于:停止即时消息任务
         */
        void realTimeStop();

        /**
         * 此方法用于:取消即时消息任务
         */
        void realTimeCancle();
    }
}
