package com.hc.posterccb.util.system;

import android.content.Context;
import android.media.AudioManager;

import com.hc.posterccb.application.ProApplication;
import com.hc.posterccb.util.LogUtils;

/**
 * Created by alex on 2017/7/22.
 */

public class VolumeUtils {
    public static void setVolum(double direction){
        direction=15*(direction/100);
        AudioManager am=(AudioManager) ProApplication.getmContext().getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) direction, AudioManager.STREAM_MUSIC);
        int max =am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//得到媒体音量的最大值
        LogUtils.e("系统最大音量",max+"");
        int current=am.getStreamVolume(AudioManager.STREAM_MUSIC);//得到媒体音量的当前值
        LogUtils.e("当前系统音量",current+"");
    }
}
