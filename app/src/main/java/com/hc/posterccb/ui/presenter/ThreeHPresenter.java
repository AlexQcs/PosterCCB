package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.ThreeHContract;
import com.hc.posterccb.ui.fragment.Three_H_Fragment;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class ThreeHPresenter extends BasePresenter<Three_H_Fragment> implements ThreeHContract.ThreeHFragmentPresenter{

    @Override
    public void getProgramList(String path) {

    }

    @Override
    public HashMap<String, IModel> getiModelMap() {
        return null;
    }

    @Override
    public HashMap<String, IModel> loadModelMap(IModel... models) {
        return null;
    }
}
