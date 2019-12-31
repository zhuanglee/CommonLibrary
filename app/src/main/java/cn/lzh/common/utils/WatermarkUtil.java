package cn.lzh.common.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;

/**
 * 水印图片相关操作
 *
 * @author lzh
 * @see #getWatermarkBitmap(Watermark, Config)
 * @see #getWatermarkBitmap(Bitmap, Watermark)
 */
public class WatermarkUtil {
    private WatermarkUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取平铺方式的水印图片
     *
     * @param source 水印图片
     * @param watermark Watermark
     * @return Bitmap
     */
    public static Bitmap getWatermarkBitmap(Bitmap source, Watermark watermark) {
        final Config config = source.getConfig();
        Bitmap bitmap = Bitmap.createBitmap(watermark.width, watermark.height,
                config == null ? Config.ARGB_8888 : config);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(watermark.background);
        // 绘制水印图片
        for (int x = 0; x < watermark.width; x += source.getWidth()) {
            for (int y = watermark.offsetY; y < watermark.height; y += source.getHeight()) {
                canvas.drawBitmap(source, x, y, null);
            }
        }
        return bitmap;
    }

    /**
     * 获取平铺方式的水印图片
     *
     * @param watermark Watermark
     * @return Bitmap
     */
    public static Bitmap getWatermarkBitmap(Watermark watermark, Config config) {
        Bitmap watermarkBitmap = Bitmap.createBitmap(watermark.width, watermark.height,
                config);
        Canvas canvas = new Canvas(watermarkBitmap);
        canvas.drawColor(watermark.background);
        // 绘制水印图片
        int subWatermarkSize = watermark.width >> 1;// 每行显示2个
        Watermark subWatermark = new Watermark();
        subWatermark.width = subWatermark.height = watermark.width >> 1;
        subWatermark.text = watermark.text;
        subWatermark.textColor = watermark.textColor;
        subWatermark.textSize = watermark.textSize;
        subWatermark.textDegrees = watermark.textDegrees;
        Bitmap subWatermarkBitmap = getWatermarkBitmapCell(subWatermark, config);
        for (int x = 0; x < watermark.width; x += subWatermarkSize) {
            for (int y = watermark.offsetY; y < watermark.height; y += subWatermarkSize) {
                canvas.drawBitmap(subWatermarkBitmap, x, y, null);
            }
        }
        if(!subWatermarkBitmap.isRecycled()){
            subWatermarkBitmap.recycle();
        }
        return watermarkBitmap;
    }

    /**
     * 创建指定宽高的Bitmap，旋转画布绘制水印
     *
     * @param watermark Watermark
     * @return 有旋转角度的水印图片
     */
    private static Bitmap getWatermarkBitmapCell(Watermark watermark, Config config) {
        int textSize = Math.min(watermark.width, watermark.height) / watermark.text.length();// 自动计算文字大小
        if (watermark.textSize > 0 && watermark.textSize < textSize) {
            textSize = watermark.textSize;// 如果已经设置了文字大小，并且小于自动计算的文字大小，则采用
        }
        Bitmap bitmap = Bitmap.createBitmap(watermark.width, watermark.height, config);
        Canvas canvas = new Canvas(bitmap);
        canvas.rotate(watermark.textDegrees, watermark.width >> 1, watermark.height >> 1);// 中心旋转
        // 绘制水印文字
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(watermark.textColor);
        paint.setTextAlign(Align.CENTER);
        FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int x = 0, y = 0;
        Rect targetRect = new Rect(x, y, x + watermark.width, y + watermark.height);
        int baseline = (targetRect.bottom + targetRect.top
                - fontMetricsInt.bottom - fontMetricsInt.top) >> 1;
        canvas.drawText(watermark.text, targetRect.centerX(), baseline,
                paint);
        return bitmap;
    }

    /**
     * 水印图片的属性
     *
     * @author lzh
     */
    public static class Watermark {
        /**
         * 宽度
         */
        public int width;
        /**
         * 高度
         */
        public int height;
        /**
         * 图片的背景色
         */
        public int background = Color.WHITE;
        /**
         * 水印文字
         */
        public String text = "Default";
        /**
         * 水印颜色
         */
        public int textColor = 0x33333333;
        /**
         * 字体大小
         */
        public int textSize;
        /**
         * 文字的旋转角度
         */
        public int textDegrees = -45;
        /**
         * 绘制水印时,纵坐标的偏移量（通常设为：状态栏高度+标题栏高度）
         */
        public int offsetY;

    }
}
