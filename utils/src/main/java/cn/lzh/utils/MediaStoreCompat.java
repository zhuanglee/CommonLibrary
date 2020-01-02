package cn.lzh.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 拍照工具类
 * @author open source
 * @see #hasCameraFeature(Context)
 * @see #createImageFile(Context)
 * @see #getCaptureIntent(Context)
 */
public class MediaStoreCompat {
    private CaptureStrategy mCaptureStrategy;
    private Uri mCurrentPhotoUri;
    private String mCurrentPhotoPath;

    public MediaStoreCompat(@NonNull CaptureStrategy strategy) {
        if (TextUtils.isEmpty(strategy.authority)) {
            throw new IllegalArgumentException("CaptureStrategy.authority is null");
        }
        this.mCaptureStrategy = strategy;
    }

    /**
     * Checks whether the device has a camera feature or not.
     *
     * @param context a context to check for camera feature.
     * @return true if the device has a camera feature. false otherwise.
     */
    public static boolean hasCameraFeature(@NonNull Context context) {
        PackageManager pm = context.getApplicationContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 获取拍照意图
     * @param context Context
     * @return Intent or null
     */
    @Nullable
    public Intent getCaptureIntent(@NonNull Context context) {
        context = context.getApplicationContext();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(context.getPackageManager()) != null) {
            File photoFile = createImageFile(context);
            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                mCurrentPhotoUri = PathUtils.fileToUri(context,
                        mCaptureStrategy.authority, photoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    List<ResolveInfo> resInfoList = context.getPackageManager()
                            .queryIntentActivities(captureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        context.grantUriPermission(packageName, mCurrentPhotoUri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                return captureIntent;
            }
        }
        return null;
    }

    /**
     * 创建图片文件
     * @param context Context
     * @return File
     */
    private File createImageFile(@NonNull Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir = getPictureDir(context);
        return storageDir == null ? null : new File(storageDir, imageFileName);
    }

    /**
     * 获取图片文件夹
     * @param context Context
     * @return File
     */
    @Nullable
    public File getPictureDir(@NonNull Context context) {
        File storageDir;
        if (mCaptureStrategy.isPublic) {
            storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        if (mCaptureStrategy.directory != null) {
            storageDir = new File(storageDir, mCaptureStrategy.directory);
        }
        if (storageDir != null && (storageDir.exists() || storageDir.mkdirs())) {
            return storageDir;
        }
        return null;
    }

    public Uri getCurrentPhotoUri() {
        return mCurrentPhotoUri;
    }

    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public static class CaptureStrategy {

        public final boolean isPublic;
        public final String authority;
        public final String directory;

        public CaptureStrategy(boolean isPublic, String authority) {
            this(isPublic, authority, null);
        }

        public CaptureStrategy(boolean isPublic, String authority, String directory) {
            this.isPublic = isPublic;
            this.authority = authority;
            this.directory = directory;
        }
    }
}
