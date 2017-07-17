package com.hc.posterccb.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.posterccb.mvp.IView;
import com.hc.posterccb.util.TUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class BaseFragment<T extends BasePresenter,E extends BaseModel> extends Fragment implements IView{
    protected View rootView;
    protected T mPresenter;
    protected E mModel;
    protected Unbinder mUnbinder;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null)
            rootView=inflater.inflate(getLayoutResource(),container,false);
        mContext=getActivity();
        mUnbinder=ButterKnife.bind(mContext,rootView);
        mPresenter= TUtil.getT(this,0);
        mModel=TUtil.getT(this,1);
        initPresenter();
        initView();

        return rootView;
    }

    //获取布局文件
    protected abstract int getLayoutResource();

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    protected void initPresenter() {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    //初始化view
    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter!=null){
            mPresenter.detachView();
        }
    }
}
