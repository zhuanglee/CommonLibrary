package cn.lzh.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 图片缓存
 * 
 * @author lzh
 *
 */
@Deprecated
public class BitmapCache {

	private static BitmapCache mInstance = null;

	public static synchronized BitmapCache getInstance() {
		if (mInstance == null) {
			mInstance = new BitmapCache();
		}
		return mInstance;

	}

	private LruCache<String, Bitmap> mLruCache = null;

	private BitmapCache() {
		// use 1/8 of available heap size
		mLruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime()
				.maxMemory() / 8)) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public Bitmap put(String key, Bitmap value) {
		return mLruCache.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	public Bitmap get(String key) {
		return mLruCache.get(key);
	}
}
