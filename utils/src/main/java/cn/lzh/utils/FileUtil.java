package cn.lzh.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import cn.lzh.utils.StringUtil;

/**
 * File Utils
 * <ul>
 * <li>{@link #getFileExtension(String)}</li>
 * <li>{@link #getFileName(String)}</li>
 * <li>{@link #getFileNameWithoutExtension(String)}</li>
 * <li>{@link #getFileSize(File)}</li>
 * <li>{@link #delete(String)}</li>
 * <li>{@link #isExists(String)}</li>
 * </ul>
 *
 * @author from open source
 */
public class FileUtil {


    private static final String TAG = "FileUtil";
    private static final String SHARED_PREFS = "shared_prefs";
    private static final String FILE_SEPARATOR = "/";
    private static final String FILE_SEPARATOR_2 = "\\";
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    private FileUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取SP路径
     *
     * @param context Context
     * @return File
     */
    public static File getSharedPreferencesPath(@NonNull Context context) {
        return new File(context.getFilesDir().getParentFile(), SHARED_PREFS);
    }

    /**
     * 获取SP文件
     *
     * @param context  Context
     * @param fileName 文件名
     * @return File
     */
    public static File getSharedPreferencesFile(@NonNull Context context, @NonNull String fileName) {
        if (!fileName.endsWith(".xml")) {
            fileName += ".xml";
        }
        return new File(getSharedPreferencesPath(context), fileName);
    }

    /**
     * 获取外部缓存地址
     *
     * @param context   Context
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @return File
     */
    @Nullable
    public static File getExternalCacheDir(Context context, String cacheName) {
        return getCacheDir(context, cacheName, true);
    }

    /**
     * 获取内部缓存地址
     *
     * @param context   Context
     * @param cacheName The name of the subdirectory in which to store the cache.
     * @return File
     */
    @Nullable
    public static File getCacheDir(Context context, String cacheName) {
        return getCacheDir(context, cacheName, false);
    }

