package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.SecondHContract;
import com.hc.posterccb.ui.fragment.Second_H_Fragment;
import com.hc.posterccb.ui.model.FullHModel;
import com.hc.posterccb.ui.model.SecondHModel;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class SecondHPresenter extends BasePresenter<Second_H_Fragment> implements SecondHContract.SecondHFragmentPresenter {


    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new FullHModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }

    @Override
    public void getProgramList(String path) {
        if (!getIView().checkPostParamNull()) {
            ((SecondHModel) getiModelMap().get("postplaylog")).getProgram(path, new SecondHModel.InfoHint() {

//                @Override
//                public void playVideo(ArrayList<ProgramBean> list) {
//                    getIView().playProgram(list);
//                }

                //                @Override


                @Override
                public void playVideo(String path) {
                    getIView().playProgram(path);
                }

                @Override
                public void playErrorLog(String msg) {
                    getIView().playSuccess(msg);
                }

                @Override
                public void playSuccessLog(String msg) {
                    getIView().playError(msg);
                }
            });
        }
    }
}
