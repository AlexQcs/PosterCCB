package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.polling.ProgramBean;
import com.hc.posterccb.util.FileUtils;
import com.hc.posterccb.util.JsonUtils;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by alex on 2017/7/19.
 */

public class FullHModel extends BaseModel {

    public void getProgram(@NonNull String programPath) {
        String jsonStr = FileUtils.readFileSdcard(programPath);
        ArrayList<ProgramBean> programList = JsonUtils.JsonStr2ArrayList(jsonStr, new TypeToken<ArrayList<ProgramBean>>() {
        }.getType());
        Observable.from(programList)
                .subscribe(new Subscriber<ProgramBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProgramBean bean) {

                    }
                });
    }

    //通过接口产生信息回调
    public interface InfoHint {

    }
}
