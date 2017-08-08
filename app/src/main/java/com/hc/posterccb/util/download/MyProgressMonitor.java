package com.hc.posterccb.util.download;

import com.hc.posterccb.util.LogUtils;

/**
 * Created by alex on 2017/8/8.
 */

public class MyProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {

    private long transfered;

    @Override
    public void init(int i, String s, String s1, long l) {
        LogUtils.e("下载状态","开始");
    }

    @Override
    public boolean count(long count) {

        transfered=transfered+count;
        if (transfered<1024){
            LogUtils.e("当前下载文件大小",transfered+"bytes");
        }
        if ((transfered>1024)&&(transfered<1048576)){
            LogUtils.e("当前下载文件大小",transfered/1024+"kb");
        }else {
            LogUtils.e("当前下载文件大小",transfered/1024/1024+"m");
        }

        return true;
    }

    @Override
    public void end() {
        LogUtils.e("下载状态","结束");
    }
}
