package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.hc.posterccb.Constant;
import com.hc.posterccb.api.Api;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.polling.ConfigBean;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.ControlProgramBean;
import com.hc.posterccb.bean.polling.DownLoadFileBean;
import com.hc.posterccb.bean.polling.LogReportBean;
import com.hc.posterccb.bean.polling.PollResultBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.UpGradeBean;
import com.hc.posterccb.bean.report.TaskReportBean;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.subscriber.CommonSubscriber;
import com.hc.posterccb.util.ControlUtils;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.FileUtils;
import com.hc.posterccb.util.JsonUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.SFTPUtils;
import com.hc.posterccb.util.SpUtils;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.VolumeUtils;
import com.hc.posterccb.util.XmlUtils;
import com.hc.posterccb.util.encrypt.DesDecUtils;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/10.
 */

public class MainModel extends BaseModel {

    private String TAG = "MainModel";

    private SFTPUtils mSFTPUtils = new SFTPUtils();

    public volatile int pollingTimer = 10;
    private TaskReportBean mTaskReport = new TaskReportBean();

    private boolean mLicensed = false;


    //轮询任务
    public void pollingTask(@NonNull final String command, @NonNull final String mac, @NonNull final InfoHint infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");
        Observable.interval(pollingTimer, TimeUnit.SECONDS)
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
                                            //获取返回的任务类型集合
                                            ArrayList<String> typeList = XmlUtils.getXmlType(resStr);
                                            if (typeList.size() <= 0) return;
                                            //获取任务类型实体类
                                            for (String type : typeList) {
                                                PostResult postResult = XmlUtils.getTaskBean(type, resStr);//通过返回的响应xml报文解析出是哪个任务
                                                Log.e("MainModel", postResult.toString());
                                                //处理任务类型
                                                resResult(type, postResult, infoHint);
                                            }
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

    //下载文件
    public void downLoadFile(@NonNull String path) {

    }

    //系统配置
    public void configSystem() {
        String jsonStr = FileUtils.getStringFromTxT(Constant.LOCAL_CONFIG_TXT);
        if (!jsonStr.equals("")) {
            Gson gson = new Gson();
            ConfigBean bean = gson.fromJson(jsonStr, ConfigBean.class);
            Log.e("即时消息", bean.toString() + "---");
            String powerOnTimeStr = bean.getStaruptime();//开机时间
            String powerOffTimeStr = bean.getShutdowntime();//关机时间
            int diskspacealarm = Integer.parseInt(bean.getDiskspacealarm());//硬盘警告阀值
            String serverip = bean.getServerconfig();//服务器信息
            SpUtils.put("serverip", serverip);//保存服务器信息
            int selectinterval = Integer.parseInt(bean.getSelectinterval());//轮询时间
            SpUtils.put("selectinterval", selectinterval);//保存轮询时间
            int volume = Integer.parseInt(bean.getVolume());//终端音量 机器最高音量为15
            VolumeUtils.setVolum(volume);
            int downloadrate = Integer.parseInt(bean.getVolume());
            String downloadtime = bean.getDownloadtime();//开机时间


            Date powerOnTime = DateFormatUtils.string2Date(powerOnTimeStr, "HH:mm");
            Date powerOffTime = DateFormatUtils.string2Date(powerOffTimeStr, "HH:mm");
            Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = DateFormatUtils.date2String(date);
                            Log.e("现在时间", currentTime + "---");
                            if (date.getTime() > startTime.getTime()) {
                                infoHint.realtimeStart();
                            } else if (date.getTime() > endTime.getTime()) {
                                infoHint.realTimeStop();
                            }
                        }
                    });
        }
    }


    private void resResult(String taskType, PostResult postResult, InfoHint infoHint) {
        if (postResult.getBean() == null) {
            return;
        }

        switch (taskType) {
            //播放类任务
            case Constant.POLLING_PROGRAM: {
                ArrayList<ProgramBean> list = postResult.getList();
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resProgram(list);
                break;
            }
            //升级类任务
            case Constant.POLLING_UPGRADE: {
                UpGradeBean bean = (UpGradeBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resUpgrade(bean);
                break;
            }
            //控制类任务
            case Constant.POLLING_CONTROL: {
                ControlBean bean = (ControlBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resControl(bean);
                break;
            }
            //即时消息类任务
            case Constant.POLLING_REALTIMEMSG: {
                RealTimeMsgBean bean = (RealTimeMsgBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resRealTimeMsg(bean, infoHint);
                break;
            }
            //取消即时类任务
            case Constant.POLLING_CANCELREALTIMEMSG: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resCacnleRealTimeMsg(bean, infoHint);
                break;
            }
            //配置类任务
            case Constant.POLLING_CONFIG: {
                ConfigBean bean = (ConfigBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resConfig(bean);
                break;
            }
            //控制类任务
            case Constant.POLLING_CONTROLPROGRAM: {
                ControlProgramBean bean = (ControlProgramBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resControlProgram(bean, infoHint);
                break;
            }
            //终端配置信息日志上报任务
            case Constant.POLLING_CFGREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resCfgReport(bean);
                break;
            }
            //终端配置信息日志上报任务
            case Constant.POLLING_WORKSTATUSREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resResWorkStatusReport(bean);
                break;
            }
            //终端工作状态上报类任务
            case Constant.POLLING_MONITORREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resMonitorReport(bean);
                break;
            }
            case Constant.POLLING_LOGREPORT: {
                LogReportBean bean = (LogReportBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resLogReport(bean);
                break;
            }

            case Constant.POLLING_DOWNLOADRES: {
                DownLoadFileBean bean = (DownLoadFileBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resReDownLoadFile(bean);
                break;
            }

            case Constant.POLLING_DOWNLOADSTATUSREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resResDownLoadStatusReport(bean);
            }

        }
    }

    //控制类任务  0:重启 1:休眠 2:唤醒
    private void resControl(ControlBean bean) {
        int control = Integer.parseInt(bean.getControl());
        Observable.just(control)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        switch (integer) {
                            case 0:
                                ControlUtils.do_exec("reboot");
                                break;
                            case 1:
                                try {
                                    ControlUtils.writeFile("0");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                try {
                                    ControlUtils.writeFile("1");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                    }
                });
    }

    //通知终端上报资源下载状态
    private void resResDownLoadStatusReport(PollResultBean bean) {

    }

    //通知终端下载资源文件
    private void resReDownLoadFile(DownLoadFileBean bean) {
        mSFTPUtils.setHost(Api.SFTP_PATH + bean.getClass());
        mSFTPUtils.setPassword("123456");
        mSFTPUtils.setUsername("Alex");
        Observable.just(mSFTPUtils)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<SFTPUtils>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "下载成功");
                        mSFTPUtils.disconnect();
                        Log.e(TAG, "断开连接");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "下载失败");
                        Log.e(TAG, e.getMessage());
                        mSFTPUtils.disconnect();
                        Log.e(TAG, "断开连接");
                    }

                    @Override
                    public void onNext(SFTPUtils utils) {
                        Log.e(TAG, "下载文件");
                        String localPath = Constant.UDP_TESTPATH;
                        String remotePath = "test1/";
                        mSFTPUtils.connect();
                        Log.e(TAG, "连接成功");
                        mSFTPUtils.downloadFile(remotePath, "aec.mp4", localPath, "test.mp4");
                    }
                });
    }

    //终端日志上报任务
    private void resLogReport(LogReportBean bean) {

    }

    //终端在播内容上报
    private void resMonitorReport(PollResultBean bean) {
        String jsonStr = FileUtils.getStringFromTxT(Constant.LOCAL_PROGRAM_TXT);

    }

    //终端工作状态上报
    private void resResWorkStatusReport(PollResultBean bean) {

    }

    //终端配置信息日志上报
    private void resCfgReport(PollResultBean bean) {

    }

    //播放类控制类
    private void resControlProgram(ControlProgramBean bean, final InfoHint infoHint) {
        int cmd = Integer.getInteger(bean.getCmd());
        Observable.just(cmd)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        switch (integer) {
                            case 1:
                                infoHint.videoPause();
                                break;
                            case 2:
                                infoHint.videoReplay();
                                break;
                            case 3:
                                infoHint.videoDelProgramList();
                                break;
                            case 4:
                                infoHint.videoInterruptCancle();
                                break;
                        }
                    }
                });
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
    private void resUpgrade(UpGradeBean bean) {

        LogUtils.e("resResUpgrade", bean.toString());
    }

    //即时消息类任务
    private void resRealTimeMsg(RealTimeMsgBean bean, final InfoHint infoHint) {

        final String startTimeStr = bean.getStarttime();
        final String endTimeStr = bean.getEndtime();
        final Date startTime = DateFormatUtils.string2Date(startTimeStr, "HH:mm");
        final Date endTime = DateFormatUtils.string2Date(endTimeStr, "HH:mm");
        infoHint.realTimeMessage(bean);

        Log.e("即时消息", bean.toString() + "---");
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Date date = new Date(System.currentTimeMillis());
                        String currentTime = DateFormatUtils.date2String(date, "HH:mm");
                        Log.e("现在时间", currentTime + "---");
                        if (date.getTime() > startTime.getTime()) {
                            infoHint.realtimeStart();
                        } else if (date.getTime() > endTime.getTime()) {
                            infoHint.realTimeStop();
                        }
                    }
                });
    }

    //取消即时消息任务
    private void resCacnleRealTimeMsg(PollResultBean bean, final InfoHint infoHint) {
        infoHint.realTimeCancle();
    }

    //终端配置信息类任务
    private void resConfig(ConfigBean bean) {
        String jsonStr = JsonUtils.Bean2JsonStr(bean);
        LogUtils.e("resResConfig", jsonStr);
        try {
            FileUtils.coverTxtToFile(jsonStr, Constant.LOCAL_CONFIG_TXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void postTaskreport(String tasktype, String taskid) {
        XStream xStream = new XStream();
        mTaskReport.setTasktype(tasktype);
        mTaskReport.setTaskid(taskid);
        mTaskReport.setStatus("0000");
        xStream.alias("command", TaskReportBean.class);
        String postStr = xStream.toXML(mTaskReport);
        StringUtils.setEncoding(postStr, "UTF-8");

        httpService.report(Constant.TASKREPORT, Constant.getSerialNumber(), postStr)
                .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                    @Override
                    public void onNext(ResponseBody body) {
                        String resStr = null;
                        try {
                            //获取返回的xml 字符串
                            resStr = body.string();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void CheckLisence(final Config config) {
        getData();
        if (mLicensed) {
            Log.e("mLicensed", "已授权");
        } else {
            config.noLicense();
        }
        File file4 = new File(Constant.SERIAL_PATH);
        if (file4.exists()) {
            if (!mLicensed) {
                desSerialNumber(config);
            }
        }
        Observable.interval(1000, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        File file1 = new File(Constant.RESULT_PATH);
                        File file2 = new File(Constant.DEBUG_PATH);
                        File file3 = new File(Constant.LOGCAT_PATH);


//                    Log.e("路径：",ConstUtil.RESULT_PATH+"--------"+ConstUtil.DEBUG_PATH+"-------"+ConstUtil.LOGCAT_PATH);
                        if (!file1.exists() || !file2.exists() || !file3.exists()) {
                            Log.e("runCheckLicenedTask", "文件不存在");
                            config.noLicense();
                        }

                    }
                });
    }

    private void desSerialNumber(Config config) {
        FileUtils fileUtils = new FileUtils();
        try {
            List<String> listDec = fileUtils.DecFile(Constant.SERIAL_PATH);
            for (String s : listDec) {
                Log.e("解密结果", DesDecUtils.desDec(s.trim()));
                if (DesDecUtils.desDec(s.trim()).equals(Constant.SERIAL_NUMBER.trim())) {
                    mLicensed = true;
                    Log.e("Licensed", mLicensed + "");
                    saveData();
//                    FileUtils.deleteFile(path);
                    FileUtils.CreatText();
                    FileUtils.writSerialTxt(Constant.SERIAL_NUMBER);
                    config.license();
                    break;
                }
            }
            if (!mLicensed) config.noLicense();
        } catch (IOException e) {
            Log.e("Licensed", mLicensed + "");
        }
    }

    private void saveData() {
        SpUtils.put("Licensed", mLicensed);
    }

    private void getData() {
        mLicensed = (boolean) SpUtils.get("Licensed", false);
    }

    //关机
    private void powerOff(String dateStr) {

    }

    //开机
    private void powerOn(String dateStr) {

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

        //暂停播放
        void videoPause();

        //恢复播放
        void videoReplay();

        //删除当前播放列表
        void videoDelProgramList();

        //取消插播
        void videoInterruptCancle();


    }

    //通过接口产生信息回调
    public interface Config {
        //授权
        void license();

        void noLicense();
    }

}
