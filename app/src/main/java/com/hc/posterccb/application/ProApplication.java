package com.hc.posterccb.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hc.posterccb.Constant;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.bean.report.DetailBean;
import com.hc.posterccb.bean.resource.ResourceBean;
import com.hc.posterccb.ui.acitivity.WelcomeActivity;
import com.hc.posterccb.util.StringUtils;
import com.hc.posterccb.util.file.FileUtils;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alex on 2017/7/8.
 */

public class ProApplication extends Application {
    private static ProApplication instance;
    private static Context mContext;
    private Program mProgram; //当前节目单
    private volatile boolean mNormalIsPlay;  //是否有正常播放节目单正在播放
    private volatile boolean mAreaIsPlay;//是否已经在播放节目
    private boolean mInterIsPlay;      //是否有插播播放节目单正在播放
    private String mDisplayModel;
    private Program mDefProgram;
    private ResourceBean mResourceBean;
    private ProgramRes mArea1ProgramRes;
    private ProgramRes mArea2ProgramRes;
    private ProgramRes mArea3ProgramRes;
    private List<ResourceBean> mResourceBeanList;
    private List<DetailBean> mDetailBeanList;
    private HashMap<String,Integer> mPlaycntMap;

    private volatile boolean mPicPlayArea1IsOver;
    private volatile boolean mPicPlayArea2IsOver;
    private volatile boolean mPicPlayArea3IsOver;

    private volatile int mProresIndex1;
    private volatile int mProresIndex2;
    private volatile int mProresIndex3;


    private String mProPath;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("ProApplication onCreate","我被调用了");
        String processName = getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals("com.hc.posterccb");
            if (defaultProcess) {
                //当前应用的初始化
                if (LeakCanary.isInAnalyzerProcess(this)) {
                    return;
                }
                LeakCanary.install(this);
                instance = this;
                mContext = this;
                mDefProgram = new Program();
                mProgram=new Program();
                mResourceBean = new ResourceBean();
                mResourceBeanList = new ArrayList<>();
                mDetailBeanList = new ArrayList<>();
                mArea1ProgramRes = new ProgramRes();
                mArea2ProgramRes = new ProgramRes();
                mArea3ProgramRes = new ProgramRes();
                mPlaycntMap=new HashMap<>();
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
    }

