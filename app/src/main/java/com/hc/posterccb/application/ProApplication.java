package com.hc.posterccb.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by alex on 2017/7/8.
 */

public class ProApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
    }

    /**
     * @return
     * 全局的上下文
     */
    public static Context getmContext(){
        return mContext;
    }

}
