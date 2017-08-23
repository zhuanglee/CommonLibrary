package cn.lzh.utils;

import android.util.Log;

/**
 * 
 * 日志工具类:先判断APP是否属于调试模式,再决定是否打印Log
 * 
 * @author lzh
 * 
 */
public class LogUtils {
	public static final String TAG = LogUtils.class.getName();

	public static void d(String msg) {
		debug(null, msg);
	}

	public static void i(String msg) {
		info(null, msg);
	}

	public static void w(String msg) {
		warn(null, msg);
	}

	public static void e(String msg) {
		error(null, msg);
	}

	/**
	 * @param tag
	 *            可取值-->null:默认tag, String:tag, Object:获取类名tag
	 * @param msg
	 */
	public static void debug(Object tag, String msg) {
		if (BuildConfig.DEBUG) {
			if (tag != null) {
				if (tag instanceof String) {
					Log.d(String.valueOf(tag), msg);
				} else {
					Log.d(tag.getClass().getName(), msg);
				}
			} else {
				Log.d(TAG, msg);
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            可取值-->null:默认tag, String:tag, Object:获取类名tag
	 * @param msg
	 */
	public static void info(Object tag, String msg) {
		if (BuildConfig.DEBUG) {
			if (tag != null) {
				if (tag instanceof String) {
					Log.i(String.valueOf(tag), msg);
				} else {
					Log.i(tag.getClass().getName(), msg);
				}
			} else {
				Log.i(TAG, msg);
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            可取值-->null:默认tag, String:tag, Object:获取类名tag
	 * @param msg
	 */
	public static void warn(Object tag, String msg) {
		if (BuildConfig.DEBUG) {
			if (tag != null) {
				if (tag instanceof String) {
					Log.w(String.valueOf(tag), msg);
				} else {
					Log.w(tag.getClass().getName(), msg);
				}
			} else {
				Log.w(TAG, msg);
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            可取值-->null:默认tag, String:tag, Object:获取类名tag
	 * @param msg
	 */
	public static void error(Object tag, String msg) {
		if (BuildConfig.DEBUG) {
			if (tag != null) {
				if (tag instanceof String) {
					Log.e(String.valueOf(tag), msg);
				} else {
					Log.e(tag.getClass().getName(), msg);
				}
			} else {
				Log.e(TAG, msg);
			}
		}
	}

	/**
	 * 输出到控制台
	 * 
	 * @param msg
	 */
	public static void syso(String msg) {
		if (BuildConfig.DEBUG) {
			System.out.println(msg);
		}
	}

}
