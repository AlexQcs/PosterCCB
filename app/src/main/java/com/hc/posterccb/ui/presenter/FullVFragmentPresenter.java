package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.FullVContract;
import com.hc.posterccb.ui.fragment.Full_V_Fragment;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class FullVFragmentPresenter extends BasePresenter<Full_V_Fragment> implements FullVContract.FullVFragmentPresenter {
    @Override
    public HashMap<String, IModel> getiModelMap() {
        return null;
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        return null;
    }

    @Override
    public void getProgramList(String path) {

    }
}
