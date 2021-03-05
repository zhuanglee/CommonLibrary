package cn.lzh.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

import java.io.File;
import java.util.Collections;
import java.util.Locale;

/**
 * 通过 Intent 和系统进行交互
 *
 * @author open source
 * @see #getLaunchIntentForPackage(Context, String)
 * @see #getNotifyMediaScanner(Context, File)
 * @see #getOpenAppDetail(String)
 * @see #getOpenBrowser(String)
 * @see #getOpenCall(String)
 * @see #getOpenContacts()
 * @see #getOpenFile(String)
 * @see #getOpenHome()
 * @see #getOpenWifiSettings()
 * @see #getOpenWirelessSettings()
 * @see #getSendEmail(String[], String[], String[], String, String)
 * @see #getSendSms(String, String)
 * @see #getShareTextIntent(String, String, String)
 * @see #installShortcut(Activity, String, int, Intent, int)
 */
public class IntentUtil {

    private IntentUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 添加快捷方式
     *
     * @param iconResId 快捷方式图标
     * @return {@code true} if the launcher supports this feature
     */
    public static boolean installShortcut(
            Activity activity,
            String name,
            @DrawableRes int iconResId,
            Intent actionIntent,
            int requestCode
    ) {
        // 对应的 Activity 会收到回调
        PendingIntent pendingIntent = PendingIntent.getActivity(activity,
                requestCode, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        ShortcutInfoCompat shortcutInfo = buildShortcutInfo(activity, name, iconResId, actionIntent);
        boolean requestPinShortcut = ShortcutManagerCompat.requestPinShortcut(activity, shortcutInfo,
                pendingIntent.getIntentSender());
        if (!requestPinShortcut && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            // Android 7.0
            addDynamicShortcuts(activity, name, iconResId, actionIntent);
        }
        return requestPinShortcut;
    }

    /**
     * 添加动态的快捷方式
     *
     * @param iconResId 快捷方式图标
     */
    public static void addDynamicShortcuts(
            Activity activity,
            String name,
            @DrawableRes int iconResId,
            Intent actionIntent
    ) {
        ShortcutInfoCompat shortcutInfo = buildShortcutInfo(activity, name, iconResId, actionIntent);
        ShortcutManagerCompat.addDynamicShortcuts(activity, Collections.singletonList(shortcutInfo));
    }

    /**
     * 构建快捷方式信息
     * @param context Context
     * @param name 快捷方式的 id、名字
     * @param iconResId 快捷方式的图标
     * @param actionIntent 快捷方式的意图
     * @return ShortcutInfo
     */
    private static ShortcutInfoCompat buildShortcutInfo(Context context, String name, @DrawableRes int iconResId, Intent actionIntent) {
        return new ShortcutInfoCompat.Builder(context, name)
                .setShortLabel(name)
                .setLongLabel(name)
                .setIcon(IconCompat.createWithResource(context, iconResId))
                .setIntent(actionIntent)
                .build();
    }

    /**
     * 移除动态的快捷方式
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public static void removeDynamicShortcuts(
            Activity activity,
            String name
    ) {
//        ShortcutManagerCompat.removeDynamicShortcuts(activity, Collections.singletonList(name));
        ShortcutManager shortcutManager = (ShortcutManager) activity.getSystemService(Context.SHORTCUT_SERVICE);
        if (shortcutManager != null) {
            shortcutManager.removeDynamicShortcuts(Collections.singletonList(name));
        }
    }

    /**
     * 分享文本
     *
     * @param subject 分享的主题
     * @param text    分享的文本
     * @param title   分享的标题
     */
    public static Intent getShareTextIntent(String subject, String text, String title) {
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
     * 查看应用详情
     */
    public static Intent getOpenAppDetail(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 打开浏览器
     *
     * @param uri 网址
     */
    public static Intent getOpenBrowser(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        return Intent.createChooser(intent, "请选择浏览器");
    }

    /**
     * 启动打电话界面
     *
     * @param tel 电话号码
     */
    public static Intent getOpenCall(String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + tel));
        return Intent.createChooser(intent, "请选择拨号器");
    }

    /**
     * 打电话
     *
     * @param tel 电话号码
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static Intent getCall(String tel) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + tel));
        return Intent.createChooser(intent, "请选择拨号器");
    }

    /**
     * 打开通讯录
     */
    public static Intent getOpenContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        return intent;
    }

    /**
     * 跳转到桌面
     */
    public static Intent getOpenHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return intent;
    }

    /**
     * 打开地图，并跳转到指定坐标
     *
     * @param latitude  纬度
     * @param longitude 经度
     */
    public static Intent getOpenMap(float latitude, float longitude) {
        Uri uri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return Intent.createChooser(intent, "请选择地图");
    }

    /**
     * 打开 Wireless 设置界面
     */
    public static Intent getOpenWirelessSettings() {
        return new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    }

    /**
     * 打开 WIFI 设置界面
     */
    public static Intent getOpenWifiSettings() {
        return new Intent(Settings.ACTION_WIFI_SETTINGS);
    }

    /**
     * 发短信
     *
     * @param tel 电话号码
     */
    public static Intent getSendSms(String tel, String body) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + tel));
        // 设置消息体
        intent.putExtra("sms_body", body);
        return intent;
    }

    /**
     * 发送邮件
     *
     * @param email 邮件地址
     */
    public static Intent getSendEmail(String[] email, String[] ccEmail,
                                      String[] bccEmail, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc882");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_CC, ccEmail);
        intent.putExtra(Intent.EXTRA_BCC, bccEmail);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(intent, "发送邮件");
    }

    /**
     * 获取应用的启动意图
     *
     * @param context     Context
     * @param packageName 包名
     */
    public static Intent getLaunchIntentForPackage(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        return pm.getLaunchIntentForPackage(packageName);
    }

    /**
     * 通知媒体库扫描文件
     *
     * @param context Context
     * @param file    File
     */
    public static void getNotifyMediaScanner(Context context, File file) {
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
     * 打开指定文件
     *
     * @param filePath 文件路径
     */
    public static Intent getOpenFile(String filePath) {
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
                    .substring(file.getName().lastIndexOf(".") + 1).toLowerCase(Locale.CHINA);
            // 根据后缀名设置intent
            switch (end) {
                case "mp3":
                case "mid":
                case "ogg":
                case "wav":
                    return getAudioFileIntent(filePath);
                case "mp4":
                    return getVideoFileIntent(filePath);
                case "jpg":
                case "gif":
                case "png":
                case "jpeg":
                case "bmp":
                    return getImageFileIntent(filePath);
                case "apk":
                    return getApkFileIntent(filePath);
                case "txt":
                    return getTextFileIntent(filePath);
                case "html":
                case "htm":
                    return getHtmlFileIntent(filePath);
                case "pdf":
                    return getPdfFileIntent(filePath);
                default:
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