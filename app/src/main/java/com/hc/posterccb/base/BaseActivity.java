package com.hc.posterccb.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.hc.posterccb.mvp.IView;
import com.hc.posterccb.util.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 2017/7/7.
 */

public abstract class BaseActivity<P extends BasePresenter> extends FragmentActivity implements IView, View.OnClickListener {

    protected View mView;
    protected P mPresenter;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("onCreate","我被调用了");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getView());
        ButterKnife.bind(this);
        mUnbinder = ButterKnife.bind(this);
        mPresenter = loadPresenter();
        initCommonData();
        initView();
        initListener();
        initData();
    }

    protected abstract P loadPresenter();

    private void initCommonData() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void initView();

    protected abstract int getLayoutId();

    protected abstract void otherViewClick(View view);

    /**
     * @return 显示的内容
     */
    public View getView() {
        mView = View.inflate(this, getLayoutId(), null);
        return mView;
    }

    /**
     * 点击事件的统一处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                otherViewClick(v);
                break;
        }
    }

    /**
     * 显示一个内容是str的toast
     *
     * @param str
     */
    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示一个内容是contentId指定的toast
     *
     * @param contentId
     */
    public void toast(int contentId) {
        Toast.makeText(this, contentId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 日志的处理
     *
     * @param str
     */
    public void LogE(String str) {
        LogUtils.e(getClass(), str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","我被调用了");
        if (mPresenter != null)
            mPresenter.detachView();
        mUnbinder.unbind();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","我被调用了");
        if (mPresenter != null)
            mPresenter.detachView();
        mUnbinder.unbind();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop","我被调用了");
        if (mPresenter != null)
            mPresenter.detachView();
        mUnbinder.unbind();
        finish();
    }
}
