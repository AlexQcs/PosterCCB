package com.hc.posterccb.mvp;

/**
 * Created by alex on 2017/7/7.
 */

public interface IPresenter<V extends IView> {
    /**
     *此方法用于:  绑定
     * @author.Alex.on.2017年7月7日18:15:47
     */
    void attachView(V view);


    /**
     * @author.Alex.on.2017年7月7日18:15:47
     * 防止内存的泄漏,清楚presenter与activity之间的绑定
     */
    void detachView();


    /**
     *@author.Alex.on.2017年7月7日18:15:47
     * @return 获取View
     */
    IView getIView();

}
