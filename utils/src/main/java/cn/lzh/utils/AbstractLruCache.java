package cn.lzh.utils;

import androidx.collection.LruCache;

/**
 * 缓存
 * 
 * @author lzh
 */
public abstract class AbstractLruCache<T> {

	private final LruCache<String, T> mLruCache;

	protected AbstractLruCache() {
		// use 1/8 of available heap size
		mLruCache = new LruCache<String, T>((int) (Runtime.getRuntime()
				.maxMemory() / 8)) {
			@Override
			protected int sizeOf(String key, T value) {
				return getSizeOf(value);
			}
		};
	}

	protected abstract int getSizeOf(T value);

	/**
	 * 缓存
	 * @param key 键
	 * @param value 值
	 */
	public T put(String key, T value) {
		return mLruCache.put(key, value);
	}

	/**
	 * @param key 键
	 */
	public T get(String key) {
		return mLruCache.get(key);
	}
}
