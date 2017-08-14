package com.hc.posterccb.subscriber;

import android.content.Context;

import com.hc.posterccb.base.BaseSubcriber;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.util.LogUtils;
import com.hc.posterccb.util.system.NetworkUtil;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class CommonSubscriber<T> extends BaseSubcriber<T>{

    private Context mContext;
    public CommonSubscriber(Context context){
        this.mContext=context;
    }

    private static final String TAG="CommonSubscriber";

    @Override
    public void onStart() {
        if (!NetworkUtil.isNetworkAvailable(mContext)){
            LogUtils.e(TAG,"网络不可用");
        }else {
            LogUtils.e(TAG,"网络可用");
        }
    }

    @Override
    public void onError(ApiException e) {
        LogUtils.e(TAG,"错误信息为"+"code:"+e.code+"  message"+e.message);
    }

    @Override
    public void onCompleted() {
        LogUtils.e(TAG,"成功了");
    }
}
