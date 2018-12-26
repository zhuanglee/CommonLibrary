package cn.lzh.utils;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Created by lzh on 2018/12/26.<br/>
 */
public final class Objects {
    private Objects() {
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

}
