package com.hc.posterccb.base;

import com.hc.posterccb.mvp.IModel;
import com.hc.posterccb.mvp.IPresenter;
import com.hc.posterccb.mvp.IView;

import java.lang.ref.WeakReference;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class OtherPresenter<M extends IModel,V extends IView> implements IPresenter {
    private WeakReference actReference;
    protected V iView;
    protected M iModel;

    public M getiModel(){
        iModel=loadModel();//使用前先进行初始化
        return iModel;
    }

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
        return (V) actReference.get();
    }

    public abstract M loadModel();
}
