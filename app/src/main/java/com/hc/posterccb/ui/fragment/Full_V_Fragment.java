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
import com.hc.posterccb.ui.contract.Full_V_Constract;
import com.hc.posterccb.ui.presenter.Full_V_FrgmPresenter;
import com.hc.posterccb.util.DateFormatUtils;
import com.pili.pldroid.player.widget.PLVideoView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class Full_V_Fragment extends BaseFragment<Full_V_FrgmPresenter> implements
        Full_V_Constract.FrgmView{

    private static final String TAG = "Full_V_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoViewOne;
    @BindView(R.id.rel_one)
    RelativeLayout mRelOne;

    private String mProgramsPath = Constant.VIDEO1_PATH;






    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected Full_V_FrgmPresenter loadPresenter() {
        return new Full_V_FrgmPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_full__v_;
    }

    @Override
    protected void initView() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void setAreaView(Date date, ProgramRes programRes, boolean isovertime) {
        switch (programRes.getArea()) {
            case "area1":
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
                break;
            default:
                setVideoView(mRelOne, mPLVideoViewOne, programRes, date, isovertime);
                break;
        }

    }


    @Override
    public void pause() {

    }

    @Override
    public void replay() {

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
        Log.e("InterProgram","我被调用了");
        mPresenter.getProgramList(program);
    }


    @Override
    public void playProgram(Program program) {
        final List<ProgramRes> programResList = program.getList();
        if (programResList == null || programResList.size() == 0) {
            playError("播放列表为空");
            return;
        }
        //正常播放
        if (program.getMode().equals(Constant.PROGRAM_MODE_NORMAL)) {
            Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            Date date = new Date(System.currentTimeMillis());
                            String currentTime = DateFormatUtils.date2String(date, "HH:mm");
//                            Log.e("现在时间", currentTime + "---");
                            Date startPlayTime = DateFormatUtils.string2Date(program.getStdtime(), "HH:mm");
                            Date endPlayTime = DateFormatUtils.string2Date(program.getEdtime(), "HH:mm");
                            try {
                                //定时播放
                                if (startPlayTime.getTime() < date.getTime() && date.getTime() < endPlayTime.getTime()) {
                                    areaPlay(programResList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else if (program.getMode().equals(Constant.PROGRAM_MODE_DEF)) {
            areaPlay(programResList);
        } else {
            //插播播放
            areaPlay(programResList);
        }

    }

    @Override
    public void playSuccess(String msg) {

    }

    @Override
    public void playError(String msg) {

    }
}
