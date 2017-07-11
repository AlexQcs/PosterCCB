package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;

import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.exception.ApiException;
import com.hc.posterccb.subscriber.CommonSubscriber;
import com.hc.posterccb.util.LogUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/10.
 */

public class MainModel extends BaseModel {

    public void pollingTask(@NonNull String command, @NonNull String mac, @NonNull final InfoHint infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");
        httpService.polling(command, mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber<ResponseBody>(ProApplication.getmContext()) {
                    @Override
                    public void onNext(ResponseBody response) {

                        String resStr = null;
                        try {
                            resStr = response.string();
                            LogUtils.e("轮询数据:",resStr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        infoHint.successInfo(resStr);
                            LogUtils.e("MainModel", resStr);
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        infoHint.failInfo(e.message);
                        LogUtils.e("MainModel", e.getMessage());
                    }
                });
    }

    //通过接口产生信息回调
    public interface InfoHint {
        //请求有返回
        void successInfo(String str);

        //请求没有返回
        void failInfo(String str);
    }
}
