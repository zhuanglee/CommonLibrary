package cn.lzh.utils;

import android.os.Looper;

/**
 * 前提条件
 *
 * @author from open source
 * @see #checkMainThread()
 * @see #checkNotNull(Object, String)
 */
public final class Preconditions {
    private Preconditions() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    public static void checkNotNull(Object value, String message) {
        if (value == null) {
            throw new NullPointerException(message);
        }
    }

    public static void checkMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().getName());
        }
    }

}
