package cn.lzh.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import cn.lzh.utils.security.Base64Util;

/**
 * Created by lzh on 2017/9/18.<br/>
 */
@RunWith(AndroidJUnit4.class)
public class CommonAndroidTest {

    private static final String TAG = "CommonAndroidTest";
    private static final int GB = (int) Math.pow(2, 30);

    @Test
    public void testFileUtil() {
        log(("formatFileSize:" + FileUtil.formatFileSize(getContext(), (long) (GB * 666.66))));
    }


    @Test
    public void testBase64() {
        String text = "测试Base64加密";
        String base64 = Base64Util.encodeBase64(text);
        log("base64=" + base64);
        assert text.equals(Base64Util.decodeBase64(base64));
    }

    @Test
    public void testSystemUtil() {
        Context appContext = getContext();
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

    private Context getContext() {
        return InstrumentationRegistry.getTargetContext();
    }

    private void log(String str) {
        Log.d(TAG, str);
    }

}