    /**
     * 获取缓存地址
     *
     * @param context         Context
     * @param cacheName       The name of the subdirectory in which to store the cache.
     * @param isExternalCache 是否为外部缓存
     * @return File
     */
    @Nullable
    private static File getCacheDir(Context context, String cacheName, boolean isExternalCache) {
        File cacheDir = isExternalCache ? context.getExternalCacheDir() : context.getCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists but not a directory
                return null;
            }
            return result;
        }
        if (Log.isLoggable(TAG, Log.ERROR)) {
            Log.e(TAG, "default disk cache dir is null");
        }
        return null;
    }


    /**
     * 是否有外部存储
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取文件在SDCard中的完整路径
     */
    @Nullable
    public static String getSdcardPath() {
        if (hasExternalStorage()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    @NonNull
    private static String getAbsolutePath(@NonNull String path, @NonNull String filename) {
        if (!filename.startsWith(path)) {
            if (filename.startsWith(FILE_SEPARATOR)) {
                path += filename;
            } else {
                path += FILE_SEPARATOR + filename;
            }
        }
        return path;
    }

    /**
     * 获取文件在SDCard中的完整路径: 如果本来就是完整路径则直接返回,
     * 否则在filename前加上SDCard路径
     *
     * @param filename 文件名(可包含路径)
     */
    @Nullable
    public static String getSdcardPath(@NonNull String filename) {
        String path = getSdcardPath();
        return path == null ? null : getAbsolutePath(path, filename);
    }

    /**
     * 获取文件在SDCard中的完整路径: 如果本来就是完整路径则直接返回,
     * 否则在filename前加上SDCard路径
     *
     * @param filename 文件名(可包含路径)
     */
    @Nullable
    public static File getSdcardFile(@NonNull String filename) {
        String path = getSdcardPath(filename);
        return path == null ? null : new File(path);
    }

    public static File getFileInData(Context context, String filename) {
        return new File(getAbsolutePath(context.getFilesDir().toString(), filename));
    }

    /**
     * 获取文件的Uri
     *
     * @param path 文件的路径
     * @return Uri
     */
    public static Uri getUri(String path) {
        return Uri.fromFile(new File(path));
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 必须是完整路径
     * @return boolean
     */
    public static boolean isExists(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }

    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    public static boolean mkdirs(String path) {
        if (path == null) {
            return false;
        }
        File dir = new File(path);
        return dir.isDirectory() && (dir.exists() || dir.mkdirs());
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        return file.isFile() && (file.exists() || file.createNewFile()) ? file : null;
    }

    /**
     * 删除文件或文件夹
     *
     * @param path 文件或文件夹的路径
     * @return 是否删除
     */
    public static boolean delete(String path) {
        return !TextUtils.isEmpty(path) && delete(new File(path));
    }

    /**
     * 删除文件或文件夹
     *
     * @param file File
     * @return 是否删除
     */
    public static boolean delete(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    boolean success = delete(f);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 获取总缓存大小（内部缓存+外部缓存）
     *
     * @param context Context
     * @return 字节数
     */
    public static long getTotalCacheSize(Context context) {
        long cacheSize = getFileSize(context.getCacheDir());
        if (hasExternalStorage()) {
            File dir = context.getExternalCacheDir();
            if (dir != null) {
                cacheSize += getFileSize(dir);
            }
        }
        return cacheSize;
    }

    /**
     * 获取文件或文件夹的大小，并格式化
     *
     * @param file File
     * @return 字节数格式化后的文本
     */
    public static String getFileSize(@NonNull Context context, @NonNull File file) {
        return formatFileSize(context, getFileSize(file));
    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param file File
     * @return 字节数
     */
    public static long getFileSize(@NonNull File file) {
        if (file.isFile()) {
            return file.length();
        }
        long size = 0;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File subFile : files) {
                // 如果下面还有文件
                if (subFile.isDirectory()) {
                    size += getFileSize(subFile);
                } else {
                    size += subFile.length();
                }
            }
        }
        return size;
    }

    /**
     * 格式化文件大小
     *
     * @param context   Context
     * @param sizeBytes 字节数
     */
    public static String formatFileSize(@NonNull Context context, long sizeBytes) {
        return Formatter.formatFileSize(context, sizeBytes);
    }

    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath 文件路径
     * @return file name from path, not include suffix
     * @see #getFileName(String)
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int extIndex = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int separatorIndex = filePath.lastIndexOf(File.separator);
        if (separatorIndex == -1) {
            return (extIndex == -1 ? filePath : filePath.substring(0, extIndex));
        }
        if (extIndex == -1) {
            return filePath.substring(separatorIndex + 1);
        }
        return (separatorIndex < extIndex ? filePath.substring(separatorIndex + 1, extIndex) : filePath.substring(separatorIndex + 1));
    }

    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath 文件路径
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int separatorIndex = filePath.lastIndexOf(File.separator);
        return (separatorIndex == -1) ? filePath : filePath.substring(separatorIndex + 1);
    }

    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath 文件路径
     * @return 文件夹路径
     */
    public static String getFolderName(String filePath) {

        if (StringUtil.isBlank(filePath)) {
            return filePath;
        }

        int separatorIndex = filePath.lastIndexOf(File.separator);
        return (separatorIndex == -1) ? "" : filePath.substring(0, separatorIndex);
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath 文件路径
     * @return 扩展名
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int extIndex = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        if (extIndex == -1) {
            return "";
        }
        int separatorIndex = filePath.lastIndexOf(File.separator);
        return (separatorIndex >= extIndex) ? "" : filePath.substring(extIndex + 1);
    }

    /**
     * 对文件数组按名称排序
     *
     * @param files 文件数组
     */
    public static void sortFiles(File[] files) {
        Arrays.sort(files, new Comparator<File>() {

            /**
             * return -1表示file1<file2，0 表示file1=file2，1表示file1>file2
             */
            @Override
            public int compare(File file1, File file2) {
                boolean isDirectory1 = file1.isDirectory();
                boolean isDirectory2 = file2.isDirectory();
                if (isDirectory1 && !isDirectory2)
                    return -1;
                else if (!isDirectory1 && isDirectory2)
                    return 1;
                else {
                    return file1.getName().compareTo(file2.getName());
                }
            }
        });
    }

}
