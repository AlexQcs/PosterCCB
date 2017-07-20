package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.SecondHContract;
import com.hc.posterccb.ui.fragment.Second_H_Fragment;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class SecondHPresenter extends BasePresenter<Second_H_Fragment> implements SecondHContract.SecondHFragmentPresenter {


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
