package cn.lzh.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.Locale;

public class IntentUtil {

	public static final Object LOG_TAG = IntentUtil.class.getSimpleName();

	private IntentUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

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

	/**
	 * 获取调用便携式热点和数据共享设置的意图
	 * 
	 * @return
	 */
	public static Intent getTetherSettingsIntent() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		ComponentName com = new ComponentName("com.android.settings",
				"com.android.settings.TetherSettings");
		intent.setComponent(com);
		return intent;
	}

	/**
	 * 获取跳转到桌面的意图
	 * 
	 * @return
	 */
	public static Intent getHomeIntent() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		return intent;
	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * @param iconRes
	 *            快捷方式图标
	 * @return
	 */
	public static Intent getUnInstallShortcutIntent(String name,
			ShortcutIconResource iconRes, String action) {
		Intent install = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		install.putExtra("duplicate", true);// 不允许重复创建
		install.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		install.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		install.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(action));
		return install;
	}

	/**
	 * 获取添加快捷方式意图
	 * 
	 * @param iconRes
	 *            快捷方式图标
	 * @return
	 */
	public static Intent getInstallShortcutIntent(String name,
			ShortcutIconResource iconRes, String action) {
		Intent install = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		install.putExtra("duplicate", true);// 不允许重复创建
		install.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		install.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		install.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(action));
		return install;
	}

	/**
	 * 获取分享信息意图
	 * 
	 * @param subject
	 * @param text 分享的文本
	 * @param title 分享的标题
	 * @return
	 * @throws Exception
	 */
	public static Intent getShareIntent(String subject, String text,
			String title) throws Exception {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		if (!TextUtils.isEmpty(subject)) {
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		}
		if (!TextUtils.isEmpty(text)) {
			intent.putExtra(Intent.EXTRA_TEXT, text);
		}
		if (TextUtils.isEmpty(title)) {
			return intent;
		} else {
			return Intent.createChooser(intent, title);
		}
	}

	/**
	 * 获取查看应用详情意图 {act=android.settings.APPLICATION_DETAILS_SETTINGS
	 * dat=package:com.xiaomi.shop
	 * cmp=com.android.settings/.applications.InstalledAppDetails}
	 */
	public static Intent getAppDetailIntent(String packageName) {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + packageName));
		return intent;
	}

	/**
	 * 获取打开系统联系人的意图
	 * 
	 * @return
	 */
	public static Intent getContactsIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("vnd.android.cursor.dir/phone_v2");
		return intent;
	}

	/**
	 * 获取浏览意图
	 * 
	 * @param uri
	 * @return
	 * @throws Exception
	 */
	public static Intent getBrowserIntent(String uri) throws Exception {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		return Intent.createChooser(intent, "请选择浏览器");
	}

	/**
	 * 获取打电话意图
	 * 
	 * @param tel
	 * @return
	 * @throws Exception
	 */
	public static Intent getCallIntent(String tel) throws Exception {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + tel));
		return Intent.createChooser(intent, "请选择拨号器");
	}

	/**
	 * 获取发送邮件意图
	 * 
	 * @param smsto
	 * @return
	 * @throws Exception
	 */
	public static Intent getSendEmailIntent(String[] smsto) throws Exception {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc882");
		intent.putExtra(Intent.EXTRA_EMAIL, smsto);
		intent.putExtra(Intent.EXTRA_CC, "");
		intent.putExtra(Intent.EXTRA_TEXT, "");
		intent.putExtra(Intent.EXTRA_SUBJECT, "");
		return Intent.createChooser(intent, "发送邮件");
	}

	/**
	 * 获取应用的启动意图
	 * 
	 * @param mContext
	 * @param packageName
	 * @return
	 */
	public static Intent getLaunch(Context mContext, String packageName) {
		PackageManager pm = mContext.getPackageManager();
		return pm.getLaunchIntentForPackage(packageName);
	}

	/**
	 * 获取打开指定文件的Intent
	 * @param filePath 文件路径
	 * @return
	 *
	 */
	public static Intent getOpenFileIntent(String filePath){
		return OpenFileUtil.getOpenFileIntent(filePath);
	}
	
	/**
	 * 打开文件的工具类 
	 */
	private static class OpenFileUtil {

		public static Intent getOpenFileIntent(String filePath) {
			File file = new File(filePath);
			if (!file.exists())
				return null;
			// 取得后缀名，并转为小写
			String end = file.getName()
					.substring(file.getName().lastIndexOf(".") + 1,
							file.getName().length()).toLowerCase(Locale.CHINA);
			// 根据后缀名设置intent
			if (end.equals("mp3") || end.equals("mid") || end.equals("ogg")
					|| end.equals("wav")) {
				return getAudioFileIntent(filePath);
			} else if (end.equals("mp4")) {
				return getVideoFileIntent(filePath);
			} else if (end.equals("jpg") || end.equals("gif")
					|| end.equals("png") || end.equals("jpeg")
					|| end.equals("bmp")) {
				return getImageFileIntent(filePath);
			} else if (end.equals("apk")) {
				return getApkFileIntent(filePath);
			} else if (end.equals("txt")) {
				return getTextFileIntent(filePath);
			} else if (end.equals("html") || end.equals("htm")) {
				return getHtmlFileIntent(filePath);
			} else if (end.equals("pdf")) {
				return getPdfFileIntent(filePath);
			} else {
				return getAllIntent(filePath);
			}
		}

		// 打开奇怪的文件
		private static Intent getAllIntent(String param) {

			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri, "*/*");
			return intent;
		}

		// APK
		private static Intent getApkFileIntent(String param) {

			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			return intent;
		}

		// Video
		private static Intent getVideoFileIntent(String param) {

			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri, "video/*");
			return intent;
		}

		// Audio
		private static Intent getAudioFileIntent(String param) {

			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri, "audio/*");
			return intent;
		}

		// HTML
		private static Intent getHtmlFileIntent(String param) {

			Uri uri = Uri.parse(param).buildUpon()
					.encodedAuthority("com.android.htmlfileprovider")
					.scheme("content").encodedPath(param).build();
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setDataAndType(uri, "text/html");
			return intent;
		}

		// Image
		private static Intent getImageFileIntent(String param) {

			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri, "image/*");
			return intent;
		}

		// TXT
		private static Intent getTextFileIntent(String param) {

			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
			return intent;
		}

		// PDF
		private static Intent getPdfFileIntent(String param) {

			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Uri uri = Uri.fromFile(new File(param));
			intent.setDataAndType(uri, "application/pdf");
			return intent;
		}
	}
}
