package com.hc.posterccb.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by alex on 2017/7/28.
 */

public class UpdateAutoReceiver extends BroadcastReceiver {
    private final String ACTION = "android.intent.action.PACKAGE_REMOVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            try {
                if (intent.getDataString().contains("com.hc.posterccb")) {
                    Intent myIntent = new Intent();
                    PackageManager pm = context.getPackageManager();
                    try {
//                        myIntent=pm.getLaunchIntentForPackage(intent.getDataString().substring(8));
                        myIntent = pm.getLaunchIntentForPackage("com.hc.posterccb");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
//                    ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
//                    am.restartPackage("com.hc.busystation");
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
