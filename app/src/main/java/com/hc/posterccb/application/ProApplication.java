package com.hc.posterccb.application;

import android.app.Application;
import android.content.Context;

import com.hc.posterccb.bean.program.Program;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by alex on 2017/7/8.
 */

public class ProApplication extends Application {
    private static ProApplication instance;
    private static Context mContext;
    private Program mProgram;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        instance = this;
        mContext=this;
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
}
