package cn.lzh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.lzh.utils.io.CloseableUtils;

/**
 * 图片处理工具类
 *
 * @author from open source
 */
public class BitmapUtil {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int TOP = 3;
    public static final int BOTTOM = 4;

    @IntDef(value = {LEFT, RIGHT, TOP, BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {

    }

    private BitmapUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap Bitmap
     * @param width   目标宽度
     * @param height  目标高度
     * @return Bitmap
     */
    @NonNull
    public static Bitmap createScaledBitmap(@NonNull Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * 获取资源图片
     *
     * @param context Context
     * @param resId   图片资源ID
     * @param width   目标宽度
     * @param height  目标高度
     */
    @NonNull
    public static Bitmap getBitmap(@NonNull Context context, @DrawableRes int resId, int width, int height) {
        Options opts = new Options();
        if (width > 0 && height > 0) {
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), resId, opts);
            opts.inSampleSize = computeSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
        }
        return BitmapFactory
                .decodeResource(context.getResources(), resId, opts);
    }

    /**
     * 获取本地图片
     *
     * @param file   图片文件
     * @param width  目标宽度
     * @param height 目标高度
     * @return Bitmap
     */
    @Nullable
    public static Bitmap getBitmap(File file, int width, int height) {
        return getBitmap(file.getPath(), width, height);
    }

    /**
     * 获取本地图片
     *
     * @param path   图片文件
     * @param width  目标宽度
     * @param height 目标高度
     * @return Bitmap
     */
    @Nullable
    public static Bitmap getBitmap(String path, int width, int height) {
        Options opts = new Options();
        if (width > 0 && height > 0) {
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            opts.inSampleSize = computeSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
        }
        return BitmapFactory.decodeFile(path, opts);
    }

    /**
     * 加载本地图片
     *
     * @param context  Context
     * @param localUri Uri {@link android.content.ContentResolver#openInputStream(Uri)}
     * @return Bitmap
     */
    @Nullable
    public static Bitmap getBitmap(@NonNull Context context, @NonNull Uri localUri, int width, int height) {
        InputStream in = null;
        try {
            in = context.getContentResolver().openInputStream(localUri);
            if (width > 0 && height > 0) {
                Rect outPadding = new Rect(0, 0, width, height);
                Options opts = new Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(in, outPadding, opts);
                opts.inSampleSize = computeSampleSize(opts, width, height);
                opts.inJustDecodeBounds = false;
                return BitmapFactory.decodeStream(in, outPadding, opts);
            } else {
                return BitmapFactory.decodeStream(in);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtils.close(in);
        }
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Drawable 转 Bitmap
     *
     * @param drawable Drawable
     * @return instanceof
     */
    @NonNull
    public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        } else {
            return drawable2Bitmap(drawable);
        }
    }

    /**
     * drawable 转换成 bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    @NonNull
    private static Bitmap drawable2Bitmap(@NonNull Drawable drawable) {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * byte[] 转 bitmap
     *
     * @param bytes 字节数组
     * @return Bitmap
     */
    @Nullable
    public static Bitmap bytesToBitmap(@NonNull byte[] bytes) {
        return bytes.length == 0 ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * bitmap 转 byte[]
     *
     * @param bmp     Bitmap
     * @param format  CompressFormat
     * @param quality 质量
     * @return 字节数组
     */
    @NonNull
    public static byte[] bitmapToBytes(@NonNull Bitmap bmp, CompressFormat format, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            bmp.compress(format, quality, out);
            return out.toByteArray();
        } finally {
            CloseableUtils.close(out);
        }
    }

    /**
     * 保存图片
     * @param bitmap Bitmap
     * @param file File
     * @param format CompressFormat
     * @param quality 质量
     * @return boolean
     */
    public static boolean saveImage(@NonNull Bitmap bitmap, @NonNull File file, @NonNull CompressFormat format, int quality) {
        boolean isSuccess = false;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (bitmap.compress(format, quality, fos)) {
                fos.flush();
            }
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 保存图片
     * @param bitmap Bitmap
     * @param file File
     * @param format CompressFormat
     * @return boolean
     */
    @WorkerThread
    public static boolean saveImage(@NonNull Bitmap bitmap, @NonNull File file,
                                    @NonNull CompressFormat format) {
        return saveImage(bitmap, file, format, 100);
    }

    /**
     * 保存图片
     * @param bitmap Bitmap
     * @param filePath 文件路径
     * @param format CompressFormat
     * @return boolean
     */
    @WorkerThread
    public static boolean saveImage(@NonNull Bitmap bitmap, @NonNull String filePath,
                                    @NonNull CompressFormat format) {
        return saveImage(bitmap, new File(filePath), format);
    }

    /**
     * 获取旋转指定角度后的图片
     *
     * @param src    原图
     * @param degree 旋转角度
     * @param isRecycle 是否回收
     * @return Bitmap
     */
    @Deprecated
    @NonNull
    public static Bitmap getRotationBitmap(@NonNull Bitmap src, int degree, boolean isRecycle) {
        Matrix m = new Matrix();
        m.setRotate(degree, (float) src.getWidth() / 2,
                (float) src.getHeight() / 2);
        float targetX, targetY;
        if (degree == 90) {
            targetX = src.getHeight();
            targetY = 0;
        } else {
            targetX = src.getHeight();
            targetY = src.getWidth();
        }
        final float[] values = new float[9];
        m.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bmp = Bitmap.createBitmap(src.getHeight(), src.getWidth(),
                getConfig(src));
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(src, m, paint);
        if (isRecycle && !src.isRecycled()) src.recycle();
        return bmp;
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param src 原图
     * @return 去色后的图片
     */
    @Deprecated
    @NonNull
    public static Bitmap toGrayScale(@NonNull Bitmap src) {
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(src, 0, 0, paint);
        return bitmap;
    }

    /**
     * 把图片变成圆角
     *
     * @param src    原图
     * @param pixels 圆角的弧度
     * @return 圆角图片
     */
    @Deprecated
    @NonNull
    public static Bitmap toRoundCorner(@NonNull Bitmap src, float pixels) {
        Bitmap output = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), getConfig(src));
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, src.getWidth(), src.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);
        return output;
    }

