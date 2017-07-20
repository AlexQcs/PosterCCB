package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.FullHContract;
import com.hc.posterccb.ui.fragment.Full_H_Fragment;
import com.hc.posterccb.ui.model.FullHModel;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/19.
 */

public class FullHFragmentPresenter extends BasePresenter<Full_H_Fragment> implements FullHContract.FullHFragmentPresenter {



    @Override
    public void getProgramList(String path) {
        if (!getIView().checkPostParamNull()) {
            ((FullHModel) getiModelMap().get("postplaylog")).getProgram(path, new FullHModel.InfoHint() {

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


}
