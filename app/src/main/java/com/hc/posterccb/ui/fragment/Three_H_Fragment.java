package com.hc.posterccb.ui.fragment;

import android.graphics.Color;
import android.widget.RelativeLayout;

import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.ui.acitivity.MainActivity;
import com.hc.posterccb.ui.contract.BaseFrgmContract;
import com.hc.posterccb.ui.presenter.BaseFrgmPresenter;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.Date;

import butterknife.BindView;

public class Three_H_Fragment extends BaseFragment<BaseFrgmPresenter> implements
        BaseFrgmContract.FrgmView
        , MainActivity.ActivityInteraction {


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

    /**
     * @Override public void playProgram(String path) {
     * mPLVideoViewOne.setAVOptions(options);
     * mPLVideoViewTwo.setAVOptions(options);
     * mPLVideoViewThree.setAVOptions(options);
     * <p>
     * mPLVideoViewOne.setVideoPath(mProgramsPath01);
     * mPLVideoViewOne.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
     * @Override public void onCompletion(PLMediaPlayer player) {
     * mPLVideoViewOne.seekTo(0);
     * mPLVideoViewOne.start();
     * }
     * });
     * mPLVideoViewOne.setOnErrorListener(mOnErrorListener);
     * <p>
     * mPLVideoViewTwo.setVideoPath(mProgramsPath02);
     * mPLVideoViewTwo.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
     * @Override public void onCompletion(PLMediaPlayer player) {
     * mPLVideoViewTwo.seekTo(0);
     * mPLVideoViewTwo.start();
     * }
     * });
     * mPLVideoViewTwo.setOnErrorListener(mOnErrorListener);
     * <p>
     * mPLVideoViewThree.setVideoPath(mProgramsPath03);
     * mPLVideoViewThree.setOnCompletionListener(new PLMediaPlayer.OnCompletionListener() {
     * @Override public void onCompletion(PLMediaPlayer player) {
     * mPLVideoViewThree.seekTo(0);
     * mPLVideoViewThree.start();
     * }
     * });
     * mPLVideoViewThree.setOnErrorListener(mOnErrorListener);
     * }
     */

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

    @Override
    protected BaseFrgmPresenter loadPresenter() {
        return new BaseFrgmPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_three__h_;
    }

    @Override
    protected void initView() {
        mRootView.setBackgroundColor(Color.parseColor("#ff0000"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPLVideoViewOne.stopPlayback();

    }

    @Override
    protected void setAreaView(Date date, ProgramRes programRes) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(date, programRes, mPLVideoViewOne);
                break;
            case "area2":
                setVideoView(date, programRes, mPLVideoViewTwo);
                break;
            case "area3":
                setVideoView(date, programRes, mPLVideoViewThree);
                break;
            default:
                setVideoView(date, programRes, mPLVideoViewOne);
                break;
        }
    }

    @Override
    public void pause() {
        mPLVideoViewOne.pause();
        mPLVideoViewTwo.pause();
        mPLVideoViewThree.pause();
    }

    @Override
    public void replay() {
        mPLVideoViewOne.start();
        mPLVideoViewTwo.start();
        mPLVideoViewThree.start();
    }

    @Override
    public void delProgramList() {

    }

    @Override
    public void interruptCancle() {

    }

    @Override
    public void playNormalProgram(Program program) {
        mPresenter.getProgramList(program);
    }

    @Override
    public void playInterProgram(Program program) {
        mPresenter.getProgramList(program);
    }
}
