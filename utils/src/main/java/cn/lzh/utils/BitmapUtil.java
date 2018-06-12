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
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Last modify on 2017-10-25<br/>
 * 图片处理工具类
 */
public class BitmapUtil {
	private static final int UNCONSTRAINED = -1;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	private BitmapUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 将Bitmap转换成指定大小
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	@NonNull
	public static Bitmap createScaledBitmap(@NonNull Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * 读取旋转角度
	 *
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(@NonNull String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				case ExifInterface.ORIENTATION_NORMAL:

					degree = 0;
					break;
				case ExifInterface.ORIENTATION_UNDEFINED:

					degree = 0;
					break;
				case -1:

					degree = 0;
					break;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		return degree;
	}

	/**
	 * 旋转
	 *
	 * @param src 原图（旋转后会被回收）
	 * @param orientationDegree 旋转角度
	 * @return
	 */
	@NonNull
	public static Bitmap adjustPhotoRotation(@NonNull Bitmap src,
											 final int orientationDegree) {
		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) src.getWidth() / 2,
				(float) src.getHeight() / 2);
		float targetX, targetY;
		if (orientationDegree == 90) {
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
				Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(src, m, paint);
		src.recycle();
		return bmp;
	}

	/**
	 * 图片去色,返回灰度图片
	 *
	 * @param src 原图
	 * @return 去色后的图片
	 */
	@NonNull
	public static Bitmap toGrayScale(@NonNull Bitmap src) {
		Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Bitmap.Config.RGB_565);
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
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	@NonNull
	public static Bitmap toRoundCorner(@NonNull Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 使圆角功能支持BitmapDrawable
	 *
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	@NonNull
	public static BitmapDrawable toRoundCorner(@NonNull BitmapDrawable bitmapDrawable,
											   int pixels) {
		return new BitmapDrawable(
				toRoundCorner(bitmapDrawable.getBitmap(), pixels));
	}

	/**
	 * 获取裁剪后的圆形图片
	 *
	 * @param bmp    原图
	 * @param radius 半径
	 */
	public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		Bitmap squareBitmap;
		if(bmpHeight == bmpWidth){
			squareBitmap = bmp;
		}else{
			int size = Math.min(bmpHeight, bmpWidth);
			int x = bmpHeight > bmpWidth ? 0 : (bmpWidth - bmpHeight) / 2;
			int y = bmpHeight > bmpWidth ? (bmpHeight - bmpWidth) / 2 : 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, size, size);
		}
		if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);
		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2,
				scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		bmp.recycle();
		squareBitmap.recycle();
		scaledSrcBmp.recycle();
		return output;
	}

	/**
	 * 获取添加水印后的图片
	 *
	 * @param src 原图
	 * @param watermark 水印图片
	 * @return
	 */
	@NonNull
	public static Bitmap getWatermarkBitmap(@NonNull Bitmap src, @NonNull Bitmap watermark) {
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(bitmap);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return bitmap;
	}

	/**
	 * 图片合成
	 *
	 * @return
	 */
	@Nullable
	public static Bitmap getFotoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createFotoMixBitmap(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	@Nullable
	private static Bitmap createFotoMixBitmap(Bitmap first, Bitmap second,
											  int direction) {
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
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	/**
	 * Drawable 转 Bitmap
	 *
	 * @param drawable
	 * @return
	 */
	@NonNull
	public static Bitmap drawableToBitmap(@NonNull Drawable drawable) {
		if(drawable instanceof BitmapDrawable){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			return bitmapDrawable.getBitmap();
		}else{
			return drawable2Bitmap(drawable);
		}
	}

	/**
	 * drawable 转换成bitmap
	 *
	 * @param drawable
	 * @return
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
	 * Bitmap 转 Drawable
	 *
	 * @param bitmap
	 * @return
	 */
	@NonNull
	public static Drawable bitmapToDrawable(@NonNull Bitmap bitmap) {
		return new BitmapDrawable(bitmap);
	}

	/**
	 * byte[] 转 bitmap
	 *
	 * @param b
	 * @return
	 */
	@Nullable
	public static Bitmap bytesToBitmap(@NonNull byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * bitmap 转 byte[]
	 *
	 * @param bmp
	 * @return
	 */
	@NonNull
	public static byte[] bitmapToBytes(@NonNull Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 获取资源图片
	 *
	 * @param context
	 * @param resId
	 *            图片资源ID
	 * @return
	 * @deprecated 使用开源库性能更佳，如：Glide
	 */
	@Deprecated
	@NonNull
	public static Bitmap getBitmap(Context context, int resId) {
		return getBitmap(context, resId, 0, 0);
	}

	/**
	 * 获取资源图片
	 *
	 * @param context
	 * @param resId
	 *            图片资源ID
	 * @param width
	 * @param height
	 * @return
	 * @deprecated 使用开源库性能更佳，如：Glide
	 */
	@Deprecated
	@NonNull
	public static Bitmap getBitmap(Context context, int resId, int width,
								   int height) {
		Options opts = new Options();
		if (width > 0 && height > 0) {
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(context.getResources(), resId, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, width * height);
			opts.inJustDecodeBounds = false;
		}
		return BitmapFactory
				.decodeResource(context.getResources(), resId, opts);
	}

	/**
	 * 获取本地图片
	 *
	 * @param file
	 *            图片文件
	 * @return
	 * @deprecated 使用开源库性能更佳，如：Glide
	 */
	@Deprecated
	@Nullable
	public static Bitmap getBitmap(File file) {
		return getBitmap(file, 0, 0);
	}

	/**
	 * 获取本地图片
	 *
	 * @param file
	 *            图片文件
	 * @param width
	 * @param height
	 * @return
	 * @deprecated 使用开源库性能更佳，如：Glide
	 */
	@Deprecated
	@Nullable
	public static Bitmap getBitmap(File file, int width,
								   int height) {
		Options opts = new Options();
		if (width > 0 && height > 0) {
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.toString(), opts);
			opts.inSampleSize = computeSampleSize(opts, -1, width * height);
			opts.inJustDecodeBounds = false;
		}
		return BitmapFactory.decodeFile(file.toString(), opts);
	}

	/**
	 * 加载本地图片
	 * @param context
	 * @param localUri
	 * @return
	 * @deprecated 使用开源库性能更佳，如：Glide
	 */
	@Deprecated
	@Nullable
	public static Bitmap getBitmap(Context context, Uri localUri) {
		if (context == null || localUri == null)
			return null;
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(localUri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * {@link android.media.ThumbnailUtils}
	 * Compute the sample size as a function of minSideLength and
	 * maxNumOfPixels. minSideLength is used to specify that minimal width or
	 * height of a bitmap. maxNumOfPixels is used to specify the maximal size in
	 * pixels that is tolerable in terms of memory usage.
	 *
	 * The function returns a sample size based on the constraints. Both size
	 * and minSideLength can be passed in as IImage.UNCONSTRAINED, which
	 * indicates no care of the corresponding constraint. The functions prefers
	 * returning a sample size that generates a smaller bitmap, unless
	 * minSideLength = IImage.UNCONSTRAINED.
	 *
	 * Also, the function rounds up the sample size to a power of 2 or multiple
	 * of 8 because BitmapFactory only honors sample size this way. For example,
	 * BitmapFactory downsamples an image by 2 even though the request is 3. So
	 * we round up the sample size to avoid OOM.
	 */
	private static int computeSampleSize(Options options,
										 int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * {@link android.media.ThumbnailUtils}
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	private static int computeInitialSampleSize(Options options,
												int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength),
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 保存图片
	 *
	 * @param bitmap
	 * @param file
	 * @param format
	 *            ：JPEG|PNG
	 * @return
	 */
	public static boolean saveImage(@NonNull Bitmap bitmap, @NonNull File file,
									@NonNull CompressFormat format) {
		boolean isSuccess = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			if (bitmap.compress(format, 100, fos)) {
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
	 *
	 * @param bitmap
	 * @param filePath
	 * @param format
	 *            ：JPEG|PNG
	 * @return
	 */
	public static boolean saveImage(@NonNull Bitmap bitmap, @NonNull String filePath,
									@NonNull CompressFormat format) {
		return saveImage(bitmap, new File(filePath), format);
	}

	/**
	 * 旋转数据
	 *
	 * @param dst
	 *            目标数据
	 * @param src
	 *            源数据
	 * @param srcWidth
	 *            源数据宽
	 * @param srcHeight
	 *            源数据高
	 */
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
	 * @param data
	 *            源数据
	 * @param imageWidth
	 *            源数据宽
	 * @param imageHeight
	 *            源数据高
	 * @return 目标数据
	 */
	public static byte[] YUV42RotateDegree90(byte[] data, int imageWidth, int imageHeight)
	{
		byte [] yuv = new byte[imageWidth*imageHeight*3/2];

		// Y
		int i = 0;
		for(int x = 0;x < imageWidth;x++)
		{
			for(int y = imageHeight-1;y >= 0;y--)
			{
				yuv[i] = data[y*imageWidth+x];
				i++;
			}
		}

		// U and V
		i = imageWidth*imageHeight*3/2-1;
		int pos = imageWidth*imageHeight;
		for(int x = imageWidth-1;x > 0;x=x-2)
		{

			for(int y = 0;y < imageHeight/2;y++)
			{
				yuv[i] = data[pos+(y*imageWidth)+x];
				i--;
				yuv[i] = data[pos+(y*imageWidth)+(x-1)];
				i--;
			}
		}
		return yuv;
	}

}
