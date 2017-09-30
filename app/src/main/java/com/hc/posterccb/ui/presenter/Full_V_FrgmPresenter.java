package com.hc.posterccb.ui.presenter;

import android.util.Log;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.bean.program.Program;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.Full_V_Constract;
import com.hc.posterccb.ui.fragment.Full_V_Fragment;
import com.hc.posterccb.ui.model.Full_V_Model;

import java.util.HashMap;

/**
 * Created by HC on 2017/9/21.
 */

public class Full_V_FrgmPresenter extends BasePresenter<Full_V_Fragment> implements Full_V_Constract.FrgmPresenter {

    @Override
    public void getProgramList(Program program) {
        Log.e("getProgramList","我被调用了");
        if (program==null)return;
        if (!getIView().checkPostParamNull()) {
            ((Full_V_Model) getiModelMap().get("postplaylog")).getProgram(program, new Full_V_Model.InfoHint() {
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
        return loadModelMap(new Full_V_Model());
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        HashMap<String, IModel> map = new HashMap<>();
        map.put("postplaylog", models[0]);
        return map;
    }
}
