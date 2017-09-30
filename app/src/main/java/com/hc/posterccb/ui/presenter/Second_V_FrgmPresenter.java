package com.hc.posterccb.ui.presenter;

import android.util.Log;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.Second_V_Constract;
import com.hc.posterccb.ui.fragment.Second_V_Fragment;
import com.hc.posterccb.ui.model.Second_V_Model;

import java.util.HashMap;

/**
 * Created by HC on 2017/9/26.
 */

public class Second_V_FrgmPresenter extends BasePresenter<Second_V_Fragment> implements Second_V_Constract.FrgmPresenter {
    @Override
    public void getProgramList(Program program) {
        Log.e("getProgramList","我被调用了");
        if (program==null)return;
        if (!getIView().checkPostParamNull()) {
            ((Second_V_Model) getiModelMap().get("postplaylog")).getProgram(program, new Second_V_Model.InfoHint() {
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
        return loadModelMap(new Second_V_Model());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }
}
