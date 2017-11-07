package com.hc.posterccb.ui.fragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RelativeLayout;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.ui.contract.Three_H_Constract;
import com.hc.posterccb.ui.presenter.Three_H_FrgmPresenter;
import com.hc.posterccb.util.DateFormatUtils;
import com.hc.posterccb.util.LogUtils;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class Three_H_Fragment extends BaseFragment<Three_H_FrgmPresenter> implements
        Three_H_Constract.FrgmView {

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

    private Subscription mSubscriptionPlayProgram;

    @Override
    public void playProgram(Program program) {
        LogUtils.e("竖直全屏","播放被调用");
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
            mSubscriptionPlayProgram = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = DateFormatUtils.date2String(date, "HH:mm");
//                            Log.e("现在时间", currentTime + "---");
                            boolean isInTime = false;
                            Date startPlayTime = null;
                            Date endPlayTime = null;
                            if (program.getStdtime().length() < 5 || program.getEdtime().length() < 5) {
                                isInTime=true;
                            } else {
                                startPlayTime = DateFormatUtils.string2Date(program.getStdtime(), "HH:mm");
                                endPlayTime = DateFormatUtils.string2Date(program.getEdtime(), "HH:mm");
                                if (startPlayTime.getTime() < date.getTime() && date.getTime() < endPlayTime.getTime()){
                                    isInTime = true;
                                }
                            }

                            try {
                                //定时播放
                                if (isInTime&&!mApplication.isAreaIsPlay()) {
                                    if (mSubscriptionAreaPlay != null && !mSubscriptionAreaPlay.isUnsubscribed()) {
                                        mSubscriptionAreaPlay.unsubscribe();
                                    }
                                    areaPlay(programResList);
                                    mApplication.setAreaIsPlay(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
    @Override
    protected void setAreaView(Date date, ProgramRes programRes) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date);
                break;
            case "area2":
                setVideoView(mRelTwo, mPLVideoViewTwo, programRes, date);
                break;
            case "area3":
                setVideoView(mRelThree, mPLVideoViewTwo, programRes, date);
                break;
            default:
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date);
                break;
        }

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
    protected Three_H_FrgmPresenter loadPresenter() {
        return new Three_H_FrgmPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_three__h_;
    }

    @Override
    protected void initView() {

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
        if (mPresenter == null) {
            return;
        }
        mApplication.setProgram(program);
        mPresenter.getProgramList(program);
    }

    @Override
    public void playInterProgram(Program program) {
        if (mPresenter == null) {
            return;
        }
        mApplication.setProgram(program);
        Log.e("InterProgram", "我被调用了");
        mPresenter.getProgramList(program);
    }
}
