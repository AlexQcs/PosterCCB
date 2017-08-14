package com.hc.posterccb.ui.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.api.Api;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.PostResult;
import com.hc.posterccb.bean.UpGradeCfgBean;
import com.hc.posterccb.bean.polling.ConfigBean;
import com.hc.posterccb.bean.polling.ControlBean;
import com.hc.posterccb.bean.polling.ControlProgramBean;
import com.hc.posterccb.bean.polling.DownLoadFileBean;
import com.hc.posterccb.bean.polling.LogReportBean;
import com.hc.posterccb.bean.polling.PollResultBean;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.polling.TempBean;
import com.hc.posterccb.bean.polling.UpGradeBean;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.report.CfgReportBean;
import com.hc.posterccb.bean.report.ReportDownloadStatus;
import com.hc.posterccb.bean.report.ReportIdReqBean;
import com.hc.posterccb.bean.report.ReportWorkStatusBean;
import com.hc.posterccb.bean.report.TaskReportBean;
import com.hc.posterccb.bean.resource.ResourceBean;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.http.RequestManager;
import com.hc.posterccb.subscriber.CommonSubscriber;
import com.hc.posterccb.util.system.AppVersionTools;
import com.hc.posterccb.util.system.ControlUtils;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.file.FileUtils;
import com.hc.posterccb.util.JsonUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.system.NetworkUtil;
import com.hc.posterccb.util.SpUtils;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.system.VolumeUtils;
import com.hc.posterccb.util.ccbutils.XmlUtils;
import com.hc.posterccb.util.download.MD5;
import com.hc.posterccb.util.download.SFTPUtils;
import com.hc.posterccb.util.encrypt.DesDecUtils;
import com.hc.posterccb.util.encrypt.SilentInstall;
import com.thoughtworks.xstream.XStream;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.hc.posterccb.util.file.FileUtils.getStringFromTxT;

/**
 * Created by alex on 2017/7/10.
 */
public class MainModel extends BaseModel {

    private String TAG = "MainModel";

    private SFTPUtils mSFTPUtils = new SFTPUtils();

    private volatile int mPollingTimer; //轮询时间
    private TaskReportBean mTaskReport = new TaskReportBean();

    private boolean mLicensed = false;
    private XmlPullParser mParser;
    private RequestManager requestManager = RequestManager.getInstance();
    private ProApplication mApplication = new ProApplication();


