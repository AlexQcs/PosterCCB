package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.polling.RealTimeMsgBean;
import com.hc.posterccb.bean.program.Program;

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

        //暂停播放
        void pauseVideo();

        //恢复播放
        void replayVideo();

        //删除播放列表
        void deleteProgramList();

        //取消插播
        void cancleInterruptVideo();

        void license();

        void noLicense();

        void logicNormalProgram(Program program);

        void logicInterProgram(Program program);
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
        void pollingGatTask(String command, String mac);

        void downLoadFile(String command, String mac, String path);

        void checkLicense();

        void initConfig();
    }

}
