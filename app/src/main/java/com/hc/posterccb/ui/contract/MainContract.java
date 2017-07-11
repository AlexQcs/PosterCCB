package com.hc.posterccb.ui.contract;

/**
 * Created by alex on 2017/7/8.
 */

public class MainContract {
    public interface MainView{

        //轮询成功
        void pollingSuccess(String msg);
        //轮询失败
        void pollingFail(String failStr);
    }

    public interface MainPresenter{

        /**
         * 此方法用于:轮询服务器
         *
         * @param command
         *         命令名称
         * @param mac
         *          设备码
         * @return
         * @author.Alex.on.a
         * @throw
         */
        void pollingTask(String command,String mac);
    }

}
