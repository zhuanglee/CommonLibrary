package cn.lzh.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * AppUtil
 * @author from open source
 * @see #getActivities(Context)
 * @see #getMetaValue(Context, String)
 * @see #getPackageInfo(Context)
 * @see #getVersionCode(Context)
 * @see #getVersionName(Context)
 */
public final class AppUtil {

    private AppUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取清单文件中的常量
     *
     * @param context Context
     * @param metaKey metaKey
     */
    public static String getMetaValue(@NonNull Context context,@NonNull String metaKey) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                Bundle metaData = ai.metaData;
                if (null != metaData) {
                    return metaData.getString(metaKey);
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序的所有Activity
     *
     * @param context Context
     */
    public static ArrayList<String> getActivities(Context context) {
        ArrayList<String> result = new ArrayList<String>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setPackage(context.getPackageName());
        for (ResolveInfo info : context.getPackageManager()
                .queryIntentActivities(intent, 0)) {
            result.add(info.activityInfo.name);
        }
        return result;
    }

    public static PackageInfo getPackageInfo(@NonNull Context context){
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到软件版本号
     *
     * @param context 上下文
     * @return 当前版本Code
     */
    public static int getVersionCode(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if(packageInfo == null){
            return 0;
        }
        return packageInfo.versionCode;
    }

    /**
     * 得到软件显示版本信息
     *
     * @param context 上下文
     * @return 当前版本信息
     */
    public static String getVersionName(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if(packageInfo == null){
            return null;
        }
        return packageInfo.versionName;
    }

}
