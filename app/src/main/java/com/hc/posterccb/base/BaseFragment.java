package com.hc.posterccb.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hc.posterccb.Constant;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.mvp.IView;
import com.hc.posterccb.ui.acitivity.MainActivity;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.download.DownFileUtils;
import com.hc.posterccb.util.file.MediaFileUtil;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView, MainActivity.ActivityInteraction {
    protected AVOptions options;
    protected View rootView;
    protected P mPresenter;
    protected Unbinder mUnbinder;
    protected Context mContext;
    protected String TAG = "Fragment";
    protected boolean isOverTime;//是否在定时播放内
    protected ProApplication mApplication = ProApplication.getInstance();

    Timer timer1 = new Timer();
    Timer timer2 = new Timer();
    Timer timer3 = new Timer();
    private volatile int mRecArea1 = 40;
    private volatile int mRecArea2 = 40;
    private volatile int mRecArea3 = 40;
    String mArea = "";

    protected Subscription mSubscriptionAreaPlay;

    private Subscription mSubscriptionTimerArea1;
    private Subscription mSubscriptionTimerArea2;
    private Subscription mSubscriptionTimerArea3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        mPresenter = loadPresenter();
        initCommonData();
        initView();
        initListener();
        initData();
        setOptions();
        initView();
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    protected abstract void initListener();

    protected abstract void initData();

    private void initCommonData() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected void setOptions() {
        options = new AVOptions();

        int codec = AVOptions.MEDIA_CODEC_AUTO;
// 解码方式:
// codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
// codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
// codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
// 默认值是：MEDIA_CODEC_SW_DECODE
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);

// 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
// 默认值是：无
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

// 读取视频流超时时间，单位是 ms
// 默认值是：10 * 1000
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);

//// 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
//// 默认值是：0
//        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);

//// 是否开启"延时优化"，只在在线直播流中有效
//// 默认值是：0
//        options.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

// 默认的缓存大小，单位是 ms
// 默认值是：2000
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 2000);

// 最大的缓存大小，单位是 ms
// 默认值是：4000
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);

// 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
// 默认值是：1
        options.setInteger(AVOptions.KEY_START_ON_PREPARED, 1);

// 播放前最大探测流的字节数，单位是 byte
// 默认值是：128 * 1024
        options.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);

