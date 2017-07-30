package com.hc.posterccb.ui.model;

import android.support.annotation.NonNull;

import com.hc.posterccb.base.BaseModel;
import com.hc.posterccb.bean.program.Program;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/20.
 */

public class ThreeHModel extends BaseModel {
    public void getProgram(@NonNull Program program, final InfoHint infoHint) {
//        String jsonStr = FileUtils.readFileSdcard(programPath);
//        ArrayList<ProgramBean> programList = JsonUtils.JsonStr2ArrayList(jsonStr, new TypeToken<ArrayList<ProgramBean>>() {
//        }.getType());
//        Observable.just(programList).subscribe(new Subscriber<ArrayList<ProgramBean>>() {
//            @Override
//            public void onCompleted() {
//                infoHint.playSuccessLog("获取播放列表完成");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                infoHint.playErrorLog(e.getMessage());
//            }
//
//            @Override
//            public void onNext(ArrayList<ProgramBean> list) {
//                infoHint.playVideo(list);
//            }
//        });
        Observable.just(program)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Program>() {
                    @Override
                    public void onCompleted() {
                        infoHint.playSuccessLog("获取播放列表完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        infoHint.playErrorLog(e.getMessage());
                    }

                    @Override
                    public void onNext(Program program) {
                        infoHint.playVideo(program);
                    }

                });

    }

    //通过接口产生信息回调
    public interface InfoHint {
        //        void playVideo(ArrayList<ProgramBean> list);
        void playVideo(Program program);

        void playErrorLog(String msg);

        void playSuccessLog(String msg);
    }

}
