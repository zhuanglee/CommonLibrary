package cn.lzh.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CommonUtil {

	/**
	 * 发送媒体库扫描广播
	 *
	 * @param context
	 * @param file
	 */
	public static void sendMediaScannerBroadcast(Context context, File file) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			mediaScanIntent.setData(Uri.fromFile(file));
			context.sendBroadcast(mediaScanIntent);
		} else {
			context.sendBroadcast(new Intent(
					Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		}
	}

}
