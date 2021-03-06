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
    private WeakReference actReference;//弱引用，防止内存泄漏
    protected V iView;

    public abstract HashMap<String,IModel> getiModelMap();

    @Override
    public void attachView(IView iView) {
        actReference=new WeakReference(iView);
    }

    @Override
    public void detachView() {
        if (actReference!=null){
            actReference.clear();
            actReference=null;
        }
    }

    @Override
    public V getIView() {
        return (V)actReference.get();
    }

    /**
     * 此方法用于:添加多个model,如有需要
     *
     * @param models
     * @author.Alex.on.2017年7月8日08:58:12
     */
    public abstract HashMap<String,IModel> loadModelMap(IModel... models);
}
