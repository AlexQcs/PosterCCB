package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.polling.RealTimeMsgBean;

/**
 * Created by alex on 2017/7/8.
 */

public class MainContract {
    public interface MainView {

        //轮询成功
        void pollingSuccess(String msg);

        //轮询失败
        void pollingFail(String failStr);

        //设置即时消息任务
        void setRealTimeText(RealTimeMsgBean bean);

        //开始即时消息任务
        void startRealtimeTask();

        //停止即时消息任务
        void stopRealtimeTask();

        //取消即时消息任务
        void cancleRealtimeTask();
    }

    public interface MainPresenter {

        /**
         * 此方法用于:轮询服务器
         *
         * @param command
         *         命令名称
         * @param mac
         *         设备码
         * @return
         * @author.Alex.on.a
         * @throw
         */
        void pollingTask(String command, String mac);

        void downLoadFile(String command, String mac, String path);

    }

}
