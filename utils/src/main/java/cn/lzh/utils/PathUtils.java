package cn.lzh.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * http://stackoverflow.com/a/27271131/4739220
 * @author open source
 */
public final class PathUtils {

    private static final String TAG = "PathUtils";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_FILE = "file";
    private static final String SHARED_PREFS = "shared_prefs";
    private static final String FILE_SEPARATOR = "/";
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    private PathUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 将文件转换为Uri
     *
     * @param context   Context
     * @param authority String
     * @param file      File
     * @return Uri
     */
    public static Uri fileToUri(@NonNull Context context, @NonNull String authority, @Nullable File file) {
        if (file == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0以上进行适配
            return FileProvider.getUriForFile(context, authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // 7.0以后 column '_data' does not exist
            return null;
        }
        final String data = MediaStore.Images.ImageColumns.DATA;
        try (Cursor cursor = context.getContentResolver().query(uri, new String[]{data}, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(data);
                return cursor.getString(column_index);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider

                final String id = DocumentsContract.getDocumentId(uri);
                if (!TextUtils.isEmpty(id)) {
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if (SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        } else if (SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 获取 SP 路径
     *
     * @param context Context
     * @return File
     */
    public static File getSharedPreferencesPath(@NonNull Context context) {
        return new File(context.getFilesDir().getParentFile(), SHARED_PREFS);
    }

    /**
     * 获取 SP 文件
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
     * 获取文件在SDCard中的完整路径
     */
    @Nullable
    public static String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
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
}
