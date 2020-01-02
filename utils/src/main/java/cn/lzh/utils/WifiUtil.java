package cn.lzh.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Updated by lzh on 2019/12/31.<br/>
 * Created by lzh on 2019/1/9.<br/>
 * 检测 WIFI 开关状态 和 控制 WIFI 开关
 * @see #isWifiEnabled(Context) 判断 WIFI 开关状态
 * @see #setWifiEnabled(Context, boolean) 设置 WIFI 开关状态
 * @see #toggleWifi(Context) 切换 WIFI 状态
 * @see #turnOffWifi(Context) 关闭 WIFI
 */
public final class WifiUtil {

    private WifiUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 判断 WIFI 开关状态
     *
     * @param context Context
     * @return true-打开，false-未打开
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    /**
     * 设置 WIFI 开关状态
     *
     * @param context   Context
     * @param isEnabled boolean
     */
    public static void setWifiEnabled(Context context, boolean isEnabled) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled() != isEnabled) {
            wifiManager.setWifiEnabled(isEnabled);
        }
    }

    /**
     * 切换 WIFI 状态
     *
     * @param context Context
     * @return 切换后的 WIFI 状态
     */
    public static boolean toggleWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            boolean enabled = !wifiManager.isWifiEnabled();
            wifiManager.setWifiEnabled(enabled);
            return enabled;
        }
        return false;
    }

    /**
     * 关闭 WIFI
     */
    public static boolean turnOffWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            return true;
        }
        return false;
    }

}
