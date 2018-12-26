package cn.lzh.utils;

import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;
import java.util.Locale;

/**
 * 通过Intent和系统进行交互
 * @author from open source
 * @see #getLaunch(Context, String) getLaunch
 * @see #installShortcut(String, ShortcutIconResource, String) installShortcut
 * @see #notifyMediaScannerScanFile(Context, File) notifyMediaScannerScanFile
 * @see #openAppDetail(String) openAppDetail
 * @see #openBrowser(String) openBrowser
 * @see #openCall(String) openCall
 * @see #openContacts() openContacts
 * @see #openFile(String) openFile
 * @see #openHome() openHome
 * @see #openWifiSettings() openWifiSettings
 * @see #openWirelessSettings() openWirelessSettings
 * @see #sendEmail(String[]) sendEmail
 * @see #share(String, String, String) share
 * @see #unInstallShortcut(String, ShortcutIconResource, String) unInstallShortcut
 */
public class IntentUtil {

    private IntentUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 通知媒体库扫描文件
     *
     * @param context Context
     * @param file    File
     */
    public static void notifyMediaScannerScanFile(Context context, File file) {
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
     * 打开Wireless设置界面
     */
    public static Intent openWirelessSettings() {
        return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    }

    /**
     * 打开WIFI设置界面
     */
    public static Intent openWifiSettings() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }


    /**
     * 获取跳转到桌面的意图
     */
    public static Intent openHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    /**
     * 删除程序的快捷方式
     *
     * @param iconRes 快捷方式图标
     */
    public static Intent unInstallShortcut(String name,
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
     * @param iconRes 快捷方式图标
     */
    public static Intent installShortcut(String name,
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
     * @param subject 分享的主题
     * @param text    分享的文本
     * @param title   分享的标题
     */
    public static Intent share(String subject, String text, String title) {
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
    public static Intent openAppDetail(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 获取打开系统联系人的意图
     */
    public static Intent openContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        return intent;
    }

    /**
     * 获取浏览意图
     *
     * @param uri 网址
     */
    public static Intent openBrowser(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        return Intent.createChooser(intent, "请选择浏览器");
    }

    /**
     * 获取打电话意图
     *
     * @param tel 电话号码
     */
    public static Intent openCall(String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        return Intent.createChooser(intent, "请选择拨号器");
    }

    /**
     * 获取发送邮件意图
     *
     * @param email 邮件地址
     */
    public static Intent sendEmail(String[] email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc882");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_CC, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        return Intent.createChooser(intent, "发送邮件");
    }

    /**
     * 获取应用的启动意图
     *
     * @param context     Context
     * @param packageName 包名
     */
    public static Intent getLaunch(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        return pm.getLaunchIntentForPackage(packageName);
    }

    /**
     * 获取打开指定文件的Intent
     *
     * @param filePath 文件路径
     */
    public static Intent openFile(String filePath) {
        return OpenFileUtil.getOpenFileIntent(filePath);
    }

    /**
     * 打开文件的工具类
     */
    private static final class OpenFileUtil {

        static Intent getOpenFileIntent(String filePath) {
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
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "video/*");
            return intent;
        }

        // Audio
        private static Intent getAudioFileIntent(String param) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
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
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "image/*");
            return intent;
        }

        // TXT
        private static Intent getTextFileIntent(String param) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
            return intent;
        }

        // PDF
        private static Intent getPdfFileIntent(String param) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(new File(param));
            intent.setDataAndType(uri, "application/pdf");
            return intent;
        }
    }
}
