package com.hc.posterccb.util.system;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.hc.posterccb.Constant;
import com.hc.posterccb.util.file.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2016/9/10.
 */
public class AppVersionTools {

    public static boolean needUpdate(Context context) throws NameNotFoundException {
        try {
            if (getApkVersionName(context).equals(getAppVersionName(context)))
                return false;
        } catch (NameNotFoundException e) {
            throw e;
        }
        return true;
    }

    //获取app版本名
    public static String getAppVersionName(Context context) throws NameNotFoundException {
        String versionName = "";
        try {
            //获取包名详细
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            Log.e("源版本号", versionName);
            if (versionName == null || versionName.length() == 0) return "";
        } catch (NameNotFoundException e) {
            throw e;
        }
        return versionName;
    }

    public static String getApkVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        List<String> apkpath = new ArrayList<>();
        apkpath = FileUtils.getPathOfDirectory(Constant.LOCAL_APK_PATH);
        if (apkpath.size() == 0) return "";
        PackageInfo info = pm.getPackageArchiveInfo(apkpath.get(0), PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
//            String packageName = appInfo.packageName;  //得到安装包名称
            String version = info.versionName;       //得到版本信息
            Log.e("目标版本号", version);
//            Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            return version;
        }
        return "";
    }
}
