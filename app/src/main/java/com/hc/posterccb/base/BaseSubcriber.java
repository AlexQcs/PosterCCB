package com.hc.posterccb.base;

import com.hc.posterccb.exception.ApiException;

import rx.Subscriber;

/**
 * Created by alex on 2017/7/8.
 */

public abstract class BaseSubcriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {
        ApiException apiException=(ApiException)e;
        onError(apiException);
    }

    /**
     * 错误的一个回调
     * @param e
     */
    protected abstract void onError(ApiException e);
}
