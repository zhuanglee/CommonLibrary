package cn.lzh.utils;

import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * CollectionUtil
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-7-22
 */
public class CollectionUtil {

    /**
     * default join separator
     **/
    public static final CharSequence DEFAULT_JOIN_SEPARATOR = ",";

    private CollectionUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * is null or its size is 0
     * <p>
     * <pre>
     * isBlank(null)   =   true;
     * isBlank({})     =   true;
     * isBlank({1})    =   false;
     * </pre>
     *
     * @return if collection is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    /**
     * join collection to string, separator is {@link #DEFAULT_JOIN_SEPARATOR}
     * <p>
     * <pre>
     * join(null)      =   "";
     * join({})        =   "";
     * join({a,b})     =   "a,b";
     * </pre>
     *
     * @param collection Iterable
     * @return join collection to string, separator is {@link #DEFAULT_JOIN_SEPARATOR}. <br/>
     * if collection is empty, return ""
     */
    public static String join(Iterable collection) {
        return collection == null ? "" : TextUtils.join(DEFAULT_JOIN_SEPARATOR, collection);
    }


    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Class<?> cls, ArrayList<T> items) {
        if (items == null || items.size() == 0) {
            return (T[]) Array.newInstance(cls, 0);
        }
        return items.toArray((T[]) Array.newInstance(cls, items.size()));
    }

}