    /**
     * 获取裁剪后的圆形图片
     *
     * @param src    原图
     * @param radius 半径
     * @param isRecycle 是否回收
     */
    @Deprecated
    public static Bitmap getCroppedRoundBitmap(Bitmap src, int radius, boolean isRecycle) {
        Bitmap.Config config = getConfig(src);
        int diameter = radius * 2;
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
        int bmpWidth = src.getWidth();
        int bmpHeight = src.getHeight();
        Bitmap squareBitmap;
        if (bmpHeight == bmpWidth) {
            squareBitmap = src;
        } else {
            int size = Math.min(bmpHeight, bmpWidth);
            int x = bmpHeight > bmpWidth ? 0 : (bmpWidth - bmpHeight) / 2;
            int y = bmpHeight > bmpWidth ? (bmpHeight - bmpWidth) / 2 : 0;
            squareBitmap = Bitmap.createBitmap(src, x, y, size, size);
            if (isRecycle && !src.isRecycled()) src.recycle();
        }
        Bitmap scaledSrcBmp;
        if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
            scaledSrcBmp = createScaledBitmap(squareBitmap, diameter, diameter);
            if (isRecycle && !squareBitmap.isRecycled()) squareBitmap.recycle();
        } else {
            scaledSrcBmp = squareBitmap;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
                scaledSrcBmp.getHeight(), config);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2,
                scaledSrcBmp.getWidth() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
        if (isRecycle && !scaledSrcBmp.isRecycled()) scaledSrcBmp.recycle();
        return output;
    }

    /**
     * 在原图的右下角添加水印图片
     *
     * @param src       原图
     * @param watermark 水印图片（会被回收）
     * @return Bitmap
     */
    @Deprecated
    @NonNull
    public static Bitmap getWatermarkBitmap(@NonNull Bitmap src, @NonNull Bitmap watermark) {
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        Canvas cv = new Canvas(src);
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);
        watermark.recycle();
        return src;
    }

    /**
     * 图片合成
     * @param direction Direction
     * @param bitmaps Bitmap[]
     * @return Bitmap
     */
    @Deprecated
    @Nullable
    public static Bitmap getMixBitmap(@Direction int direction, Bitmap... bitmaps) {
        if (bitmaps.length <= 0) {
            return null;
        }
        if (bitmaps.length == 1) {
            return bitmaps[0];
        }
        Bitmap newBitmap = bitmaps[0];
        for (int i = 1; i < bitmaps.length; i++) {
            newBitmap = createMixBitmap(newBitmap, bitmaps[i], direction);
        }
        return newBitmap;
    }

    @Nullable
    private static Bitmap createMixBitmap(Bitmap first, Bitmap second, @Direction int direction) {
        if (first == null) {
            return null;
        }
        if (second == null) {
            return first;
        }
        int fw = first.getWidth();
        int fh = first.getHeight();
        int sw = second.getWidth();
        int sh = second.getHeight();
        Bitmap newBitmap = null;
        Bitmap.Config config = getConfig(first);
        if (direction == LEFT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    config);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, sw, 0, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == RIGHT) {
            newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    config);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, fw, 0, null);
        } else if (direction == TOP) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    config);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, sh, null);
            canvas.drawBitmap(second, 0, 0, null);
        } else if (direction == BOTTOM) {
            newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
                    config);
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, fh, null);
        }
        return newBitmap;
    }

    /**
     * 旋转数据
     *
     * @param dst       目标数据
     * @param src       源数据
     * @param srcWidth  源数据宽
     * @param srcHeight 源数据高
     */
    @Deprecated
    public static void YV12RotateNegative90(byte[] dst, byte[] src, int srcWidth,
                                            int srcHeight) {
        int t = 0;
        int i, j;

        int wh = srcWidth * srcHeight;

        for (i = srcWidth - 1; i >= 0; i--) {
            for (j = srcHeight - 1; j >= 0; j--) {
                dst[t++] = src[j * srcWidth + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh + j * srcWidth / 2 + i];
            }
        }

        for (i = srcWidth / 2 - 1; i >= 0; i--) {
            for (j = srcHeight / 2 - 1; j >= 0; j--) {
                dst[t++] = src[wh * 5 / 4 + j * srcWidth / 2 + i];
            }
        }

    }


    /**
     * 旋转数据
     *
     * @param data        源数据
     * @param imageWidth  源数据宽
     * @param imageHeight 源数据高
     * @return 目标数据
     */
    @Deprecated
    public static byte[] YUV42RotateDegree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];

        // Y
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }

        // U and V
        i = imageWidth * imageHeight * 3 / 2 - 1;
        int pos = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {

            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[pos + (y * imageWidth) + x];
                i--;
                yuv[i] = data[pos + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    /**
     * 获取Config
     *
     * @param src 原图
     * @return 原图有Config，则直接返回，否则返回Bitmap.Config.ARGB_8888
     */
    @NonNull
    private static Bitmap.Config getConfig(@NonNull Bitmap src) {
        return getConfig(src, Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取Config
     *
     * @param src           原图
     * @param defaultConfig 默认值
     * @return 原图有Config，则直接返回，否则返回defaultConfig
     */
    @NonNull
    private static Bitmap.Config getConfig(@NonNull Bitmap src, Bitmap.Config defaultConfig) {
        Bitmap.Config config = src.getConfig();
        config = config == null ? defaultConfig : config;
        return config;
    }

}
