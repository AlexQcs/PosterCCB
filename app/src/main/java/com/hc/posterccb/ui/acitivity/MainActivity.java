package com.hc.posterccb.ui.acitivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hc.posterccb.Constant;
import com.hc.posterccb.R;
import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseActivity;
import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.ui.contract.MainContract;
import com.hc.posterccb.ui.fragment.Full_H_Fragment;
import com.hc.posterccb.ui.fragment.Full_V_Fragment;
import com.hc.posterccb.ui.fragment.Second_H_Fragment;
import com.hc.posterccb.ui.fragment.Second_V_Fragment;
import com.hc.posterccb.ui.fragment.Three_H_Fragment;
import com.hc.posterccb.ui.presenter.MainPresenter;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.file.FileUtils;
import com.hc.posterccb.util.system.MemInfo;
import com.hc.posterccb.widget.MarqueeTextView;

import java.io.IOException;

import butterknife.BindView;

import static java.lang.Integer.parseInt;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.MainView {

    @BindView(R.id.tv_realtimetop)
    MarqueeTextView mStvRealTimeTop;
    @BindView(R.id.tv_realtimebottom)
    MarqueeTextView mStvRealTimeBottom;
    @BindView(R.id.tv_nolicense)
    TextView mTvNolicense;

    private Fragment[] fragments = {new Full_H_Fragment(), new Full_V_Fragment(), new Second_H_Fragment(), new Second_V_Fragment(), new Three_H_Fragment()};
    private int currentIndex = -1;
    private AudioManager am;

    private ActivityInteraction mInteraction;

    private android.support.v4.app.FragmentManager mFragmentManager;
//    private FragmentTransaction mFragmentTransaction;

    private String TAG = "MainActivity";

    private PowerManager localPowerManager = null;// 电源管理对象
    private PowerManager.WakeLock localWakeLock = null;// 电源锁

    //    (R.id.tv_realtime_top)
//    ScrollTextView mTvRealTimeTop;
    //轮询任务tag
    private String mTaskName = "getTask";
    //机器码
    private String mSerialNumber = Constant.getSerialNumber();
    //mac地址
    private String mMac = Constant.MAC;
    //区分即时消息类的位置
    private String mRealTimePosition = "top";

    private ProApplication mApplication = ProApplication.getInstance();

    @Override
    protected MainPresenter loadPresenter() {
        return new MainPresenter();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initData() {
        FileUtils.checkAppFile();
        long unused = MemInfo.getAvailableSize();
        LogUtils.e(TAG, "剩余内存" + unused);
        mMac = mMac.toUpperCase();
        mMac = mMac.replaceAll(":", "-");
        LogUtils.e(TAG, "mac地址" + mMac);
        boolean isNull = (mMac == null || mMac.length() <= 0);
        if (!isNull) mPresenter.pollingGetTask(mTaskName, mMac);
//        mPresenter.checkLicense();
        mPresenter.initConfig();
    }

    @Override
    protected void initListener() {

    }

    //fragment视频播放控制
    public interface ActivityInteraction {
        //暂停
        void pause();

        //恢复
        void replay();

        //删除播放列表
        void delProgramList();

        //取消插播
        void interruptCancle();

        //处理正常播放列表
        void playNormalProgram(Program program);

        //处理插播播放列表
        void playInterProgram(Program program);
    }

    //初始化控件
    @Override
    protected void initView() {
        mFragmentManager = getSupportFragmentManager();
    }

    private void replace(int index, Program program) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (index != currentIndex) {
            if (fragments[index].isAdded()) {
                fragmentTransaction.show(fragments[index]);
            } else {
                fragmentTransaction.add(R.id.frame_fragment, fragments[index]);
            }
            if (currentIndex != -1 && currentIndex != index) {
                fragmentTransaction.hide(fragments[currentIndex]);
            }
//            fragmentTransaction.replace(R.id.frame_fragment, fragments[index]);
            currentIndex = index;
            fragmentTransaction.commit();
            getSupportFragmentManager().executePendingTransactions();
        }

//        fragmentTransaction.replace(R.id.frame_fragment,fragments[index]);
//        currentIndex = index;
//        fragmentTransaction.commit();
//        getSupportFragmentManager().executePendingTransactions();

    }

    //获取布局
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void otherViewClick(View view) {

    }

    //检查轮询
    public boolean checkPostParamNull() {
        boolean isNull = false;
        if (StringUtils.isEmpty(mTaskName)) {
            LogUtils.e(TAG, "任务名称为空");
            isNull = true;
        }
//        else if (StringUtils.isEmpty(mSerialNumber)) {
//            LogUtils.e(TAG, "序列号为空");
//            isNull = true;
//        }
        return isNull;
    }

    //设置即时消息
    @Override
    public void setRealTimeText(RealTimeMsgBean bean) {

        String position = bean.getPosition();
        mRealTimePosition = position;
        if (position.equals("top")) {
            setMarqueeTextView(mStvRealTimeTop, bean);
        } else if (position.equals("under")) {
            setMarqueeTextView(mStvRealTimeBottom, bean);
        }
    }

    //开始即时消息的滚动
    @Override
    public void startRealtimeTask() {
        if (mRealTimePosition.equals("top")) {
            mStvRealTimeTop.setVisibility(View.VISIBLE);
            mStvRealTimeTop.startScroll();
        } else if (mRealTimePosition.equals("under")) {
            mStvRealTimeBottom.setVisibility(View.VISIBLE);
            mStvRealTimeBottom.startScroll();
        }
    }

    //停止即时消息滚动
    @Override
    public void stopRealtimeTask() {
        if (mRealTimePosition.equals("top")) {
            mStvRealTimeTop.stopScroll();
            mStvRealTimeTop.setVisibility(View.GONE);
        } else if (mRealTimePosition.equals("under")) {
            mStvRealTimeBottom.stopScroll();
            mStvRealTimeBottom.setVisibility(View.GONE);
        }
    }

    //取消即时消息
    @Override
    public void cancleRealtimeTask() {
        mStvRealTimeTop.stopScroll();
        mStvRealTimeBottom.stopScroll();
    }

    //暂停播放视频
    @Override
    public void pauseVideo() {
        mInteraction.pause();
    }

    //恢复播放视频
    @Override
    public void replayVideo() {
        mInteraction.replay();
    }

    //删除播放列表
    @Override
    public void deleteProgramList() {
        mInteraction.delProgramList();
    }

    //取消视频插播
    @Override
    public void cancleInterruptVideo() {
        mInteraction.interruptCancle();
        try {
            FileUtils.coverTxtToFile("", Constant.LOCAL_INSERT_PROGRAM_LIST_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //授权操作
    @Override
    public void license() {
        startActivity(new Intent(this, LicenseActivity.class));
    }

    //未授权显示
    @Override
    public void noLicense() {
//        mTvNolicense.setVisibility(View.VISIBLE);
    }

    @Override
    public void logicNormalProgram(Program program) {
        String model = mApplication.getDisplayModel();
        String modelStr = program.areatype;
        mApplication.setProgram(program);
        switch (modelStr) {
            case "model_full_h":
                LogE("模板为：横向全屏");
                mApplication.setDisplayModel(modelStr);
                replace(0, program);
                mInteraction = (ActivityInteraction) fragments[0];
                break;
            case "model_full_v":
                LogE("模板为：竖直全屏");
                mApplication.setDisplayModel(modelStr);
                replace(1, program);
                mInteraction = (ActivityInteraction) fragments[1];
                break;
            case "model_second_h":
                LogE("模板为：横向双版");
                mApplication.setDisplayModel(modelStr);
                replace(2, program);
                mInteraction = (ActivityInteraction) fragments[2];
                break;
            case "model_second_v":
                LogE("模板为：竖直双版");
                mApplication.setDisplayModel(modelStr);
                replace(3, program);
                mInteraction = (ActivityInteraction) fragments[3];
                break;
            case "model_three_h":
                LogE("模板为：全屏三版");
                mApplication.setDisplayModel(modelStr);
                replace(4, program);
                mInteraction = (ActivityInteraction) fragments[4];
                break;
        }
        if (mInteraction == null) {
            LogE("找不到对应模板");
        } else {
            mInteraction.playNormalProgram(program);
        }

    }


    @Override
    public void logicInterProgram(Program program) {
        String model = mApplication.getDisplayModel();
        String modelStr = program.areatype;
        mApplication.setProgram(program);
        switch (modelStr) {
            case "model_full_h":
                LogE("模板为：横向全屏");
                mApplication.setDisplayModel(modelStr);
                replace(0, program);
                mInteraction = (ActivityInteraction) fragments[0];
                break;
            case "model_full_v":
                LogE("模板为：竖直全屏");
                mApplication.setDisplayModel(modelStr);
                replace(1, program);
                mInteraction = (ActivityInteraction) fragments[1];
                break;
            case "model_second_h":
                LogE("模板为：横向双版");
                mApplication.setDisplayModel(modelStr);
                replace(2, program);
                mInteraction = (ActivityInteraction) fragments[2];
                break;
            case "model_second_v":
                LogE("模板为：竖直双版");
                mApplication.setDisplayModel(modelStr);
                replace(3, program);
                mInteraction = (ActivityInteraction) fragments[3];
                break;
            case "model_three_h":
                LogE("模板为：全屏三版");
                mApplication.setDisplayModel(modelStr);
                replace(4, program);
                mInteraction = (ActivityInteraction) fragments[4];
                break;
        }
        if (mInteraction == null) {
            LogE("找不到对应模板");
        } else {
            mInteraction.playNormalProgram(program);
        }
    }


    //设置滚动textview样式
    void setMarqueeTextView(MarqueeTextView view, RealTimeMsgBean bean) {
        //字体大小
        float fontSize = Float.parseFloat(bean.getFontsize());
        //背景颜色
        int bgColor = Color.parseColor(bean.getBgcolor());
        //字体颜色
        int fontColor = Color.parseColor(bean.getFontcolor());
        //播放速度
        int speed = parseInt(bean.getSpeed());
        //播放的内容
        String message = bean.getMessage();
        view.setTextSize(fontSize);
        view.setText(message, TextView.BufferType.SPANNABLE);
        view.setBackgroundColor(bgColor);
        view.setTextColor(fontColor);
        switch (speed) {
            case 0:
                view.setSpeed(1);
                break;
            case 1:
                view.setSpeed(2);
                break;
            case 2:
                view.setSpeed(4);
                break;
        }
        //播放时长或者播放时间
        if (!("").equals(bean.getCount())) {
            int count = parseInt(bean.getCount());
            view.init(getWindowManager(), count);
        } else if (!("").equals(bean.getTimelength())) {
            int timeLength = parseInt(bean.getTimelength());
        }


    }

    //轮询成功操作
    @Override
    public void pollingSuccess(String msg) {

    }

    //轮询失败操作
    @Override
    public void pollingFail(String failStr) {
        toast(failStr);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }
}