    //轮询任务
    public void pollingTask(@NonNull final String command, @NonNull final String mac, @NonNull final InfoHint infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");
        mPollingTimer = (int) SpUtils.get("selectinterval", 10);//从sp获取轮询时间，默认10秒
        Observable.interval(mPollingTimer, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        httpService.polling((String) SpUtils.get("polling", "/xmlserver/revXml"), command, mac)
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

    public void init() {
        configSystem();
    }

    //系统配置
    public void configSystem() {
        String jsonStr = getStringFromTxT(Constant.LOCAL_CONFIG_TXT);
        if (!jsonStr.equals("")) {
            Gson gson = new Gson();
            ConfigBean bean = gson.fromJson(jsonStr, ConfigBean.class);
            Log.e("系统配置", bean.toString() + "---");
            String powerOnTimeStr = bean.getStaruptime();//开机时间
            String powerOffTimeStr = bean.getShutdowntime();//关机时间
            Date powerOnTime = DateFormatUtils.string2Date(powerOnTimeStr, "HH:mm");
            Date powerOffTime = DateFormatUtils.string2Date(powerOffTimeStr, "HH:mm");
            int diskspacealarm = Integer.parseInt(bean.getDiskspacealarm());//硬盘警告阀值
            SpUtils.put(mApplication.getString(R.string.diskspacealarm), diskspacealarm);
            String serverip = bean.getServerconfig();//服务器信息  http://ip:port/appname
            SpUtils.put(mApplication.getString(R.string.serverip), serverip);//保存服务器信息
            String sftpServer = bean.getFtpserver();//sftp下载服务器地址列表
            SpUtils.put(mApplication.getString(R.string.sftpserver), sftpServer);
            String httpServer = bean.getHttpserver();//http下载服务器地址列表
            SpUtils.put(mApplication.getString(R.string.httpserver), httpServer);
            int selectinterval = Integer.parseInt(bean.getSelectinterval());//轮询时间
            mPollingTimer = selectinterval;
            SpUtils.put(mApplication.getString(R.string.selectinterval), selectinterval);//保存轮询时间
            int volume = Integer.parseInt(bean.getVolume());//终端音量 机器最高音量为15
            VolumeUtils.setVolum(volume);
            int downloadrate = Integer.parseInt(bean.getVolume());//下载速度
            String[] downloadtimeArray = bean.getDownloadtime().split(",");//下载时间段
            Set<String> downloadtime = new HashSet<>();
            for (String time : downloadtimeArray) {
                downloadtime.add(time);
            }
            SpUtils.put(mApplication.getString(R.string.downloadtime), downloadtime);

            String logServer = bean.getLogserver();//日志服务器路径:ftp://user:pwd@serverip:port/logdir
            SpUtils.put(mApplication.getString(R.string.logServer), logServer);
            String upLoadLogTime = bean.getUploadlogtime();//日志定时上传时间，格式: HH:MM:SS
            SpUtils.put(mApplication.getString(R.string.upLoadLogTime), upLoadLogTime);
            String keeplogTime = bean.getKeeplogtime();//日志保留时间，单位：天
            SpUtils.put(mApplication.getString(R.string.keeplogTime), keeplogTime);

            Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = DateFormatUtils.date2String(date, "HH:mm");
//                            Log.e("现在时间", currentTime + "---");

                            try {
                                //定时关机
                                if (date.getTime() == powerOffTime.getTime()) {
                                    ControlUtils.writeFile("0");
                                }
                                //定时开机
                                if (date.getTime() == powerOnTime.getTime()) {
                                    ControlUtils.writeFile("1");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    //处理轮询任务返回
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
                resProgram(list, infoHint);
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
                reportCfg(bean);
                break;
            }
            //终端配置信息日志上报任务
            case Constant.POLLING_WORKSTATUSREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                reportWorkStatus(bean);
                break;
            }
            //终端工作状态上报类任务
            case Constant.POLLING_MONITORREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                reportMonitor(bean);
                break;
            }

            //通知终端上报日志
            case Constant.POLLING_LOGREPORT: {
                LogReportBean bean = (LogReportBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                reportLog(bean);
                break;
            }

            //通知终端下载资源
            case Constant.POLLING_DOWNLOADRES: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                ArrayList<DownLoadFileBean> list = postResult.getList();
                resDownLoadFile(list);
                break;
            }

            //通知终端上报下载状态
            case Constant.POLLING_DOWNLOADSTATUSREPORT: {
                PollResultBean bean = (PollResultBean) postResult.getBean();
                postTaskreport(bean.getTasktype(), bean.getTaskid());
                resDownLoadStatusReport(bean);
            }
        }
    }

    //终端日志上报任务
    private void reportLog(LogReportBean bean) {

    }

    //终端在播内容上报
    private void reportMonitor(PollResultBean bean) {
        String jsonStr = getStringFromTxT(Constant.LOCAL_PROGRAM_TXT);
        if (!jsonStr.equals("")) {

        }

    }

    //终端工作状态上报
    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void reportWorkStatus(PollResultBean bean) {

        PowerManager pm = (PowerManager) mApplication.getSystemService(Context.POWER_SERVICE);
        boolean screen = pm.isInteractive();
        ReportWorkStatusBean postBean = new ReportWorkStatusBean();
        postBean.setTaskid("001");
        if (screen) {
            postBean.setPlayerstatus("wakeup");
        } else {
            postBean.setPlayerstatus("sleep");
        }

        XStream xStream = new XStream();
        String postStr = xStream.toXML(postBean);
        StringUtils.setEncoding(postStr, "UTF-8");
        httpService.report((String) SpUtils.get(mApplication.getString(R.string.serverip), Api.LOCALHOST), Constant.REPORT_CONFIG, Constant.MAC, postStr)
                .subscribeOn(Schedulers.io())
                .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                    @Override
                    public void onNext(ResponseBody body) {
                        reportRequest(body, "工作状态");
                    }
                });
    }

    //终端配置信息日志上报
    private void reportCfg(PollResultBean bean) {
        String jsonStr = getStringFromTxT(Constant.LOCAL_CONFIG_TXT);
        if (!jsonStr.equals("")) {
            Gson gson = new Gson();
            ConfigBean configbean = gson.fromJson(jsonStr, ConfigBean.class);
            CfgReportBean postBean = new CfgReportBean();
            String ip = NetworkUtil.getLocalIp();
            postBean.setIp(ip);
            String mac = Constant.MAC;
            postBean.setMac(mac);
            String appversion = AppVersionTools.getApkVersionName(mApplication.getApplicationContext());
            postBean.setAppversion(appversion);
            String startuptime = configbean.getStaruptime();
            postBean.setStartuptime(startuptime);
            String shutdowntime = configbean.getShutdowntime();
            postBean.setShutdowntime(shutdowntime);
            String diskspacealarm = configbean.getDiskspacealarm();
            postBean.setDiskspacealarm(diskspacealarm);
            String serverconfig = configbean.getServerconfig();
            postBean.setServerconfig(serverconfig);
            String selectinterval = configbean.getSelectinterval();
            postBean.setSelectinterval(selectinterval);
            String volume = configbean.getVolume();
            postBean.setVolume(volume);
            String ftpserver = configbean.getFtpserver();
            postBean.setFtpserver(ftpserver);
            String httpserver = configbean.getHttpserver();
            postBean.setHttpserver(httpserver);
            String downloadrate = configbean.getDownloadrate();
            postBean.setDownloadrate(downloadrate);
            String downloadtime = configbean.getDownloadtime();
            postBean.setDownloadtime(downloadtime);
            String logserver = configbean.getLogserver();
            postBean.setLogserver(logserver);
            String uploadlogtime = configbean.getUploadlogtime();
            postBean.setUploadlogtime(uploadlogtime);
            String keeplogtime = configbean.getKeeplogtime();
            postBean.setKeeplogtime(keeplogtime);

            XStream xStream = new XStream();
            String postStr = xStream.toXML(postBean);
            StringUtils.setEncoding(postStr, "UTF-8");
            httpService.report((String) SpUtils
                    .get(mApplication.getString(R.string.serverip), Api.LOCALHOST), Constant.REPORT_CONFIG, Constant.MAC, postStr)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                        @Override
                        public void onNext(ResponseBody body) {
                            reportRequest(body, "配置信息");
                        }
                    });
        }
    }

    //上报任务通用返回处理
    private void reportRequest(ResponseBody body, String task) {
        String resStr = null;
        try {
            //获取返回的xml 字符串
            resStr = body.string();
            mParser = XmlUtils.getXmlPullParser(resStr);
            PostResult postResult = XmlUtils.getBeanByParseXml(mParser, Constant.XML_LISTTAG, TempBean.class, Constant.XML_STARTDOM, ReportIdReqBean.class);
            ReportIdReqBean mStatus = (ReportIdReqBean) postResult.getBean();
            int status = Integer.getInteger(mStatus.getResult());
            if (status == 0) {
                LogUtils.e("上报响应", "上报" + task + "响应成功");
            } else {
                LogUtils.e("上报响应", "上报" + task + "错误:" + mStatus.getResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private void resDownLoadStatusReport(PollResultBean bean) {

        Observable.just(Constant.LOCAL_RESOURCE_LIST_PATH, Constant.LOCAL_INSERT_RESOURCE_LIST_PATH)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        File file = new File(s);
                        if (file.exists()) {
                            String xmlStr = getStringFromTxT(s);
                            if (xmlStr.equals("")) return;
                            ArrayList<ResourceBean> resourceList = new ArrayList<>();
                            resourceList = XmlUtils.parseResource(xmlStr);
                            if (resourceList.size() == 0) return;
                            reportDownLoadStatus(resourceList);
                        }
                    }


                });

    }

    private void reportDownLoadStatus(ArrayList<ResourceBean> list) {
        ReportDownloadStatus status = new ReportDownloadStatus();

        Observable.just(list)
                .flatMap(new Func1<ArrayList<ResourceBean>, Observable<ResourceBean>>() {
                    @Override
                    public Observable<ResourceBean> call(ArrayList<ResourceBean> been) {
                        return Observable.from(been);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ResourceBean>() {
                    @Override
                    public void call(ResourceBean bean) {
                        File file = new File(Constant.LOCAL_PROGRAM_PATH + "/" + bean.getResname());//测试
                        boolean md5 = MD5.decode(file, bean.getMd5());
                        if (file.exists() && md5) {
                            status.setStatus("0100");
                            return;
                        }
                    }
                });
    }

    //通知终端下载资源文件列表
    private void resDownLoadFile(List<DownLoadFileBean> list) {

        mSFTPUtils.setPassword("123456");
        mSFTPUtils.setUsername("Alex");
        String tempFileName = "";

        Observable.just(list)
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mSFTPUtils.disconnect();
                        Log.e(TAG, "下载资源文件断开连接");
                    }
                })
                .flatMap(new Func1<List<DownLoadFileBean>, Observable<DownLoadFileBean>>() {
                    @Override
                    public Observable<DownLoadFileBean> call(List<DownLoadFileBean> list) {
                        return Observable.from(list);
                    }
                }).subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DownLoadFileBean>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "下载资源文件成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "下载资源文件列表失败");

                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(DownLoadFileBean bean) {

                        String url = (String) SpUtils.get("baseurl", Api.BASE_URL);
                        mSFTPUtils.setHost(url);
                        Log.e(TAG, "下载资源文件列表");
                        String localPath = Constant.UDP_TESTPATH;//测试
//                            String localPath=Constant.LOCAL_PROGRAM_MEDIACFG_PATH;//本地存储
                        String remotePath = "test1/";//测试
//                            String remotePath= Protocol.getLinkRemotePath(bean.getLink());
//                            String fileName=Protocol.getLinkFileName(bean.getLink());
                        mSFTPUtils.connect();
                        Log.e(TAG, "连接成功");
                        mSFTPUtils.downloadFile(remotePath, "aec.mp4", localPath, "test.mp4");//测试
//                            mSFTPUtils.downloadFile(remotePath,fileName,localPath,fileName);
                        File file = new File(localPath + "/test.mp4");//测试

//                        File file = new File(localPath+"/"+fileName);
                        if (MD5.decode(file, bean.getMd5())) {
                            Log.e(TAG, "下载资源文件列表成功");
                            downLoadResource(file.getPath());
                        } else {
                            Log.e(TAG, "资源文件列表文件非法");
                        }
                    }
                });
    }

    //下载资源文件
    private void downLoadResource(String path) {

        String xmlStr = getStringFromTxT(path);
        if (xmlStr.equals("")) return;
        ArrayList<ResourceBean> resourceList = new ArrayList<>();
        resourceList = XmlUtils.parseResource(xmlStr);
        if (resourceList == null || resourceList.size() == 0) return;
        mApplication.setResourceBeanList(resourceList);
        mApplication.initDetailBeanList(resourceList);
        SFTPUtils sFTPUtils = new SFTPUtils();
        Observable.interval(1, TimeUnit.SECONDS)
                .just(resourceList)
                .flatMap(new Func1<List<ResourceBean>, Observable<ResourceBean>>() {
                    @Override
                    public Observable<ResourceBean> call(List<ResourceBean> been) {
                        return Observable.from(been);
                    }
                })
                //结束时操作
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        sFTPUtils.disconnect();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ResourceBean>() {
                    @Override
                    public void onCompleted() {
                        File file = new File(Constant.LOCAL_PROGRAM_PATH + "/" + mApplication.getResourceBean().getResname());//测试
                        boolean md5 = MD5.decode(file, mApplication.getResourceBean().getMd5());
                        if (md5) {
                            setDownloadStatus("0001");//设置状态下载成功
                        } else {
                            setDownloadStatus("0104");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        setDownloadStatus("0");//设置状态下载出错
                    }

                    @Override
                    public void onNext(ResourceBean bean) {
                        mApplication.setResourceBean(bean);
                        setDownloadStatus("0002");//设置状态下载中
                        String localPath = Constant.UDP_TESTPATH;//测试
//                            String localPath=Constant.LOCAL_PROGRAM_PATH;//
                        File file = new File(localPath + "/test.mp4");//测试
//                        double fileSize=FileUtils.getFileOrFilesSize(file.getPath(),SIZETYPE_KB);
                        boolean md5 = MD5.decode(file, bean.getMd5());
                        if (file.exists() && md5) {
                            return;
                        }
                        Date date = new Date(System.currentTimeMillis());
                        String dateStr = DateFormatUtils.date2String(date, "HH:mm");
                        Set<String> tempSet = new HashSet<>();
                        if (DateFormatUtils.checkTimer(dateStr, SpUtils.get("downloadtime", tempSet))) {
                            sFTPUtils.setUsername("");
                            sFTPUtils.setPassword("");
                            String url = (String) SpUtils.get("baseurl", Api.BASE_URL);
                            sFTPUtils.setHost(url);
                            Log.e(TAG, "下载资源文件");
                            String remotePath = "test1/";//测试
//                            String remotePath= Protocol.getLinkRemotePath(bean.getFtpAdd());
//                            String fileName=Protocol.getLinkFileName(bean.getFtpAdd());
                            mSFTPUtils.connect();
                            Log.e(TAG, "连接成功");

//                            if (mSFTPUtils.isDirExist(remotePath + "/" + fileName))
//                                setDownloadStatus("0101");

                            mSFTPUtils.downloadFile(remotePath, "aec.mp4", localPath, "test.mp4");//测试
                            if (MD5.decode(file, bean.getMd5())) {
                                Log.e(TAG, "下载资源文件" + file.getName() + "成功");
                            } else {
                                Log.e(TAG, "资源文件" + file.getName() + "非法");
                            }
                        }
                    }
                });
    }

    //设置单个资源文件的下载状态
    private void setDownloadStatus(String errorType) {
        for (int i = 0; i < mApplication.getDetailBeanList().size(); i++) {
            String resName = mApplication.getResourceBean().getResname();
            String tarName = mApplication.getDetailBeanList().get(i).getFilename();
            if (tarName.equals(resName)) {
                mApplication.getDetailBeanList().get(i).setStatus(errorType);
            }
        }
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

    //播放节目单类任务
    private void resProgram(ArrayList<ProgramBean> list, InfoHint infoHint) {

        String arrayStr = JsonUtils.ArrayList2JsonStr(list);
        LogUtils.e("resProgram", arrayStr);
        try {
            FileUtils.coverTxtToFile(arrayStr, Constant.LOCAL_PROGRAM_TXT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Observable.just(list)
                .flatMap(new Func1<ArrayList<ProgramBean>, Observable<ProgramBean>>() {
                    @Override
                    public Observable<ProgramBean> call(ArrayList<ProgramBean> list) {
                        return Observable.from(list);
                    }
                }).subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ProgramBean>() {
                    @Override
                    public void call(ProgramBean bean) {
                        downloadProgram(bean, infoHint);
                    }
                });
    }

    //下载播放节目单
    private void downloadProgram(ProgramBean bean, InfoHint infoHint) {
        LogUtils.e("ProgramBean", bean.toString());
        String url = bean.getLink();
        String[] array = url.split("/");
        if (array.length == 0) return;
        int playmode = Integer.parseInt(bean.getPlaymode());
        String filename = array[array.length - 1];
        if (playmode == 1) {
            filename = "program.txt";
        } else {
            filename = "insert_program.txt";
        }
        String finalFilename = filename;

        requestManager.downLoadFile(filename, (String) SpUtils.get("baseurl", Api.BASE_URL) + url, Constant.LOCAL_PROGRAM_PATH, new RequestManager.ReqCallBack<File>() {
            @Override
            public void onReqSuccess(File result) {
                if (MD5.decode(result, bean.md5)) {
                    LogUtils.e("下载播放列表", finalFilename + "下载成功");
                    if (playmode == 1) {
                        logicNomalProgram(infoHint);
                    } else {
                        logicInterProgram(infoHint);
                    }
                } else {
                    LogUtils.e("下载播放列表", finalFilename + "下载失败");
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.e("resUpgrade", finalFilename + "下载失败");
            }
        });
    }

    //正常播放任务逻辑
    private void logicNomalProgram(InfoHint infoHint) {

        String xmlNormalStr = getStringFromTxT(Constant.LOCAL_PROGRAM_NORMAL_TXT);
        if (xmlNormalStr.equals("")) {
            LogUtils.e(TAG, "正常播放节目单为空");
            return;
        }
        List<Program> list = new ArrayList<>();
        list = XmlUtils.parseNormalProgramXml(xmlNormalStr);
        for (Program program : list) {
            if (program.getType().equals("defaultpls")) {
                mApplication.setDefProgram(program);
                SpUtils.put("defaultpls", program);
            }
        }
        Observable.interval(1, TimeUnit.SECONDS)
                .just(list)
                .flatMap(new Func1<List<Program>, Observable<Program>>() {
                    @Override
                    public Observable<Program> call(List<Program> programs) {
                        return Observable.from(programs);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Program>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Program program) {
                                   if (program.getType().equals("defaultpls")) return;
                                   Date date = new Date(System.currentTimeMillis());
                                   Date stdtime = DateFormatUtils.string2Date(program.getStdtime(), "HH:ss");
                                   Date edtime = DateFormatUtils.string2Date(program.getEdtime(), "HH:ss");
                                   Program curProgram = mApplication.getProgram();
                                   //
                                   if (date.after(stdtime) && date.before(edtime)) {
                                       if (mApplication.isPlay() && curProgram.equals(program)) {
                                           return;
                                       } else {
                                           infoHint.logicNormalProgram(program);
                                       }
                                       Log.e("正常播放节目单", program.toString() + "---");
                                   } else if (program.getStdtime().equals("") && program.getEdtime().equals("")) {
                                       if (mApplication.isPlay() && curProgram.equals(program)) {
                                           return;
                                       } else {
                                           infoHint.logicNormalProgram(program);
                                       }
                                   } else {
                                       infoHint.logicNormalProgram(mApplication.getDefProgram());
                                   }
                               }
                           }
                );
    }

    //插播播放任务逻辑
    private void logicInterProgram(InfoHint infoHint) {
        String xmlInterStr = getStringFromTxT(Constant.LOCAL_PROGRAM_INTER_TXT);
        if (xmlInterStr.equals("")) {
            LogUtils.e(TAG, "插播播放节目单为空");
            return;
        }

        Program program = XmlUtils.parseInterProgramXml(xmlInterStr);

        Observable.just(program)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Program>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Program program) {
                                   infoHint.logicNormalProgram(program);
                                   Log.e("插播播放节目单", program.toString() + "---");
                               }
                           }
                );
    }

    //升级任务
    private void resUpgrade(UpGradeBean bean) {
        String jsonStr = JsonUtils.Bean2JsonStr(bean);
        SpUtils.put("UpGradeBean", jsonStr);

        requestManager.downLoadFile("upgrade.txt", (String) SpUtils.get("baseurl", Api.BASE_URL) + bean.getLink(), Constant.LOCAL_APK_PATH, new RequestManager.ReqCallBack<File>() {
            @Override
            public void onReqSuccess(File result) {
                LogUtils.e("resUpgrade", "升级配置文件下载成功");
                String xmlStr = FileUtils.getStringFromTxT(result.getPath());
                ArrayList<UpGradeCfgBean> list = XmlUtils.parseUpGradeXml(xmlStr);
                String href = (String) SpUtils.get("baseurl", Api.BASE_URL) + list.get(0).getHref();
                downloadApk(href);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.e("resUpgrade", "升级配置文件下载失败");
            }
        });
        LogUtils.e("resResUpgrade", bean.toString());
    }

    //下载升级apk文件
    private void downloadApk(String href) {
        requestManager.downLoadFile("upgrade.apk", href, Constant.LOCAL_APK_PATH, new RequestManager.ReqCallBack<File>() {
            @Override
            public void onReqSuccess(File file) {
                LogUtils.e("downloadApk", "升级文件下载失败");
                try {
                    if (AppVersionTools.needUpdate(ProApplication.getmContext())) {
                        Log.e("", "需要更新");
                        boolean result = SilentInstall.install(file.getPath());
                        Log.d("安装是否成功", result + "");
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.e("downloadApk", "升级文件下载失败");
            }
        });
    }

    //即时消息类任务
    private void resRealTimeMsg(RealTimeMsgBean bean, final InfoHint infoHint) {
        String jsonStr = JsonUtils.Bean2JsonStr(bean);
        SpUtils.put("realtime", jsonStr);
        realTimeTimer(infoHint);
    }

    //即时消息类
    private void realTimeTimer(final InfoHint infoHint) {
        Gson gson = new Gson();
        String jsonStr = (String) SpUtils.get("realtime", "");
        if (jsonStr.equals("")) return;
        RealTimeMsgBean bean = gson.fromJson(jsonStr, RealTimeMsgBean.class);
        infoHint.realTimeMessage(bean);
        final String startTimeStr = bean.getStarttime();
        SpUtils.put("StartRealTimeMsg", startTimeStr);
        final String endTimeStr = bean.getEndtime();
        SpUtils.put("EndRealTimeMsg", endTimeStr);

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
                        if (date.getTime() == startTime.getTime()) {
                            infoHint.realtimeStart();
                        }
                        if (date.getTime() == endTime.getTime()) {
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

    //上报任务id
    private void postTaskreport(String tasktype, String taskid) {
        XStream xStream = new XStream();
        mTaskReport.setTasktype(tasktype);
        mTaskReport.setTaskid(taskid);
        mTaskReport.setStatus("0000");
        xStream.alias("command", TaskReportBean.class);
        String postStr = xStream.toXML(mTaskReport);
        StringUtils.setEncoding(postStr, "UTF-8");

        httpService.report((String) SpUtils.get("logurl", "/xmlserver/revXml"), Constant.TASKREPORT, Constant.MAC, postStr)
                .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                    @Override
                    public void onNext(ResponseBody body) {
                        reportRequest(body, "任务id");
                    }
                });
    }

    //检查授权
    public void checkLisence(final Config config) {
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

    //解密
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

    //保存sp
    private void saveData() {
        SpUtils.put("Licensed", mLicensed);
    }

    //获取sp
    private void getData() {
        mLicensed = (boolean) SpUtils.get("Licensed", false);
    }

    //上传日志
    private void uploadLog() {

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

        //播放类逻辑
        void logicNormalProgram(Program bean);

        void logicInterProgram(Program bean);


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
