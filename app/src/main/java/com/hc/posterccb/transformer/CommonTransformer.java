package com.hc.posterccb.transformer;

import com.hc.posterccb.base.BaseHttpResult;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by alex on 2017/7/8.
 */

public class CommonTransformer<T> implements Observable.Transformer<BaseHttpResult<T>,T> {

    @Override
    public Observable<T> call(Observable<BaseHttpResult<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(ErrorTransformer.<T>getInstance());
    }
}
