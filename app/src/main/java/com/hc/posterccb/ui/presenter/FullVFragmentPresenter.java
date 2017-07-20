package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.FullVContract;
import com.hc.posterccb.ui.fragment.Full_V_Fragment;
import com.hc.posterccb.ui.model.FullVModel;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class FullVFragmentPresenter extends BasePresenter<Full_V_Fragment> implements FullVContract.FullVFragmentPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return loadModelMap(new FullVModel());
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
            ((FullVModel) getiModelMap().get("postplaylog")).getProgram(path, new FullVModel.InfoHint() {

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
