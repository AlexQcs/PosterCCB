package com.hc.posterccb.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import com.hc.posterccb.util.file.MediaFileUtil;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView ,MainActivity.ActivityInteraction{
    protected AVOptions options;
    protected View rootView;
    protected P mPresenter;
    protected Unbinder mUnbinder;
    protected Context mContext;
    protected String TAG = "Fragment";
    protected boolean isOverTime;//是否在定时播放内
    protected ProApplication mApplication=ProApplication.getInstance();

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
        mContext=context;
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
        for (ProgramRes res : programResList) {
            if (res.getStdtime()!=null&&res.getEdtime()!=null&&!res.getStdtime().equals("") && !res.getEdtime().equals("")) {
                Date sttime = DateFormatUtils.string2Date(res.getStdtime(), "HH:mm");
                Date edtime = DateFormatUtils.string2Date(res.getEdtime(), "HH:mm");
                limitTime.add(sttime);
                limitTime.add(edtime);
            }
        }
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Observable.just(programResList)
                                .flatMap(new Func1<List<ProgramRes>, Observable<ProgramRes>>() {
                                    @Override
                                    public Observable<ProgramRes> call(List<ProgramRes> programResList) {
                                        return Observable.from(programResList);
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ProgramRes>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onNext(ProgramRes programRes) {
                                        Date date = new Date(System.currentTimeMillis());
                                        if (limitTime.size() > 0 && limitTime.size() % 2 == 0) {
                                            for (int i = 0; i < limitTime.size(); i = i + 2) {
                                                if (limitTime.get(i).getTime() < date.getTime()
                                                        && date.getTime() < limitTime.get(i + 1).getTime()) {
                                                    isOverTime = true;
                                                }

                                            }
                                        }

                                        setAreaView(date, programRes, isOverTime);
                                    }
                                });
                    }
                });
    }

    //设置播放节目单中的资源播放位置接口
    protected abstract void setAreaView(Date date, ProgramRes programRes, boolean isovertime);

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void setVideoView(RelativeLayout layout, PLVideoView view, ProgramRes bean, Date date, boolean isovertime) {
        ProgramRes appRes = ProApplication.getInstance().getProgramRes();
        String path = Constant.LOCAL_PROGRAM_PATH + "/" + bean.getResnam();
        File file = new File(path);
        if (!file.exists()) return;
        if (bean.getResnam()==null||bean.getResnam().equals("")) return;

        if (bean.getStdtime()==null && bean.getEdtime()==null && !isOverTime) {
            int priority =( bean.getPriority()==null )? 0 : Integer.parseInt(bean.getPriority());
            int appPriority = (appRes.getPriority()==null) ? 0 : Integer.parseInt(appRes.getPriority());
            if (!view.isPlaying() && !bean.equals(appRes) && priority >= appPriority) {
                ProApplication.getInstance().setProgramRes(bean);
                int playtimes =(bean.getPlaycnt()==null)?0:Integer.parseInt(bean.getPlaycnt());
                nomalResPlay(layout,view, path, playtimes);
//                view.setVideoPath(path);
//                view.start();
            }
        } else if (isovertime) {
            Date startPlayTime = DateFormatUtils.string2Date(bean.getStdtime(), "HH:mm");
            Date endPlayTime = DateFormatUtils.string2Date(bean.getEdtime(), "HH:mm");
            if (startPlayTime.getTime() < date.getTime() && date.getTime() < endPlayTime.getTime()) {
                if (!appRes.equals(bean)) {
                    nomalResPlay(layout,view, path, 0);
                } else {
                    if (!view.isPlaying()) view.start();
                }

            }
        }
    }

    /**
     * @param view
     *         播放控件
     * @param playTimes
     *         播放次数
     * @param path
     *         资源路径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void nomalResPlay(RelativeLayout layout, PLVideoView view, String path, int playTimes) {
        final int[] tempTimes = {0};
        boolean isImg=MediaFileUtil.isImageFileType(path);
        if (isImg){
            view.setVisibility(View.GONE);
            Drawable drawable = Drawable.createFromPath(path);
            layout.setBackground(drawable);
        }else {
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
                    tempTimes[0]++;
                    if (tempTimes[0] < playTimes || playTimes == 0) {
                        view.setVideoPath(path);
                        view.seekTo(0);
                        view.start();
                    }
                }
            });
        }

    }



}
