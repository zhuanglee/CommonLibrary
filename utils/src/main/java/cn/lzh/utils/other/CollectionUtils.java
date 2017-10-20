package cn.lzh.utils.other;

import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

/**
 * CollectionUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-7-22
 */
public class CollectionUtils {

    /**
     * default join separator
     **/
    public static final CharSequence DEFAULT_JOIN_SEPARATOR = ",";

    private CollectionUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its size is 0
     * <p>
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     *
     * @param <V>
     * @param c
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
     * @param collection
     * @return join collection to string, separator is {@link #DEFAULT_JOIN_SEPARATOR}. if collection is empty, return
     * ""
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