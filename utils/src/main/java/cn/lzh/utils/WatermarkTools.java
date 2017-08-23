package cn.lzh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.net.URL;

/**
 * 水印图片相关操作
 * @author lzh
 *
 */
public class WatermarkTools {
	/**
	 * 水印图片文件名的格式("%swatermark_%s.png")
	 */
	public static final String WATERMARK_FILE_FORMAT = "%swatermark_%s.png";
	
	private WatermarkTools() {
		throw new AssertionError("Cannot be instantiated");
	}

	/**
	 * 获取平铺方式的水印图片
	 * @param srcBitmap 水印图片
	 * @param watermark
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getWatermarkDrawable(Bitmap srcBitmap,Watermark watermark) {
		// 将Bitmap转换为Drawable
		return new BitmapDrawable(getWatermarkBitmap(srcBitmap, watermark));
	}

	/**
	 * 获取平铺方式的水印图片
	 * @param srcBitmap 水印图片
	 * @param watermark
	 * @return
	 */
	public static Bitmap getWatermarkBitmap(Bitmap srcBitmap,Watermark watermark) {
		try {
			Bitmap mBitmap = Bitmap.createBitmap(watermark.width, watermark.height,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(mBitmap);
			// 绘制背景
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(watermark.background);
			canvas.drawRect(
					new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight()),
					paint);
			// 绘制水印图片
			for(int x = 0; x< watermark.width; x+=srcBitmap.getWidth()){
				for(int y = watermark.offsetY; y< watermark.height; y+=srcBitmap.getHeight()){
					canvas.drawBitmap(srcBitmap, x, y, null);
				}
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			return mBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取平铺方式的水印图片
	 * @param watermark
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static BitmapDrawable getWatermarkDrawable(Watermark watermark) {
		// 将Bitmap转换为Drawable
		return new BitmapDrawable(getWatermarkBitmap(watermark));
	}

	/**
	 * 获取平铺方式的水印图片
	 * @param watermark
	 * @return
	 */
	public static Bitmap getWatermarkBitmap(Watermark watermark) {
		try {
			Bitmap watemarkBitmap = Bitmap.createBitmap(watermark.width, watermark.height,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(watemarkBitmap);
			// 绘制背景
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(watermark.background);
			canvas.drawRect(
					new RectF(0, 0, watemarkBitmap.getWidth(), watemarkBitmap.getHeight()),
					paint);
			// 绘制水印图片
			int subWatemarkSize= watermark.width>>1;//每行显示2个
			Watermark subWatermark = new Watermark();
			subWatermark.width= subWatermark.height= watermark.width>>1;
			subWatermark.text= watermark.text;
			subWatermark.textColor= watermark.textColor;
			subWatermark.textSize= watermark.textSize;
			subWatermark.textDegrees= watermark.textDegrees;
			Bitmap subWatemarkBitmap = getWatermarkBitmapCell(subWatermark);
			for(int x = 0; x< watermark.width; x+=subWatemarkSize){
				for(int y = watermark.offsetY; y< watermark.height; y+=subWatemarkSize){
					canvas.drawBitmap(subWatemarkBitmap, x, y, null);
				}
			}
			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
			return watemarkBitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建指定宽高的Bitmap,旋转画布绘制水印
	 * @param watermark
	 * @return 有旋转角度的水印图片
	 */
	private static Bitmap getWatermarkBitmapCell(Watermark watermark) {
		int textSize = Math.min(watermark.width, watermark.height) / watermark.text.length();// 自动计算文字大小
		if (watermark.textSize != 0&& watermark.textSize<textSize) {
			textSize = watermark.textSize;//如果已经设置了文字大小,并且不大于自动计算的文字大小,则采用
		}
		Bitmap bitmap = Bitmap.createBitmap(watermark.width, watermark.height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.rotate(watermark.textDegrees, watermark.width >> 1, watermark.height >> 1);// TODO 中心旋转
		// 绘制水印文字
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);	
		paint.setTextSize(textSize);
		paint.setColor(watermark.textColor);
		paint.setTextAlign(Align.CENTER);
		FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
		int x=0,y=0;
		Rect targetRect = new Rect(x, y, x + watermark.width, y + watermark.height);
		int baseline = (targetRect.bottom + targetRect.top
				- fontMetricsInt.bottom - fontMetricsInt.top) >> 1;
		canvas.drawText(watermark.text, targetRect.centerX(), baseline,
				paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bitmap;
	}

	/**
	 * 下载水印图片
	 * @param context
	 * @param url
	 * @param filename
	 */
	public static void downloadWatermarkImage(final Context context,final URL url,String filename) {
		if(context==null||url==null||TextUtils.isEmpty(filename)){
			throw new IllegalArgumentException("参数不可为空");
		}
		if(!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)){
			return;//无法存储
		}
		final File file=new File(filename);
		if(file.exists()){
			return;//已经存在不用再下载
		}else if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		new Thread(){
			public void run() {
				try {
					Bitmap bitmap = BitmapUtil.getBitmap(context, url);
					if(bitmap!=null){
						BitmapUtil.saveImage(bitmap, file, CompressFormat.PNG);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	/**
	 * 水印图片的属性
	 * @author lzh
	 *
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
		public String text = "测试水印";
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
		public int textDegrees=-45;
		/**
		 * 绘制水印时,纵坐标的偏移量(通常当界面有标题栏时,设为标题栏高度)
		 */
		public int offsetY;

	}
}
