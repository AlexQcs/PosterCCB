package com.hc.posterccb.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.posterccb.mvp.IView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IView{
    protected View rootView;
    protected P mPresenter;
    protected Unbinder mUnbinder;
    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView==null)
            rootView=inflater.inflate(getLayoutResource(),container,false);
        mContext=getActivity();
        mUnbinder=ButterKnife.bind(this,rootView);
        mPresenter= loadPresenter();
        initCommonData();
        initView();
        initListener();
        initData();
        initPresenter();
        initView();
        
        return rootView;
    }

    protected abstract void initListener();

    protected abstract void initData();

    private void initCommonData() {
        if (mPresenter!=null)
            mPresenter.attachView(this);
    }


    protected abstract P loadPresenter();

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
