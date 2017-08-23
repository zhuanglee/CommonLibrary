package cn.lzh.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import java.util.List;

/**
 * 跟网络相关的工具类
 * 
 * 
 * 
 */
public class NetUtils {
	private static final String TAG = NetUtils.class.getName();
	
	private NetUtils() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity) {

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取当前网络类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static NetworkInfo getNetworkInfo(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				return networkInfo;
			}
		}
		return null;
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		String strNetworkType = "0";
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				LogUtils.error(TAG, "networkInfo=" + networkInfo);
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					strNetworkType = "WIFI";
				} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					String _strSubTypeName = networkInfo.getSubtypeName();
					LogUtils.error(TAG, "Network getSubtypeName : "
							+ _strSubTypeName);
					// TD-SCDMA networkType is 17
					int networkType = networkInfo.getSubtype();
					switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace
																// by 11
						strNetworkType = "2G";
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
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace
															// by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信
						// 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
								|| _strSubTypeName.equalsIgnoreCase("WCDMA")
								|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							strNetworkType = "3G";
						} else {
							strNetworkType = _strSubTypeName;
						}

						break;
					}
					LogUtils.e("Network getSubtype : "
							+ Integer.valueOf(networkType).toString());
				}
			}
		}

		LogUtils.error(TAG, "NetworkType : " + strNetworkType);

		return strNetworkType;
	}
	
	/**
	 * 监测系统流量使用情况
	 * @param context
	 */
	public static void refreshTrafficStats(Context context){
		List<PackageInfo> packinfos = context.getPackageManager().getInstalledPackages(
				PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);  
		for (PackageInfo info : packinfos) {
		    String[] premissions = info.requestedPermissions;  
		    if (premissions != null && premissions.length > 0) {  
		        for (String premission : premissions) {  
		            if ("android.permission.INTERNET".equals(premission)) {  
		                // System.out.println(info.packageName+"访问网络");  
		                int uid = info.applicationInfo.uid;  
		                long rx = TrafficStats.getUidRxBytes(uid);  
		                long tx = TrafficStats.getUidTxBytes(uid);  
		                if (rx < 0 || tx < 0) {  
		                    System.out.println(info.packageName + "没有产生流量");  
		                } else {  
		                    System.out.println(info.packageName + "的流量信息:");  
		                    System.out.println("下载的流量" + Formatter.formatFileSize(context, rx));  
		                    System.out.println("上传的流量" + Formatter.formatFileSize(context, tx));  
		                }  
		            }  
		        }  
		        System.out.println("---------");  
		    }  
		}
	}
	
	/**
	 * 监测应用的流量使用情况
	 * @param context
	 */
	public static void refreshMyTrafficStats(Context context){
		if (context == null) {
			return;
		}
		int uid = android.os.Process.myUid();
		long rx = TrafficStats.getUidRxBytes(uid);
		long tx = TrafficStats.getUidTxBytes(uid);
		if (rx < 0 || tx < 0) {
			System.out.println(context.getPackageName() + "没有产生流量");
		} else {
			System.out.println(context.getPackageName() + "的流量信息:");
			System.out.println("下载的流量" + Formatter.formatFileSize(context, rx));
			System.out.println("上传的流量" + Formatter.formatFileSize(context, tx));
		}  
	}
}
