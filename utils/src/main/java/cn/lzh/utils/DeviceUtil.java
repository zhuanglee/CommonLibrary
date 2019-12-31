package cn.lzh.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

/**
 * 设备工具类
 * @author open source
 * @see #dip2px(Context, float) dip2px
 * @see #getScreenHeight(Context) getScreenHeight
 * @see #getScreenWidth(Context) getScreenWidth
 * @see #getStatusBarHeight(Context) getStatusBarHeight
 * @see #px2dip(Context, float) px2dip
 * @see #px2sp(Context, float) px2sp
 * @see #snapShotWithoutStatusBar(Activity) snapShotWithoutStatusBar
 * @see #snapShotWithStatusBar(Activity) snapShotWithStatusBar
 * @see #sp2px(Context, float) sp2px
 */
public class DeviceUtil {
	private DeviceUtil() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context Context
	 */
	public static int getScreenWidth(@NonNull Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return displayMetrics.widthPixels;
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context Context
	 */
	public static int getScreenHeight(@NonNull Context context) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return displayMetrics.heightPixels;
	}

    /**
     * 获得状态栏的高度
     *
     * @param activity Activity
     */
    public static int getStatusBarHeight(@NonNull Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context Context
	 */
	public static int getStatusBarHeight(Context context) {
		int statusHeight = 0;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity Activity
	 * @return Bitmap
	 */
	public static Bitmap snapShotWithStatusBar(@NonNull Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity Activity
	 * @return Bitmap
	 */
	public static Bitmap snapShotWithoutStatusBar(@NonNull Activity activity) {
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		int statusBarHeight = getStatusBarHeight(activity);
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;
	}

    public static int dip2px(@NonNull Context context, float dpValue) {
		return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
	}

	public static int px2dip(@NonNull Context context, float pxValue) {
		return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
	}

	public static int sp2px(@NonNull Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	public static float px2sp(@NonNull Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
	}

}