// 是否开启音视频播放时间戳回调
// 默认值是：0
        options.setInteger(AVOptions.KEY_AUDIO_RENDER_MSG, 1);
        options.setInteger(AVOptions.KEY_VIDEO_RENDER_MSG, 1);

    }

    protected abstract P loadPresenter();

    //获取布局文件
    protected abstract int getLayoutResource();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //初始化view
    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    //视频播放控件的错误监听器
    protected PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    LogUtils.e(TAG, "Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    LogUtils.e(TAG, "404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    LogUtils.e(TAG, "Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    LogUtils.e(TAG, "Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    LogUtils.e(TAG, "Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    LogUtils.e(TAG, "Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    LogUtils.e(TAG, "Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    LogUtils.e(TAG, "Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    LogUtils.e(TAG, "Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    LogUtils.e(TAG, "Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    LogUtils.e(TAG, AVOptions.MEDIA_CODEC_SW_DECODE + "");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    LogUtils.e(TAG, "unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    //检查提交参数是否为空
    public boolean checkPostParamNull() {
        boolean isNull = false;
//        if (StringUtils.isEmpty(mProgramsPath01)) {
//            LogUtils.e(TAG, "任务名称为空");
//            isNull = true;
//        }
//        else if (StringUtils.isEmpty(mSerialNumber)) {
//            LogUtils.e(TAG, "序列号为空");
//            isNull = true;
//        }
        return isNull;
    }


    //按照位置播放
    protected void areaPlay(List<ProgramRes> programResList) {
        Collections.sort(programResList);
        List<Date> limitTime = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis());
        String sysTime = DateFormatUtils.date2String(date, "yyyyMMdd ");
        for (ProgramRes res : programResList) {
            if (res.getStdtime() != null && res.getEdtime() != null && !res.getStdtime().equals("") && !res.getEdtime().equals("")) {
                Date sttime = DateFormatUtils.string2Date(sysTime + res.getStdtime(), "yyyyMMdd HH:mm");
                Date edtime = DateFormatUtils.string2Date(sysTime + res.getEdtime(), "yyyyMMdd HH:mm");
                limitTime.add(sttime);
                limitTime.add(edtime);
            }
        }
        mSubscriptionAreaPlay = Observable.interval(1, TimeUnit.SECONDS)
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mApplication.getPlaycntMap() != null) {
                            HashMap<String, Integer> map = mApplication.getPlaycntMap();
                            boolean needToPlay = false;
                            for (String key : map.keySet()) {
                                int cnt = map.get(key);
                                if (cnt >= 0) {
                                    needToPlay = true;
                                }
                            }
                            mApplication.setNormalPlay(needToPlay);
                            mApplication.setAreaIsPlay(needToPlay);
                        }
                        for (int n =0; n<programResList.size(); n++) {
                            ProgramRes programRes=programResList.get(n);
                            Date date = new Date(System.currentTimeMillis());
                            String path = Constant.LOCAL_PROGRAM_PATH + "/" + DownFileUtils.getResourceName(programRes.getResnam());
                            boolean isImg = MediaFileUtil.isImageFileType(path);
                            if (limitTime.size() > 0 && limitTime.size() % 2 == 0) {
                                for (int i = 0; i < limitTime.size(); i = i + 2) {
                                    if (limitTime.get(i).getTime() < date.getTime()
                                            && date.getTime() < limitTime.get(i + 1).getTime()) {
                                        isOverTime = true;
                                    }
                                }
                            } else if (limitTime.size() == 0) {
                                isOverTime = true;
                            }
                            String area = programRes.getArea();
                            boolean picPlayIsover = false;
                            boolean indexIsMore=false;
                            switch (area) {
                                case "area1":
                                    picPlayIsover = mApplication.isPicPlayArea1IsOver();
                                    indexIsMore=n>mApplication.getProresIndex1();
                                    break;
                                case "area2":
                                    picPlayIsover = mApplication.isPicPlayArea2IsOver();
                                    indexIsMore=n>mApplication.getProresIndex2();
                                    break;
                                case "area3":
                                    picPlayIsover = mApplication.isPicPlayArea3IsOver();
                                    indexIsMore=n>mApplication.getProresIndex3();
                                    break;
                            }
                            Log.e(TAG, "循环到的图片是否在播放" + picPlayIsover);
                            if (isOverTime && !picPlayIsover&&indexIsMore) {
                                setAreaView(date, programRes, isOverTime);
                                switch (area) {
                                    case "area1":
                                        if (n==programResList.size()-1){
                                            mApplication.setProresIndex1(-1);
                                        }else {
                                            mApplication.setProresIndex1(n);
                                        }

                                        break;
                                    case "area2":
                                        if (n==programResList.size()-1){
                                            mApplication.setProresIndex2(-1);
                                        }else {
                                            mApplication.setProresIndex2(n);
                                        }
                                        break;
                                    case "area3":
                                        if (n==programResList.size()-1){
                                            mApplication.setProresIndex3(-1);
                                        }else {
                                            mApplication.setProresIndex3(n);
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    //设置播放节目单中的资源播放位置接口

    protected abstract void setAreaView(Date date, ProgramRes programRes, boolean isovertime);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void setVideoView(RelativeLayout layout, PLVideoView view, ProgramRes bean, Date date, boolean isovertime) {
        ProgramRes appRes = new ProgramRes();
        String area = bean.getArea();
        switch (area) {
            case "area1":
                appRes = mApplication.getArea1ProgramRes();
                break;
            case "area2":
                appRes = mApplication.getArea2ProgramRes();
                break;
            case "area3":
                appRes = mApplication.getArea3ProgramRes();
                break;
        }
        String path = Constant.LOCAL_PROGRAM_PATH + "/" + DownFileUtils.getResourceName(bean.getResnam());
        int playtimes = (bean.getPlaycnt() == null) ? 0 : Integer.parseInt(bean.getPlaycnt());
        File file = new File(path);
        if (!file.exists()) return;
        if (bean.getResnam() == null || bean.getResnam().equals("")) return;
        int playcnt = 0;
        if (bean.getPlaycnt() != null && mApplication.getPlaycntMap() != null && mApplication.getPlaycntMap().size() != 0) {
            playcnt = mApplication.getPlaycntMap().get(bean.getResnam());
        }
        if (bean.getStdtime() == null && bean.getEdtime() == null && !isOverTime) {
            int priority = (bean.getPriority() == null) ? 0 : Integer.parseInt(bean.getPriority());
            int appPriority = (appRes.getPriority() == null) ? 0 : Integer.parseInt(appRes.getPriority());

            if (playcnt >= 0 && !view.isPlaying() && !bean.equals(appRes) && priority >= appPriority) {
                switch (area) {
                    case "area1":
                        mApplication.setArea1ProgramRes(bean);
                        break;
                    case "area2":
                        mApplication.setArea2ProgramRes(bean);
                        break;
                    case "area3":
                        mApplication.setArea3ProgramRes(bean);
                        break;
                }
                mApplication.getPlaycntMap().put(bean.getResnam(), --playcnt);
                nomalResPlay(layout, view, path, bean);
            }
        } else if (isovertime) {
            String sysTime = DateFormatUtils.date2String(date, "yyyyMMdd ");
            Date startPlayTime = null;
            Date endPlayTime = null;

            boolean programResIsIntime = false;
            if (bean.getStdtime() == null || bean.getEdtime() == null || bean.getStdtime().equals("") || bean.getEdtime().equals("")) {
                programResIsIntime = true;
                Log.e(TAG, bean.getResnam() + "节目在指定播放时间内");
            } else {
                startPlayTime = DateFormatUtils.string2Date(sysTime + bean.getStdtime(), "yyyyMMdd HH:mm");
                endPlayTime = DateFormatUtils.string2Date(sysTime + bean.getEdtime(), "yyyyMMdd HH:mm");
                if (startPlayTime.getTime() < date.getTime() && date.getTime() < endPlayTime.getTime()) {
                    programResIsIntime = true;
                    Log.e(TAG, bean.getResnam() + "节目在指定播放时间内");
                }
            }
            int priority = (bean.getPriority() == null) ? 0 : Integer.parseInt(bean.getPriority());
            int appPriority = (appRes.getPriority() == null) ? 0 : Integer.parseInt(appRes.getPriority());
            LogUtils.e("播放次数", playcnt + bean.getResnam());
            if (playcnt >= 0 && !view.isPlaying() &&programResIsIntime&&priority >= appPriority) {
                mApplication.getPlaycntMap().put(bean.getResnam(), --playcnt);
                nomalResPlay(layout, view, path, bean);
                if (!appRes.equals(bean)) {
                    switch (area) {
                        case "area1":
                            mApplication.setArea1ProgramRes(bean);
                            break;
                        case "area2":
                            mApplication.setArea2ProgramRes(bean);
                            break;
                        case "area3":
                            mApplication.setArea3ProgramRes(bean);
                            break;
                    }
                }
            }
        }
    }

    /**
     * @param view
     *         播放控件
     * @param bean
     *         原子
     * @param path
     *         资源路径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void nomalResPlay(RelativeLayout layout, PLVideoView view, String path, ProgramRes bean) {
        String area = bean.getArea();
        boolean isImg = MediaFileUtil.isImageFileType(path);
        if (isImg) {
//            if (tempTimes[0] < playTimes || playTimes == 0) {
            LogUtils.e("nomalResPlay", mRecArea1 + "播放图片" + path);
            view.setVisibility(View.GONE);
            Drawable drawable = Drawable.createFromPath(path);
            layout.setBackground(drawable);
//                tempTimes[0]++;
            switch (area) {
                case "area1":
                    Log.e(TAG, "控件1播放图片");
                    if (mSubscriptionTimerArea1 != null && !mSubscriptionTimerArea1.isUnsubscribed()) {
                        mSubscriptionTimerArea1.unsubscribe();
                    }
                    setPicCountdown("area1");
                    mApplication.setPicPlayArea1IsOver(true);
                    break;
                case "area2":
                    Log.e(TAG, "控件2播放图片");
                    if (mSubscriptionTimerArea2 != null && !mSubscriptionTimerArea2.isUnsubscribed()) {
                        mSubscriptionTimerArea2.unsubscribe();
                    }
                    setPicCountdown("area2");
                    mApplication.setPicPlayArea2IsOver(true);
                    break;
                case "area3":
                    Log.e(TAG, "控件3播放图片");
                    if (mSubscriptionTimerArea3 != null && !mSubscriptionTimerArea3.isUnsubscribed()) {
                        mSubscriptionTimerArea3.unsubscribe();
                    }
                    setPicCountdown("area3");
                    mApplication.setPicPlayArea3IsOver(true);
                    break;
            }
//            }
        } else {
            LogUtils.e("nomalResPlay", "播放视频");
            view.setVisibility(View.VISIBLE);
            view.setVideoPath(path);
            view.setOnPreparedListener(new PLMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(PLMediaPlayer player, int i) {
                    view.seekTo(0);
                    view.start();
                }
            });
            view.setOnErrorListener(new PLMediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(PLMediaPlayer player, int i) {
                    return false;
                }
            });
            view.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(PLMediaPlayer player) {
                    view.stopPlayback();
//                    tempTimes[0]++;
//                    if (tempTimes[0] < playTimes || playTimes == 0) {
//                        view.setVideoPath(path);
//                        view.seekTo(0);
//                        view.start();
//                    }
                }
            });
        }

    }

    void setPicCountdown(String area) {
        switch (area) {
            case "area1":
                mSubscriptionTimerArea1 = Observable
                        .timer(40, TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mApplication.setPicPlayArea1IsOver(false);
                                LogUtils.e("倒数结束", "图片1是否在播放" + mApplication.isPicPlayArea1IsOver());
                            }
                        });
                break;
            case "area2":
                mSubscriptionTimerArea2 = Observable
                        .timer(40, TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mApplication.setPicPlayArea2IsOver(false);
                                LogUtils.e("倒数结束", "图片1是否在播放" + mApplication.isPicPlayArea2IsOver());
                            }
                        });
                break;
            case "area3":
                mSubscriptionTimerArea3 = Observable
                        .timer(40, TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mApplication.setPicPlayArea3IsOver(false);
                                LogUtils.e("倒数结束", "图片1是否在播放" + mApplication.isPicPlayArea3IsOver());
                            }
                        });
                break;
        }
    }


}
