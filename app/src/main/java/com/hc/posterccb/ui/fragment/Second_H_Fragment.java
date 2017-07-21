package com.hc.posterccb.ui.fragment;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.ui.contract.SecondHContract;
import com.hc.posterccb.ui.presenter.SecondHPresenter;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.StringUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;


public class Second_H_Fragment extends BaseFragment<SecondHPresenter> implements SecondHContract.SecondHView {

    private static final String TAG = "Second_H_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoViewOne;

    @BindView(R.id.videoview_two)
    PLVideoView mPLVideoViewTwo;


    private String mProgramsPath = Constant.VIDEO1_PATH;

    @Override
    public void playProgram(String path) {
        mPLVideoViewOne.setVideoPath(mProgramsPath);
        mPLVideoViewOne.start();
        mPLVideoViewOne.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoViewOne.seekTo(0);
                mPLVideoViewOne.start();
            }
        });
        mPLVideoViewOne.setOnErrorListener(mOnErrorListener);

        mPLVideoViewTwo.setVideoPath(mProgramsPath);
        mPLVideoViewTwo.start();
        mPLVideoViewTwo.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoViewTwo.seekTo(0);
                mPLVideoViewTwo.start();
            }
        });
        mPLVideoViewTwo.setOnErrorListener(mOnErrorListener);
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

    }

    public boolean checkPostParamNull() {
        boolean isNull = false;
        if (StringUtils.isEmpty(mProgramsPath)) {
            LogUtils.e(TAG, "任务名称为空");
            isNull = true;
        }
//        else if (StringUtils.isEmpty(mSerialNumber)) {
//            LogUtils.e(TAG, "序列号为空");
//            isNull = true;
//        }
        return isNull;
    }

    @Override
    protected SecondHPresenter loadPresenter() {
        return new SecondHPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_second__h_;
    }

    @Override
    protected void initView() {

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

}
