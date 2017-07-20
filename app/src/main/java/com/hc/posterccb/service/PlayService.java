package com.hc.posterccb.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hc.posterccb.base.BaseService;
import com.pili.pldroid.player.AVOptions;

/**
 * Created by alex on 2017/7/20.
 */

public class PlayService extends BaseService {
    private static final String TAG = "PlayService";
    private SystemReceiver mReceiver;
    private AVOptions mAVOptions;

    public final static int MEDIA_CODEC_SW_DECODE = 0;
    public final static int MEDIA_CODEC_HW_DECODE = 1;
    public final static int MEDIA_CODEC_AUTO = 2;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public void pause() {

    }

    public class SystemReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case Intent.ACTION_SCREEN_OFF:
                    pause();
                    break;
                case Intent.ACTION_SCREEN_ON:

                    break;
            }

        }
    }
}

