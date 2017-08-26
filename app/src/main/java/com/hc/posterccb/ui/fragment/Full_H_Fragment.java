package com.hc.posterccb.ui.fragment;

import android.widget.RelativeLayout;

import com.hc.posterccb.Constant;
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


public class Full_H_Fragment extends BaseFragment<BaseFrgmPresenter> implements
        BaseFrgmContract.FrgmView
        , MainActivity.ActivityInteraction {

    private static final String TAG = "Full_H_Fragment";

    @BindView(R.id.videoview_one)
    PLVideoView mPLVideoViewOne;
    @BindView(R.id.rel_one)
    RelativeLayout mRelOne;

    private String mProgramsPath = Constant.VIDEO1_PATH;

    private static final int MESSAGE_ID_RECONNECTING = 0x01;
    private boolean mIsFragmentPaused = true;
    private String mProgramsPath01 = Constant.VIDEO2_PATH;

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
        return R.layout.fragment_full__h_;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setAreaView(Date date, ProgramRes programRes,boolean isovertime) {
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
    public void playSuccess(String msg) {

    }

    @Override
    public void playError(String msg) {

    }


    @Override
    public void onPause() {
        super.onPause();
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
        mPresenter.getProgramList(program);
    }

    @Override
    public void playInterProgram(Program program) {
        mPresenter.getProgramList(program);
    }
}
