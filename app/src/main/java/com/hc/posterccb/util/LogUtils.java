package com.hc.posterccb.util;

import android.util.Log;

import com.hc.posterccb.BuildConfig;

/**
 * Created by alex on 2017/7/8.
 */

public class LogUtils {
    public static final boolean isDebug = BuildConfig.DEBUG;


    /**
     * 打印一个debug等级的 log
     */
    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d("alex----->" + tag, msg);
        }
    }

    /**
     * 打印一个debug等级的 log
     */
    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e("alex----->" + tag, msg);
        }
    }

    /**
     * 打印一个debug等级的 log
     */
    public static void e(Class cls, String msg) {
        if (isDebug) {
            Log.e("alex----->" + cls.getSimpleName(), msg);
        }
    }

}
