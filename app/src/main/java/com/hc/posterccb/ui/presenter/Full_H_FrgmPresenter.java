package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.Full_H_Constract;
import com.hc.posterccb.ui.fragment.Full_H_Fragment;
import com.hc.posterccb.ui.model.Full_H_Model;

import java.util.HashMap;

/**
 * Created by alex on 2017/9/15.
 */

public class Full_H_FrgmPresenter extends BasePresenter<Full_H_Fragment> implements Full_H_Constract.FrgmPresenter {
    @Override
    public void getProgramList(Program program) {
        if (program==null)return;
        if (!getIView().checkPostParamNull()) {
            ((Full_H_Model) getiModelMap().get("postplaylog")).getProgram(program, new Full_H_Model.InfoHint() {
                @Override
                public void playVideo(Program program) {
                    getIView().playProgram(program);
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
        return loadModelMap(new Full_H_Model());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }
}
