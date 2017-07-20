package com.hc.posterccb.ui.presenter;

import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.ui.contract.SecondVContract;

import java.util.HashMap;

/**
 * Created by alex on 2017/7/20.
 */

public class SecondVPresenter  extends BasePresenter<SecondVPresenter> implements SecondVContract.SecondVFragmentPresenter{
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
