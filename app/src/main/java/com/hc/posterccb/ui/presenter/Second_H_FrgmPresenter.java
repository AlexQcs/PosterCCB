package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.Second_H_Constract;
import com.hc.posterccb.ui.fragment.Second_H_Fragment;
import com.hc.posterccb.ui.model.Second_H_Model;

import java.util.HashMap;

/**
 * Created by HC on 2017/10/20.
 */

public class Second_H_FrgmPresenter extends BasePresenter<Second_H_Fragment> implements Second_H_Constract.FrgmPresenter {

    @Override
    public void getProgramList(Program program) {
        if (program == null) return;
        if (!getIView().checkPostParamNull()) {
            ((Second_H_Model) getiModelMap().get("postplaylog")).getProgram(program, new Second_H_Model.InfoHint() {
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
        return loadModelMap(new Second_H_Model());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }
}