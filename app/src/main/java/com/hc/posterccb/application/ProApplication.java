package com.hc.posterccb.application;

import android.app.Application;
import android.content.Context;

import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.bean.program.ProgramRes;
import com.hc.posterccb.bean.report.DetailBean;
import com.hc.posterccb.bean.resource.ResourceBean;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2017/7/8.
 */

public class ProApplication extends Application {
    private static ProApplication instance;
    private static Context mContext;
    private Program mProgram; //当前节目单
    private boolean mIsPlay;  //是否有节目单正在播放
    private Program mDefProgram;
    private ResourceBean mResourceBean;
    private ProgramRes mProgramRes;
    private List<ResourceBean> mResourceBeanList;
    private List<DetailBean> mDetailBeanList;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        instance = this;
        mContext=this;
        mDefProgram=new Program();
        mResourceBean=new ResourceBean();
        mResourceBeanList=new ArrayList<>();
        mDetailBeanList=new ArrayList<>();
        mProgramRes=new ProgramRes();
    }

    /**
     * @return
     * 全局的上下文
     */
    public static Context getmContext(){
        return mContext;
    }


    public static ProApplication getInstance() {
        return instance;
    }

    public Program getProgram() {
        return mProgram;
    }

    public void setProgram(Program program) {
        mProgram = program;
    }

    public boolean isPlay() {
        return mIsPlay;
    }

    public void setPlay(boolean play) {
        mIsPlay = play;
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

    public ProgramRes getProgramRes() {
        return mProgramRes;
    }

    public void setProgramRes(ProgramRes programRes) {
        mProgramRes = programRes;
    }

    public void initDetailBeanList(List<ResourceBean> resourceBeanList){
        if (resourceBeanList==null||resourceBeanList.isEmpty())return;
        mDetailBeanList=new ArrayList<>();
        for (int i = 0; i < resourceBeanList.size(); i++) {
            ResourceBean res=resourceBeanList.get(i);
            DetailBean bean=new DetailBean();
            bean.setFilename(res.getResname());
            bean.setResid(res.getResid());
            mDetailBeanList.add(bean);
        }
    }
}
