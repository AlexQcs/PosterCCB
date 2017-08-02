package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.BaseFrgmContract;
import com.hc.posterccb.ui.model.BaseFrgmModel;

import java.util.HashMap;

/**
 * Created by alex on 2017/8/2.
 */

public class BaseFrgmPresenter extends BasePresenter<BaseFragment> implements BaseFrgmContract.FrgmPresenter {
    @Override
    public void getProgramList(Program program) {
        if (!getIView().checkPostParamNull()) {
            ((BaseFrgmModel) getiModelMap().get("postplaylog")).getProgram(program, new BaseFrgmModel.InfoHint() {
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
        return loadModelMap(new BaseFrgmModel());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }
}
