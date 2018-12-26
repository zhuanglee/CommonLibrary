package cn.lzh.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.File;

import cn.lzh.common.R;
import cn.lzh.utils.BitmapUtil;
import cn.lzh.utils.DeviceUtil;

/**
 * Created by lzh on 2018/12/25.<br/>
 */
public class WatermarkHelper {

    /**
     * 水印图片文件名的格式
     */
    private static final String WATERMARK_FILE_FORMAT = "watermark_%s.png";

    public static Drawable getWatermarkDrawable(@NonNull Context context) {
        return getWatermarkDrawable(context, context.getClass().getSimpleName());
    }

    public static Drawable getWatermarkDrawable(@NonNull Context context, @NonNull String text) {
        context = context.getApplicationContext();
        Drawable drawable = null;
        File file = new File(context.getCacheDir(), String.format(WATERMARK_FILE_FORMAT, text));
        if (file.exists()) {
            drawable = Drawable.createFromPath(file.getAbsolutePath());
        }
        if (drawable == null) {
            // 没有获取到水印图片,则自己画
            Resources resources = context.getResources();
            int textColor = resources.getColor(R.color.colorWatermark);
            int textSize = resources.getDimensionPixelSize(R.dimen.text_size_normal);
            int toolbarHeight = resources.getDimensionPixelSize(R.dimen.toolbar_height);
            WatermarkUtil.Watermark watermark = new WatermarkUtil.Watermark();
            watermark.text = text;
            watermark.textColor = textColor;
            watermark.textSize = textSize;
            watermark.width = DeviceUtil.getScreenWidth(context);
            watermark.height = DeviceUtil.getScreenHeight(context);
            watermark.offsetY = DeviceUtil.getStatusBarHeight(context) + toolbarHeight;
            Bitmap bitmap = WatermarkUtil.getWatermarkBitmap(watermark, Bitmap.Config.ARGB_8888);
            BitmapUtil.saveImage(bitmap, file, Bitmap.CompressFormat.JPEG, 80);
            drawable = new BitmapDrawable(bitmap);
        }
        return drawable;
    }

}
