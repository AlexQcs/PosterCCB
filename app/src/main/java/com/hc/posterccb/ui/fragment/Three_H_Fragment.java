package com.hc.posterccb.ui.fragment;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    @BindView(R.id.rel_one)
    RelativeLayout mRelOne;

    @BindView(R.id.rel_two)
    RelativeLayout mRelTwo;

    @BindView(R.id.rel_three)
    RelativeLayout mRelThree;

    @BindView(R.id.relative_three_h)
    RelativeLayout mRootView;


    @Override
    public void playProgram(Program program) {

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void setAreaView(Date date, ProgramRes programRes, boolean isovertime) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
                break;
            case "area2":
                setVideoView(mRelTwo, mPLVideoViewTwo, programRes, date, isovertime);
                break;
            case "area3":
                setVideoView(mRelThree, mPLVideoViewTwo, programRes, date, isovertime);
                break;
            default:
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
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
