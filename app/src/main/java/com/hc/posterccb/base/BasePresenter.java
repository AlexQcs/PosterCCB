package com.hc.posterccb.base;

import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.mvp.IPresenter;
import com.hc.posterccb.mvp.IView;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by alex on 2017/7/7.
 */

public abstract class BasePresenter<V extends IView> implements IPresenter {
    private WeakReference actReference;
    protected V iView;

    public abstract HashMap<String,IModel> getiModelMap();

    @Override
    public void attachView(IView view) {
        actReference=new WeakReference(iView);
    }
}
