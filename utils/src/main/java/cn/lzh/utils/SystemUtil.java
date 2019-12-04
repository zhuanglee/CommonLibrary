package cn.lzh.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * SystemUtil
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-15
 * @see #isART() isART
 * @see #isApplicationInBackground(Context) isApplicationInBackground
 * @see #isDalvik() isDalvik
 * @see #isMainProcess(Context) isMainProcess
 * @see #isNamedProcess(Context, String) isNamedProcess
 * @see #isRunningService(Context, String) isRunningService
 * @see #isRunningService(Context, Class) isRunningService
 * @see #gc(Context) gc
 * @see #getAllApps(Context) getAllApps
 * @see #getCpuCoreNumber() getCpuCoreNumber
 * @see #getCurrentRuntimeValue() getCurrentRuntimeValue
 * @see #getDefaultThreadPoolSize() getDefaultThreadPoolSize
 * @see #getDefaultThreadPoolSize(int) getDefaultThreadPoolSize
 * @see #getDeviceUsableMemory(Context) getDeviceUsableMemory
 * @see #getPhoneModel() getPhoneModel
 * @see #getProcessName(int) getProcessName
 * @see #getSDKVersion() getSDKVersion
 * @see #getSign(Context, String) getSign
 * @see #getSystemVersion() getSystemVersion
 * @see #getTopActivity(Context) getTopActivity
 * @see #getUidByPackageName(Context, String) getUidByPackageName
 * @see #getWifiIpAddress(Context) getWifiIpAddress
 * @see #stopRunningService(Context, String) stopRunningService
 */
public final class SystemUtil {

    private static final String TAG = "SystemUtil";
    private static final boolean DEBUG = cn.lzh.utils.BuildConfig.DEBUG;

    /**
     * recommend default thread pool size according to system available processors, {@link #getDefaultThreadPoolSize()}
     **/
    public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

