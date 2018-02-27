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
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
 */
public class SystemUtil {

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
     * @param max
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }

    /**
     * 获取设备唯一标识 本方法调用需要READ_PHONE_STATE权限
     *
     * @param context
     * @return
     * @deprecated 需要READ_PHONE_STATE权限
     */
    @Deprecated
    public static String getUUID(Context context) {
        String tmDevice = "", tmSerial = "", tmPhone = "", androidId = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                if (tm != null) {
                    tmDevice = "" + tm.getDeviceId();
                    tmSerial = "" + tm.getSimSerialNumber();
                    androidId = "" + android.provider.Settings.Secure.getString(
                            context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                Log.e("AppUtil", "exception:" + e.getMessage());
            }
        } else {
            Log.e(TAG, "没有 android.permission.READ_PHONE_STATE 权限");
            tmDevice = "device";
            tmSerial = "serial";
            androidId = "androidId";
        }


        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        if (DEBUG) Log.d(TAG, "uuid=" + uniqueId);

        return uniqueId;
    }

    /**
     * 获取设备ID
     *
     * @param context Context
     * @return 设备ID
     * @deprecated 不准确，且需要READ_PHONE_STATE权限
     */
    @Deprecated
    @Nullable
    public static String getDeviceId(Context context) {
        Context appContext = context.getApplicationContext();
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            return ((TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } else {
            return null;
        }
    }

    /**
     * 获取手机厂商
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取Android系统版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
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
     * 根据资源id的名称获取资源的id
     *
     * @param context        Context
     * @param resourceIdName 资源名称
     */
    public static int getResourceId(Context context, String resourceIdName) {
        return context.getResources().getIdentifier(resourceIdName, null, null);
    }

    /**
     * 根据包名获取用户ID
     *
     * @param context
     * @param packageName
     */
    public static int getUidByPackageName(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(
                packageName, PackageManager.GET_META_DATA).applicationInfo.uid;
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
     * 改变文件权限
     *
     * @param file 文件完整路径
     */
    public static void changeFilePermission(String file) {
        String command = "chmod 777 " + file;
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到CPU核心数
     *
     * @return CPU核心数
     */
    public static int getCpuCoreNumber() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
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
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
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
     * 是否ART模式
     *
     * @return 结果
     */
    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART debug build".equals(currentRuntime);
    }

    /**
     * 获取手机当前的Runtime
     *
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    public static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get",
                        String.class, String.class);
                if (get == null) {
                    return "WTF?!";
                }
                try {
                    final String value = (String) get.invoke(
                            systemProperties, "persist.sys.dalvik.vm.lib",
                        /* Assuming default is */"Dalvik");
                    if ("libdvm.so".equals(value)) {
                        return "Dalvik";
                    } else if ("libart.so".equals(value)) {
                        return "ART";
                    } else if ("libartd.so".equals(value)) {
                        return "ART debug build";
                    }

                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
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


}
