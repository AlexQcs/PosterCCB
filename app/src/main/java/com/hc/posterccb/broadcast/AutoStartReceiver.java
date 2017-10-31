package com.hc.posterccb.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hc.posterccb.ui.acitivity.WelcomeActivity;

/**
 * Created by alex on 2017/7/28.
 */

public class AutoStartReceiver extends BroadcastReceiver {
    private final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent i = new Intent(context, WelcomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
