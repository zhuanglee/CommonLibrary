package cn.lzh.common.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import android.util.Log;

import cn.lzh.utils.SystemUtil;
import cn.lzh.utils.security.BinaryUtil;

/**
 * Created by lzh on 2018/9/17.<br/>
 */
public class Test {

    private static void log(String str) {
        Log.i("Test", str);
    }

    public static void testBase64() {
        String text = "测试Base64加密";
        String str = BinaryUtil.toBase64String(text);
        log(str);
        log(new String(BinaryUtil.fromBase64String(str)));
    }

    public static void testSystemUtil(Context appContext) {
        log("getCpuCoreNumber=" + SystemUtil.getCpuCoreNumber());
        log("getDefaultThreadPoolSize=" + SystemUtil.getDefaultThreadPoolSize(1000));
        log("getSystemVersion=" + SystemUtil.getSystemVersion());
        log("getSDKVersion=" + SystemUtil.getSDKVersion());
        log("getPhoneModel=" + SystemUtil.getPhoneModel());
        log("getCurrentRuntimeValue=" + SystemUtil.getCurrentRuntimeValue());
        log("getDeviceId=" + SystemUtil.getDeviceId(appContext));
        log("getWifiIpAddress=" + SystemUtil.getWifiIpAddress(appContext));
        log("getWifiIpAddress=" + SystemUtil.getWifiIpAddress(appContext));
        log("isApplicationInBackground=" + SystemUtil.isApplicationInBackground(appContext));
        log("getTopActivity=" + SystemUtil.getTopActivity(appContext));
        log("isMainProcess=" + SystemUtil.isMainProcess(appContext));
        log("getProcessName=" + SystemUtil.getProcessName(Process.myPid()));
        try {
            log("getUidByPackageName=" + SystemUtil.getUidByPackageName(appContext, appContext.getPackageName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String serviceClassName = "cn.lzh.interview.service.DayDreamService";
        log("isRunningService=" + SystemUtil.isRunningService(appContext, serviceClassName));
        log("isRunningService=" + SystemUtil.stopRunningService(appContext, serviceClassName));
        log("getDeviceUsableMemory=" + SystemUtil.getDeviceUsableMemory(appContext));
        log("getAllApps=" + SystemUtil.getAllApps(appContext));
        log("getSign=" + SystemUtil.getSign(appContext, appContext.getPackageName()));
    }
}
