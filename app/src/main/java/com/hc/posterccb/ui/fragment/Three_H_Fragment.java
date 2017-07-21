package com.hc.posterccb.ui.fragment;

import android.graphics.Color;
import android.widget.RelativeLayout;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.ui.acitivity.MainActivity;
import com.hc.posterccb.ui.contract.ThreeHContract;
import com.hc.posterccb.ui.presenter.ThreeHPresenter;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.StringUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;

public class Three_H_Fragment extends BaseFragment<ThreeHPresenter> implements ThreeHContract.ThreeHView, MainActivity.ActivityInteraction {

    private AVOptions options;
    public final static int MEDIA_CODEC_SW_DECODE = 0;
    public final static int MEDIA_CODEC_HW_DECODE = 1;
    public final static int MEDIA_CODEC_AUTO = 2;

    private static final String TAG = "Three_H_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoViewOne;

    @BindView(R.id.videoview_two)
    PLVideoView mPLVideoViewTwo;

    @BindView(R.id.videoview_three)
    PLVideoView mPLVideoViewThree;

    @BindView(R.id.relative_three_h)
    RelativeLayout mRootView;


    private String mProgramsPath01 = Constant.VIDEO1_PATH;
    private String mProgramsPath02 = Constant.VIDEO2_PATH;
    private String mProgramsPath03 = Constant.VIDEO3_PATH;

    @Override
    public void playProgram(String path) {
        mPLVideoViewOne.setAVOptions(options);
        mPLVideoViewTwo.setAVOptions(options);
        mPLVideoViewThree.setAVOptions(options);

        mPLVideoViewOne.setVideoPath(mProgramsPath01);
        mPLVideoViewOne.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoViewOne.seekTo(0);
                mPLVideoViewOne.start();
            }
        });
        mPLVideoViewOne.setOnErrorListener(mOnErrorListener);

        mPLVideoViewTwo.setVideoPath(mProgramsPath02);
        mPLVideoViewTwo.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoViewTwo.seekTo(0);
                mPLVideoViewTwo.start();
            }
        });
        mPLVideoViewTwo.setOnErrorListener(mOnErrorListener);

        mPLVideoViewThree.setVideoPath(mProgramsPath03);
        mPLVideoViewThree.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoViewThree.seekTo(0);
                mPLVideoViewThree.start();
            }
        });
        mPLVideoViewThree.setOnErrorListener(mOnErrorListener);
    }


    @Override
    public void playSuccess(String msg) {

    }

    @Override
    public void playError(String msg) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        options = new AVOptions();
        mPresenter.getProgramList(mProgramsPath01);
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

    @Override
    protected ThreeHPresenter loadPresenter() {
        return new ThreeHPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_three__h_;
    }

    @Override
    protected void initView() {
        mRootView.setBackgroundColor(Color.parseColor("#ff0000"));
    }

    public boolean checkPostParamNull() {
        boolean isNull = false;
        if (StringUtils.isEmpty(mProgramsPath01)) {
            LogUtils.e(TAG, "任务名称为空");
            isNull = true;
        }
//        else if (StringUtils.isEmpty(mSerialNumber)) {
//            LogUtils.e(TAG, "序列号为空");
//            isNull = true;
//        }
        return isNull;
    }

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
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

    @Override
    public void pause(int id) {
        mPLVideoViewOne.pause();
        mPLVideoViewTwo.pause();
        mPLVideoViewThree.pause();
    }


    @Override
    public void relay(int id) {
        mPLVideoViewOne.start();
        mPLVideoViewTwo.start();
        mPLVideoViewThree.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPLVideoViewOne.stopPlayback();
    }
}
