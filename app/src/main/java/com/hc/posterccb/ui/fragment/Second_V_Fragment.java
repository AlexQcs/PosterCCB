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
import com.hc.posterccb.ui.contract.Second_V_Constract;
import com.hc.posterccb.ui.presenter.Second_V_FrgmPresenter;
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


public class Second_V_Fragment extends BaseFragment<Second_V_FrgmPresenter> implements
        Second_V_Constract.FrgmView {

    private static final String TAG = "Second_V_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoViewOne;

    @BindView(R.id.videoview_two)
    PLVideoView mPLVideoViewTwo;

    @BindView(R.id.rel_one)
    RelativeLayout mRelOne;

    @BindView(R.id.rel_two)
    RelativeLayout mRelTwo;

    private String mProgramsPath = Constant.VIDEO1_PATH;


    private Subscription mSubscriptionPlayProgram;

    @Override
    public void playProgram(Program program) {
        LogUtils.e("竖直双版","播放被调用");
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
    protected void setAreaView(Date date, ProgramRes programRes, boolean isovertime) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
                break;
            case "area2":
                setVideoView(mRelTwo, mPLVideoViewTwo, programRes, date, isovertime);
                break;
            default:
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
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
    protected Second_V_FrgmPresenter loadPresenter() {
        return new Second_V_FrgmPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_second__v_;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void pause() {
        mPLVideoViewOne.pause();
        mPLVideoViewTwo.pause();
    }

    @Override
    public void replay() {
        mPLVideoViewOne.start();
        mPLVideoViewTwo.start();
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
