package com.hc.posterccb.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
import android.widget.VideoView;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.ui.acitivity.MainActivity;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.download.DownFileUtils;
import com.hc.posterccb.util.file.MediaFileUtil;
import com.pili.pldroid.player.AVOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class Second_H_Fragment extends Fragment implements
        MainActivity.ActivityInteraction {

    private static final String TAG = "Second_H_Fragment";

    private AVOptions options;
    private Context mContext;
    private Unbinder mUnbinder;

    @BindView(R.id.videoview_one3)
    VideoView mPLVideoViewOne3;

    @BindView(R.id.videoview_two3)
    VideoView mPLVideoViewTwo3;

    @BindView(R.id.rel_one3)
    RelativeLayout mRelOne3;

    @BindView(R.id.rel_two3)
    RelativeLayout mRelTwo3;
    private ProApplication mApplication = ProApplication.getInstance();

    private String mProgramsPath = Constant.VIDEO1_PATH;
    private Subscription mSubscriptionPlayProgram;

    private volatile Queue<ProgramRes> resQueueArea1;
    private volatile Queue<ProgramRes> resQueueArea2;


    private Subscription mSubscriptionAreaPlay;

    private Subscription mSubscriptionTimerArea1;
    private Subscription mSubscriptionTimerArea2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second__h_, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        initListener();
        initData();
        setOptions();
        return view;
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


    public void playProgram(Program program) {
        LogUtils.e("竖直全屏", "播放被调用");
        final List<ProgramRes> programResList = program.getList();
        if (programResList == null || programResList.size() == 0) {
            playError("播放列表为空");
            return;
        }
        mApplication.setPlaycntMap(new HashMap<String, Integer>());
        for (ProgramRes res : programResList) {
            mApplication.getPlaycntMap().put(res.getResnam(), Integer.parseInt(res.playcnt));
        }

        if (mSubscriptionPlayProgram != null && !mSubscriptionPlayProgram.isUnsubscribed()) {
            mSubscriptionPlayProgram.unsubscribe();
        }

        //正常播放
        if (program.getMode().equals(Constant.PROGRAM_MODE_NORMAL)) {
            Date date = new Date(System.currentTimeMillis());
            String currentTime = DateFormatUtils.date2String(date, "HH:mm");
//                            Log.e("现在时间", currentTime + "---");
            boolean isInTime = false;
            Date startPlayTime = null;
            Date endPlayTime = null;
            if (program.getStdtime().length() < 5 || program.getEdtime().length() < 5) {
                isInTime = true;
            } else {
                startPlayTime = DateFormatUtils.string2Date(program.getStdtime(), "HH:mm");
                endPlayTime = DateFormatUtils.string2Date(program.getEdtime(), "HH:mm");
                if (startPlayTime.getTime() < date.getTime() && date.getTime() < endPlayTime.getTime()) {
                    isInTime = true;
                }
            }

            try {
                //定时播放
                if (isInTime && !mApplication.isAreaIsPlay()) {
                    if (mSubscriptionAreaPlay != null && !mSubscriptionAreaPlay.isUnsubscribed() && mApplication.getDisplayModel().equals("model_second_h")) {
                        mSubscriptionAreaPlay.unsubscribe();
                    }
                    areaPlay(programResList);
                    mApplication.setAreaIsPlay(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSubscriptionPlayProgram = Observable.interval(1, TimeUnit.SECONDS)
                    .onTerminateDetach()
                    .onBackpressureDrop()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {

                        }
                    });
        } else if (program.getMode().equals(Constant.PROGRAM_MODE_DEF)) {
            if (mSubscriptionAreaPlay != null && !mSubscriptionAreaPlay.isUnsubscribed()) {
                mSubscriptionAreaPlay.unsubscribe();
            }
            areaPlay(programResList);
        } else {
            if (mSubscriptionAreaPlay != null && !mSubscriptionAreaPlay.isUnsubscribed()) {
                mSubscriptionAreaPlay.unsubscribe();
            }
            //插播播放
            areaPlay(programResList);
            mApplication.setAreaIsPlay(true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setAreaView(Date date, ProgramRes programRes) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(mRelOne3, mPLVideoViewOne3, programRes, date);
                break;
            case "area2":
                setVideoView(mRelTwo3, mPLVideoViewTwo3, programRes, date);
                break;
            default:
                setVideoView(mRelOne3, mPLVideoViewOne3, programRes, date);
                break;
        }

    }

    //按照位置播放
    private void areaPlay(List<ProgramRes> programResList) {
        Collections.sort(programResList);
        resQueueArea1 = new LinkedList<>();
        resQueueArea2 = new LinkedList<>();

        mSubscriptionAreaPlay = Observable.interval(1, TimeUnit.SECONDS)
                .onTerminateDetach()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void call(Long aLong) {
                        //当前时间
                        Date date = new Date(System.currentTimeMillis());
                        //当前时间字符串
                        String sysTime = DateFormatUtils.date2String(date, "yyyyMMdd ");
                        //ProgramRes播放时间范围
                        List<Date> limitTime = new ArrayList<>();
                        //ProgramRes是否在播放时间范围内
                        boolean resIsInTime = false;
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
                            mApplication.setInterIsPlay(needToPlay);
                        }
                        //1.设置模板中三个控件模块控件各自的节目队列
                        for (ProgramRes res : programResList) {
                            //如果ProgramRes有播放时间限制则添加到数组中
                            if (res.getStdtime() != null && res.getEdtime() != null && !res.getStdtime().equals("") && !res.getEdtime().equals("")) {
                                Date sttime = DateFormatUtils.string2Date(sysTime + res.getStdtime(), "yyyyMMdd HH:mm");
                                Date edtime = DateFormatUtils.string2Date(sysTime + res.getEdtime(), "yyyyMMdd HH:mm");
                                limitTime.add(sttime);
                                limitTime.add(edtime);
                            }
                            //判断ProgramRes是否在播放时间范围内
                            if (limitTime.size() > 0 && limitTime.size() % 2 == 0) {
                                for (int i = 0; i < limitTime.size(); i = i + 2) {
                                    if (limitTime.get(i).getTime() < date.getTime()
                                            && date.getTime() < limitTime.get(i + 1).getTime()) {
                                        resIsInTime = true;
                                    }
                                }
                            } else if (limitTime.size() == 0) {
                                resIsInTime = true;
                            }
                            //如果ProgramRes再播放时间范围内则将
                            switch (res.getArea()) {
                                case "area1":
                                    if (resIsInTime) resQueueArea1.offer(res);
                                    break;
                                case "area2":
                                    if (resIsInTime) resQueueArea2.offer(res);
                                    break;
                            }
                        }
                        if (!mApplication.isArea1IsPlay() && resQueueArea1.size() > 0) {
                            queueArea1Play(resQueueArea1, date);
                        }

                        if (!mApplication.isArea2IsPlay() && resQueueArea2.size() > 0) {
                            queueArea2Play(resQueueArea2, date);
                        }
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void queueArea1Play(Queue<ProgramRes> queue, Date date) {
        while (queue.size() >= 0 && !mApplication.isArea1ResIsPlay()) {
            if (queue.size() == 0) {
                mApplication.setArea1IsPlay(false);
                mApplication.setArea1ResIsPlay(false);
            } else {
                mApplication.setArea1IsPlay(false);
                mApplication.setArea1ResIsPlay(true);
            }
            ProgramRes temp = queue.remove();
            setAreaView(date, temp);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void queueArea2Play(Queue<ProgramRes> queue, Date date) {
        while (queue.size() >= 0 && !mApplication.isArea2ResIsPlay()) {
            if (queue.size() == 0) {
                mApplication.setArea2IsPlay(false);
                mApplication.setArea2ResIsPlay(false);
            } else {
                mApplication.setArea2IsPlay(false);
                mApplication.setArea2ResIsPlay(true);
            }
            ProgramRes temp = queue.remove();
            setAreaView(date, temp);
        }
    }

    public void playSuccess(String msg) {

    }


    public void playError(String msg) {

    }

    private void initListener() {

    }


    protected void initData() {
    }

    private void initView() {
    }

    @Override
    public void pause() {
        mPLVideoViewOne3.pause();
    }

    @Override
    public void replay() {
        mPLVideoViewOne3.start();
    }

    @Override
    public void delProgramList() {

    }

    @Override
    public void interruptCancle() {

    }


    @Override
    public void playNormalProgram(Program program) {
        playProgram(program);
        mApplication.setProgram(program);
    }

    @Override
    public void playInterProgram(Program program) {
        playProgram(program);
        mApplication.setProgram(program);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void setVideoView(RelativeLayout layout, VideoView view, ProgramRes bean, Date date) {
        ProgramRes appRes = new ProgramRes();
        String area = bean.getArea();
        switch (area) {
            case "area1":
                appRes = mApplication.getArea1ProgramRes();
                break;
            case "area2":
                appRes = mApplication.getArea2ProgramRes();
                break;
        }

        String path = Constant.LOCAL_PROGRAM_PATH + "/" + DownFileUtils.getResourceName(bean.getResnam());
        int playtimes = (bean.getPlaycnt() == null) ? 0 : Integer.parseInt(bean.getPlaycnt());
        File file = new File(path);
        if (!file.exists()) return;
        if (bean.getResnam() == null || bean.getResnam().equals("")) return;
        int playcnt = 0;
        if (bean.getPlaycnt() != null && mApplication.getPlaycntMap() != null && mApplication.getPlaycntMap().size() != 0 && mApplication.getPlaycntMap().get(bean.getResnam()) != null) {
            if (bean.getPlaycnt() == null) Log.e("setVideoView", "getPlaycnt为空");
            if (mApplication == null) Log.e("setVideoView", "mApplication为空");
            if (mApplication.getPlaycntMap() == null)
                Log.e("setVideoView", "mApplication.getPlaycntMap()为空");
            playcnt = mApplication.getPlaycntMap().get(bean.getResnam());
        }
        if (bean.getStdtime() == null && bean.getEdtime() == null) {
            int priority = (bean.getPriority() == null) ? 0 : Integer.parseInt(bean.getPriority());
            int appPriority = (appRes.getPriority() == null) ? 0 : Integer.parseInt(appRes.getPriority());
            if (playcnt >= 0 && !bean.equals(appRes) && priority >= appPriority) {
                switch (area) {
                    case "area1":
                        mApplication.setArea1ProgramRes(bean);
                        break;
                    case "area2":
                        mApplication.setArea2ProgramRes(bean);
                        break;
                }
                mApplication.getPlaycntMap().put(bean.getResnam(), --playcnt);
                nomalResPlay(layout, view, path, bean);
            }
        } else {
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
            if (playcnt >= 0 && programResIsIntime && priority >= appPriority) {
//                playcnt = playcnt - 10;
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
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        mPLVideoViewOne3.stopPlayback();
        mUnbinder.unbind();
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
    protected void nomalResPlay(RelativeLayout layout, VideoView view, String path, ProgramRes bean) {
        String area = bean.getArea();
        boolean isImg = MediaFileUtil.isImageFileType(path);
        if (isImg) {
//            if (tempTimes[0] < playTimes || playTimes == 0) {
            view.setVisibility(View.GONE);
            Drawable drawable = Drawable.createFromPath(path);
//            layout.getBackground().setAlpha(100);
            layout.setBackground(drawable);
//                tempTimes[0]++;
            switch (area) {
                case "area1":
                    Log.e(TAG, "控件1播放图片");
                    if (mSubscriptionTimerArea1 != null && !mSubscriptionTimerArea1.isUnsubscribed()) {
                        mSubscriptionTimerArea1.unsubscribe();
                    }
                    setPicCountdown("area1");
                    mApplication.setArea1ResIsPlay(true);
                    break;
                case "area2":
                    Log.e(TAG, "控件2播放图片");
                    if (mSubscriptionTimerArea2 != null && !mSubscriptionTimerArea2.isUnsubscribed()) {
                        mSubscriptionTimerArea2.unsubscribe();
                    }
                    setPicCountdown("area2");
                    mApplication.setArea2ResIsPlay(true);
                    break;
            }
//            }
        } else {
//            layout.getBackground().setAlpha(0);
            LogUtils.e("nomalResPlay", "播放视频");
            view.setVisibility(View.VISIBLE);
            layout.setBackground(null);
            view.setVideoPath(path);

            view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    view.seekTo(0);
                    view.start();
                }
            });
            view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                        @Override
                                        public boolean onError(MediaPlayer mp, int what, int extra) {
                                            return true;
                                        }
                                    }
            );
            view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                             @Override
                                             public void onCompletion(MediaPlayer mp) {
                                                 view.stopPlayback();
                                                 switch (area) {
                                                     case "area1":
                                                         Log.e(TAG, "控件1播放视频完毕");
                                                         mApplication.setArea1ResIsPlay(false);
                                                         break;
                                                     case "area2":
                                                         Log.e(TAG, "控件2播放视频完毕");
                                                         mApplication.setArea2ResIsPlay(false);
                                                         break;
                                                 }
                                             }
                                         }

            );
        }

    }

    void setPicCountdown(String area) {
        switch (area) {
            case "area1":
                mSubscriptionTimerArea1 = Observable
                        .timer(40, TimeUnit.SECONDS)
                        .onTerminateDetach()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mApplication.setArea1ResIsPlay(false);
                                LogUtils.e("倒数结束", "图片1是否在播放" + mApplication.isArea1ResIsPlay());
                            }
                        });
                break;
            case "area2":
                mSubscriptionTimerArea2 = Observable
                        .timer(40, TimeUnit.SECONDS)
                        .onTerminateDetach()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mApplication.setArea2ResIsPlay(false);
                                LogUtils.e("倒数结束", "图片2是否在播放" + mApplication.isArea2ResIsPlay());
                            }
                        });
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            Log.e(TAG,"我被隐藏了");
            if (mSubscriptionPlayProgram != null && !mSubscriptionAreaPlay.isUnsubscribed()) {
                mSubscriptionPlayProgram.unsubscribe();
            }
            if (mSubscriptionAreaPlay != null && !mSubscriptionAreaPlay.isUnsubscribed()) {
                mSubscriptionAreaPlay.unsubscribe();
            }
            if (mSubscriptionTimerArea1 != null && !mSubscriptionTimerArea1.isUnsubscribed()) {
                mSubscriptionTimerArea1.unsubscribe();
            }
            if (mSubscriptionTimerArea2 != null && !mSubscriptionTimerArea2.isUnsubscribed()) {
                mSubscriptionTimerArea2.unsubscribe();
            }
            if (mPLVideoViewOne3.isPlaying()) {
                mPLVideoViewOne3.stopPlayback();
            }
            if (mPLVideoViewTwo3.isPlaying()) {
                mPLVideoViewTwo3.stopPlayback();
            }
            mPLVideoViewOne3.setVisibility(View.GONE);
            mPLVideoViewTwo3.setVisibility(View.GONE);
            if (mRelOne3.getBackground() != null) {
                mRelOne3.getBackground().setCallback(null);
            }
            if (mRelTwo3.getBackground() != null) {
                mRelTwo3.getBackground().setCallback(null);
            }
        } else {
            mPLVideoViewOne3.setVisibility(View.VISIBLE);
            mPLVideoViewTwo3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
