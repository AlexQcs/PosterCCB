package com.hc.posterccb.ui.fragment;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.ui.contract.SecondVContract;
import com.hc.posterccb.ui.presenter.SecondVPresenter;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.StringUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import butterknife.BindView;


public class Second_V_Fragment extends BaseFragment<SecondVPresenter> implements SecondVContract.SecondVView {

    private static final String TAG = "Second_V_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoView;

    private String mProgramsPath = Constant.VIDEO1_PATH;

    @Override
    public void playProgram(String path) {
        mPLVideoView.setVideoPath(mProgramsPath);
        mPLVideoView.start();
        mPLVideoView.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(PLMediaPlayer player) {
                mPLVideoView.seekTo(0);
                mPLVideoView.start();
            }
        });
        mPLVideoView.setOnErrorListener(mOnErrorListener);
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
        mPresenter.getProgramList(mProgramsPath);
    }

    @Override
    protected SecondVPresenter loadPresenter() {
        return new SecondVPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_second__v_;
    }

    @Override
    protected void initView() {

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

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    LogUtils.e(TAG,"Invalid URL !");
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    LogUtils.e(TAG,"404 resource not found !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    LogUtils.e(TAG,"Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    LogUtils.e(TAG,"Connection timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    LogUtils.e(TAG,"Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    LogUtils.e(TAG,"Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    LogUtils.e(TAG,"Network IO Error !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    LogUtils.e(TAG,"Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    LogUtils.e(TAG,"Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    LogUtils.e(TAG,"Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_HW_DECODE_FAILURE:
                    LogUtils.e(TAG, AVOptions.MEDIA_CODEC_SW_DECODE+"");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    LogUtils.e(TAG,"unknown error !");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };
}
