package cn.lzh.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapUtil {
	private static final int UNCONSTRAINED = -1;

	/**
	 * 将Bitmap转换成指定大小
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createScaledBitmap(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * 获取资源图片
	 * 
	 * @param context
	 * @param resId
	 *            图片资源ID
	 * @return
	 */
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
	 */
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
	 * @param context
	 * @param file
	 *            图片文件
	 * @return
	 */
	public static Bitmap getBitmap(Context context, File file) {
		return getBitmap(context, file, 0, 0);
	}

	/**
	 * 获取本地图片
	 * 
	 * @param context
	 * @param file
	 *            图片文件
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(Context context, File file, int width,
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
	 * 加载网络图片
	 * 
	 * @param context
	 * @param imgUrl
	 *            图片链接
	 * @return 可能为null
	 */
	public static Bitmap getBitmap(Context context, URL imgUrl) {
		return getBitmap(context, imgUrl, 0, 0);
	}

	/**
	 * 加载网络图片
	 * 
	 * @param context
	 * @param imgUrl
	 *            图片链接
	 * @param width
	 * @param height
	 * @return 可能为null
	 */
	public static Bitmap getBitmap(Context context, URL imgUrl, int width,
			int height) {
		byte[] data = getDataFromNet(imgUrl);
		if (data == null || data.length == 0) {
			return null;
		}
		Options opts = new Options();
		if (width > 0 && height > 0) {
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(data, 0, data.length, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, width * height);
			opts.inJustDecodeBounds = false;
		}
		return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	}

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @param filename
	 * @param format
	 *            ：JPEG|PNG
	 * @return
	 */
	public static boolean saveImage(Bitmap bitmap, File file,
			CompressFormat format) {
		boolean isSuccess = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			if (bitmap.compress(format, 100, fos)) {
				fos.flush();
			}
			isSuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
	 * Get请求，获得返回数据
	 * 
	 * @param urlStr
	 * @return
	 * @throws Exception
	 */
	private static byte[] getDataFromNet(URL url) {
		final int TIMEOUT_IN_MILLIONS = 5000;
		HttpURLConnection conn = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
			conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				baos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buf = new byte[128];

				while ((len = is.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				baos.flush();
				return baos.toByteArray();
			} else {
				throw new RuntimeException(" responseCode is not 200 ... ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
			}
			conn.disconnect();
		}
		return null;
	}

}
