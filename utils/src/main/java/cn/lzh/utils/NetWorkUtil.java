package cn.lzh.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import android.telephony.TelephonyManager;

import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;

/**
 * NetWork Utils
 * @author open source
 * <ul>
 * <strong>Attentions</strong>
 * <li>You should add <strong>android.permission.ACCESS_NETWORK_STATE</strong> in manifest, to get network status.</li>
 * </ul>
 *
 * @see #isConnected(Context) isConnected
 * @see #isWifi(Context) isWifi
 * @see #isFastMobileNetwork(Context) isFastMobileNetwork
 * @see #getNetworkType(Context) getNetworkType
 */
public final class NetWorkUtil {

    public static final String NETWORK_TYPE_WIFI = "WIFI";
    public static final String NETWORK_TYPE_4G = "4G";
    public static final String NETWORK_TYPE_3G = "3G";
    public static final String NETWORK_TYPE_2G = "2G";
    public static final String NETWORK_TYPE_WAP = "WAP";
    public static final String NETWORK_TYPE_UNKNOWN = "UNKNOWN";
    public static final String NETWORK_TYPE_DISCONNECT = "DISCONNECT";


    private NetWorkUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取当前网络类型信息
     *
     * @param context Context
     * @return NetworkInfo
     */
    private static NetworkInfo getNetworkInfo(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 判断网络是否连接
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isConnected(@NonNull Context context) {
        NetworkInfo ni = getNetworkInfo(context);
        return ni != null && ni.isConnected() && ni.isAvailable();
    }

    /**
     * 判断是否是wifi连接
     * @param context Context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        NetworkInfo ni = getNetworkInfo(context);
        return ni != null && ni.getType() == TYPE_WIFI;

    }

    /**
     * 获取当前网络类型
     *
     * @param context Context
     * @return NETWORK_TYPE_*
     */
    public static String getNetworkType(Context context) {
        String networkType = NETWORK_TYPE_UNKNOWN;
        NetworkInfo networkInfo = getNetworkInfo(context);
        if (networkInfo == null || !networkInfo.isConnected()) {
            networkType = NETWORK_TYPE_DISCONNECT;
        } else if (networkInfo.getType() == TYPE_WIFI) {
            networkType = NETWORK_TYPE_WIFI;
        } else if (networkInfo.getType() == TYPE_MOBILE) {
            networkType = getMobileType(networkInfo);
        }
        return networkType;
    }

    @NonNull
    private static String getMobileType(NetworkInfo networkInfo) {
        String strNetworkType;
        String _strSubTypeName = networkInfo.getSubtypeName();
        // TD-SCDMA networkType is 17
        int networkType = networkInfo.getSubtype();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace
                // by 11
                strNetworkType = NETWORK_TYPE_2G;
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 :
                // replace by 14
            case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 :
                // replace by 12
            case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 :
                // replace by 15
                strNetworkType = NETWORK_TYPE_3G;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace
                // by 13
                strNetworkType = NETWORK_TYPE_4G;
                break;
            default:
                // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信
                // 三种3G制式
                if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                        || _strSubTypeName.equalsIgnoreCase("WCDMA")
                        || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                    strNetworkType = NETWORK_TYPE_3G;
                } else {
                    strNetworkType = _strSubTypeName;
                }

                break;
        }
        return strNetworkType;
    }

    /**
     * Whether is fast mobile network
     *
     * @param context Context
     * @return boolean
     */
    public static boolean isFastMobileNetwork(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }
        switch (tm.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

}