    // 创建服务用于捕获崩溃异常
    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            ex.getMessage();
            String string= StringUtils.getExceptionInfo(ex);
            Log.e("程序异常",string);
            File errorLog=   new File(Constant.LOCAL_FILE_PATH+"/错误日志.txt");
            try {
                FileUtils.writeTxtFile(string,errorLog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            restartApp();//发生崩溃异常时,重启应用
        }
    };
    public void restartApp(){
        Intent intent = new Intent(instance,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instance.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    @Override
    public void onTerminate() {
        Log.e("APP", "onTerminate");
        super.onTerminate();
        Intent intent = new Intent(this,WelcomeActivity.class);
        startService(intent);
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.e("APP", "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.e("APP", "onTrimMemory");
        super.onTrimMemory(level);
        Intent intent = new Intent(this,WelcomeActivity.class);
        startService(intent);
    }


    public int getProresIndex1() {
        return mProresIndex1;
    }

    public void setProresIndex1(int proresIndex1) {
        mProresIndex1 = proresIndex1;
    }

    public int getProresIndex2() {
        return mProresIndex2;
    }

    public void setProresIndex2(int proresIndex2) {
        mProresIndex2 = proresIndex2;
    }

    public int getProresIndex3() {
        return mProresIndex3;
    }

    public void setProresIndex3(int proresIndex3) {
        mProresIndex3 = proresIndex3;
    }

    public String getProPath() {
        return mProPath;
    }

    public void setProPath(String proPath) {
        mProPath = proPath;
    }

    public boolean isPicPlayArea1IsOver() {
        return mPicPlayArea1IsOver;
    }

    public void setPicPlayArea1IsOver(boolean picPlayArea1IsOver) {
        mPicPlayArea1IsOver = picPlayArea1IsOver;
    }

    public boolean isPicPlayArea2IsOver() {
        return mPicPlayArea2IsOver;
    }

    public void setPicPlayArea2IsOver(boolean picPlayArea2IsOver) {
        mPicPlayArea2IsOver = picPlayArea2IsOver;
    }

    public boolean isPicPlayArea3IsOver() {
        return mPicPlayArea3IsOver;
    }

    public void setPicPlayArea3IsOver(boolean picPlayArea3IsOver) {
        mPicPlayArea3IsOver = picPlayArea3IsOver;
    }


    public String getDisplayModel() {
        return mDisplayModel;
    }

    public void setDisplayModel(String displayModel) {
        mDisplayModel = displayModel;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * @return 全局的上下文
     */
    public static Context getmContext() {
        return mContext;
    }



    public static ProApplication getInstance() {
        return instance;
    }

    public Program getProgram() {
        return mProgram;
    }

    public boolean isInterIsPlay() {
        return mInterIsPlay;
    }

    public void setInterIsPlay(boolean interIsPlay) {
        mInterIsPlay = interIsPlay;
    }

    public void setProgram(Program program) {
        mProgram = program;
    }

    public boolean isNormalPlay() {
        return mNormalIsPlay;
    }

    public void setNormalPlay(boolean play) {
        mNormalIsPlay = play;
    }

    public boolean isAreaIsPlay() {
        return mAreaIsPlay;
    }

    public void setAreaIsPlay(boolean areaIsPlay) {
        mAreaIsPlay = areaIsPlay;
    }

    public Program getDefProgram() {
        return mDefProgram;
    }

    public void setDefProgram(Program defProgram) {
        mDefProgram = defProgram;
    }

    public ResourceBean getResourceBean() {
        return mResourceBean;
    }

    public void setResourceBean(ResourceBean resourceBean) {
        mResourceBean = resourceBean;
    }

    public List<ResourceBean> getResourceBeanList() {
        return mResourceBeanList;
    }

    public void setResourceBeanList(List<ResourceBean> resourceBeanList) {
        mResourceBeanList = resourceBeanList;
    }

    public List<DetailBean> getDetailBeanList() {
        return mDetailBeanList;
    }

    public void setDetailBeanList(List<DetailBean> detailBeanList) {
        mDetailBeanList = detailBeanList;
    }

    public ProgramRes getArea1ProgramRes() {
        return mArea1ProgramRes;
    }

    public void setArea1ProgramRes(ProgramRes area1ProgramRes) {
        mArea1ProgramRes = area1ProgramRes;
    }

    public ProgramRes getArea2ProgramRes() {
        return mArea2ProgramRes;
    }

    public void setArea2ProgramRes(ProgramRes area2ProgramRes) {
        mArea2ProgramRes = area2ProgramRes;
    }

    public ProgramRes getArea3ProgramRes() {
        return mArea3ProgramRes;
    }

    public void setArea3ProgramRes(ProgramRes area3ProgramRes) {
        mArea3ProgramRes = area3ProgramRes;
    }

    public HashMap<String, Integer> getPlaycntMap() {
        return mPlaycntMap;
    }

    public void setPlaycntMap(HashMap<String, Integer> playcntMap) {
        mPlaycntMap = playcntMap;
    }

    public void initDetailBeanList(List<ResourceBean> resourceBeanList) {
        if (resourceBeanList == null || resourceBeanList.isEmpty()) return;
        mDetailBeanList = new ArrayList<>();
        for (int i = 0; i < resourceBeanList.size(); i++) {
            ResourceBean res = resourceBeanList.get(i);
            DetailBean bean = new DetailBean();
            bean.setFilename(res.getResname());
            bean.setResid(res.getResid());
            mDetailBeanList.add(bean);
        }
    }
}
