package cn.lzh.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
	public static final String TAG = CommonUtils.class.getName();

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Class<?> cls, ArrayList<T> items) {
		if (items == null || items.size() == 0) {
			return (T[]) Array.newInstance(cls, 0);
		}
		return items.toArray((T[]) Array.newInstance(cls, items.size()));
	}

	/**
	 * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
	 * @param sequence
	 * @return
	 */
    public static boolean checkNickname(String sequence) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        return !matcher.find();
    } 
	
	/**
	 * 检查是否包含汉字
	 * @param sequence
	 * @return
	 */
	public static boolean checkChinese(String sequence) {
        final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
        boolean result = false;
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(sequence);
        result = matcher.find();
        return result;
    }

	/**
	 * 格式化手机号码:去除" "和"-",以及"+86"
	 * 
	 * @param phone
	 * @return
	 */
	public static String formatPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return "号码为空";
		}
		if (phone.contains("-"))
			phone = phone.replace("-", "");
		if (phone.contains(" "))
			phone = phone.replace(" ", "");
		if (phone.startsWith("+86")) {
			phone = phone.replace("+86", "");
		}
		return phone;
	}

	/**
	 * 验证手机号码格式
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		return phone.matches("^1[34578]\\d{9}$");
	}

	/**
	 * 判断字符串前3位,是否是手机号码的前缀
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhonePrefix(String phone) {
		int len = phone.length();
		if (len >= 3 && len <= 11) {
			phone = phone.substring(0, 3);
		}
		return phone.matches("^1[34578]\\d$");
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
	 * 获取清单文件中的常量
	 * 
	 * @param context
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 获取任务栈栈顶Activity的类名
	 * 
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 获取应用程序的所有Activity
	 * 
	 * @param context
	 * @return
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

	/**
	 * 判断指定服务是否正在运行
	 * 
	 * @param context
	 * @param clazz
	 * @return
	 */
	public static boolean isRunningService(Context context, Class clazz) {
		boolean isRunningService = false;
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (clazz.getName().equals(service.service.getClassName())) {
				isRunningService = true;
				break;
			}
		}
		return isRunningService;
	}

	/**
	 * 获取应用程序的版本
	 * 
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getPhoneModel() {
		return Build.MODEL;
	}

	public static String getSystemVertion() {
		return Build.VERSION.RELEASE;
	}
	
	public static String getIp(Context ctx) {
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		return Formatter.formatIpAddress(wifiManager.getConnectionInfo()
				.getIpAddress());
	}
}