    private SystemUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 得到CPU核心数
     *
     * @return CPU核心数
     */
    public static int getCpuCoreNumber() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(pathname -> Pattern.matches("cpu[0-9]", pathname.getName()));
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * get recommend default thread pool size
     *
     * @return if 2 * availableProcessors + 1 less than 8, return it, else return 8;
     * @see {@link #getDefaultThreadPoolSize(int)} max is 8
     */
    public static int getDefaultThreadPoolSize() {
        return getDefaultThreadPoolSize(8);
    }

    /**
     * get recommend default thread pool size
     *
     * @param max 最大线程数
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }

    /**
     * 获取Android系统版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机厂商
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备ID：
     * GSM（Global System for Mobile Communications）全球移动通讯系统<br/>
     * IMEI（International Mobile Equipment Identity）国际移动设备识别码，是GSM手机的身份识别码<br/>
     * CDMA（Code Division Multiple Access）码分多址<br/>
     * MEID（Mobile Equipment Identifier）移动设备识别码，是CDMA手机的身份识别码<br/>
     *
     * @param context Context
     * @deprecated 不准确，且需要 READ_PHONE_STATE 权限
     * @return 设备ID
     */
    @Deprecated
    @Nullable
    public static String getDeviceId(Context context) {
        String deviceId = null;
        Context appContext = context.getApplicationContext();
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE);
            if(tm == null){
                return null;
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                deviceId = tm.getImei();
                if(deviceId == null){
                    deviceId = tm.getMeid();
                }
            }else{
                deviceId = tm.getDeviceId();
            }
        }
        return deviceId;
    }

    /**
     * 获取Wifi的IP地址
     *
     * @param context Context
     */
    @Nullable
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager == null) {
            return null;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        Formatter.formatIpAddress(wifiInfo.getIpAddress());
        return ipIntToString(wifiInfo.getIpAddress());
    }

    /**
     * 将int类型的IP转换成字符串形式的IP
     *
     * @param ip int类型的IP
     */
    private static String ipIntToString(int ip) {
        try {
            byte[] bytes = new byte[4];
            bytes[0] = (byte) (0xff & ip);
            bytes[1] = (byte) ((0xff00 & ip) >> 8);
            bytes[2] = (byte) ((0xff0000 & ip) >> 16);
            bytes[3] = (byte) ((0xff000000 & ip) >> 24);
            return Inet4Address.getByAddress(bytes).getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据包名获取用户ID
     *
     * @param context Context
     * @param packageName String
     */
    public static int getUidByPackageName(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(
                packageName, PackageManager.GET_META_DATA).applicationInfo.uid;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context Context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null){
            return false;
        }
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList == null || taskList.isEmpty()) {
            return true;
        }
        ComponentName topActivity = taskList.get(0).topActivity;
        return topActivity != null && !topActivity.getPackageName().equals(context.getPackageName());
    }


    /**
     * 获取任务栈栈顶Activity的类名
     *
     * @param context Context
     */
    @Nullable
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        return tasks == null || tasks.isEmpty() ? null : tasks.get(0).topActivity.getClassName();
    }

    /**
     * 检测服务是否运行
     *
     * @param context Context
     * @param clazz   服务类实例
     * @return 是否运行
     */
    public static boolean isRunningService(Context context, Class clazz) {
        return isRunningService(context, clazz.getName());
    }

    /**
     * 检测服务是否运行
     *
     * @param context   上下文
     * @param className 服务的类名
     * @return 是否运行
     */
    public static boolean isRunningService(Context context, String className) {
        boolean isRunningService = false;
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        List<ActivityManager.RunningServiceInfo> services = manager
                .getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : services) {
            if (className.equals(service.service.getClassName())) {
                isRunningService = true;
                break;
            }
        }
        return isRunningService;
    }

    /**
     * 停止运行服务
     *
     * @param context   上下文
     * @param className 类名
     * @return 停止运行是否成功
     */
    public static boolean stopRunningService(Context context, String className) {
        try {
            return context.stopService(new Intent(context, Class.forName(className)));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否为主进程
     * @param context Context
     */
    public static boolean isMainProcess(Context context) {
        return isNamedProcess(context, context.getPackageName());
    }

    /**
     * whether this process is named with processName
     *
     * @param context Context
     * @param processName 进程名
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null || processName == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null){
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        if (processInfoList == null || processInfoList.isEmpty()) {
            return false;
        }
        int pid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && processName.equals(processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    @Nullable
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取设备的可用内存大小
     *
     * @param context 应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null){
            return 0;
        }
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 获取系统中所有的应用
     *
     * @param context 上下文
     * @return 应用信息List
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                apps.add(packageInfo);
            }
        }
        return apps;
    }

    /**
     * 是否ART模式
     *
     * @return 结果
     */
    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART-DEBUG".equals(currentRuntime);
    }

    /**
     * 是否Dalvik模式
     *
     * @return 结果
     */
    public static boolean isDalvik() {
        return "Dalvik".equals(getCurrentRuntimeValue());
    }

    /**
     * 判断Android版本是否为6.0以后
     */
    public static boolean isM(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * 获取手机当前的Runtime
     *
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    @Nullable
    public static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get",
                    String.class, String.class);
            final String value = (String) get.invoke(
                    systemProperties, "persist.sys.dalvik.vm.lib",
                    /* Assuming default is */"Dalvik");
            if ("libdvm.so".equals(value)) {
                return "Dalvik";
            } else if ("libart.so".equals(value)) {
                return "ART";
            } else if ("libartd.so".equals(value)) {
                return "ART-DEBUG";
            }
            return value;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取应用签名
     *
     * @param context 上下文
     * @param pkgName 包名
     */
    @Nullable
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
            return hexDigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param bytes 签名byte数组
     * @return 32位签名字符串
     */
    @Nullable
    private static String hexDigest(byte[] bytes) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102};
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            byte[] digestBytes = md5.digest();
            char[] chars = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(chars);
                }
                int k = digestBytes[i];
                chars[j] = hexDigits[(0xF & k >>> 4)];
                chars[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 清理后台进程与服务
     *
     * @param context 应用上下文对象context
     * @return 被清理的数量
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static int gc(Context context) {
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null)
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid())
                    continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

        // 获取正在运行的进程列表
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null)
            for (ActivityManager.RunningAppProcessInfo process : processList) {
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    // pkgList 得到该进程下运行的包名
                    String[] pkgList = process.pkgList;
                    for (String pkgName : pkgList) {
                        try {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        } catch (Exception e) { // 防止意外发生
                            e.getStackTrace();
                        }
                    }
                }
            }
        return count;
    }

}
