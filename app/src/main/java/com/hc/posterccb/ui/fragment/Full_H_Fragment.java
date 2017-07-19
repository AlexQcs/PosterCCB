package com.hc.posterccb.ui.fragment;

import com.hc.posterccb.R;
import com.hc.posterccb.base.BaseFragment;
import com.hc.posterccb.base.BasePresenter;
import com.hc.posterccb.ui.presenter.FullHFragmentPresenter;


public class Full_H_Fragment extends BaseFragment<FullHFragmentPresenter> {

    private FullHFragmentPresenter mFullHFragmentPresenter;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected FullHFragmentPresenter loadPresenter() {
        return new FullHFragmentPresenter();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_full__h_;
    }

    @Override
    protected void initView() {

    }
}
