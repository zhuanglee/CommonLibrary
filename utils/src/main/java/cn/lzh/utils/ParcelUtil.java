package cn.lzh.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * ParcelUtil
 *
 * @author open source
 * @see #readBoolean(Parcel)  readBoolean
 * @see #readHashMapStringAndString(Parcel) readHashMapStringAndString
 * @see #readHashMapStringKey(Parcel, ClassLoader) readHashMapStringKey
 * @see #readHashMap(Parcel, ClassLoader) readHashMap
 * @see #writeBoolean(boolean, Parcel) writeBoolean
 * @see #writeHashMapStringAndString(Map, Parcel) writeHashMapStringAndString
 * @see #writeHashMapStringKey(Map, Parcel, int) writeHashMapStringKey
 * @see #writeHashMap(Map, Parcel, int) writeHashMap
 */
public class ParcelUtil {

    private ParcelUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * read boolean
     * 
     * @param in Parcel
     * @return boolean
     */
    public static boolean readBoolean(Parcel in) {
        return in.readInt() == 1;
    }

    /**
     * write boolean
     * 
     * @param b boolean
     * @param out Parcel
     */
    public static void writeBoolean(boolean b, Parcel out) {
        out.writeInt(b ? 1 : 0);
    }

    /**
     * Read a HashMap from a Parcel, class of key and value are both String
     * 
     * @param in Parcel
     * @return Map
     */
    public static Map<String, String> readHashMapStringAndString(@NonNull Parcel in) {
        int size = in.readInt();
        if (size == -1) {
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        String key, value;
        for (int i = 0; i < size; i++) {
            key = in.readString();
            value = in.readString();
            map.put(key, value);
        }
        return map;
    }

    /**
     * Write a HashMap to a Parcel, class of key and value are both String
     * 
     * @param map Map
     * @param out Parcel
     */
    public static void writeHashMapStringAndString(Map<String, String> map, Parcel out) {
        if (map == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(map.size());
            for (Entry<String, String> entry : map.entrySet()) {
                out.writeString(entry.getKey());
                out.writeString(entry.getValue());
            }
        }
    }

    /**
     * Read a HashMap from a Parcel, class of key is String, class of Value can parcelable
     * 
     * @param <V> Parcelable
     * @param in Parcel
     * @param loader ClassLoader
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static <V extends Parcelable> Map<String, V> readHashMapStringKey(@NonNull Parcel in, ClassLoader loader) {
        int size = in.readInt();
        if (size == -1) {
            return null;
        }
        Map<String, V> map = new HashMap<String, V>();
        String key;
        V value;
        for (int i = 0; i < size; i++) {
            key = in.readString();
            value = in.readParcelable(loader);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Write a HashMap to a Parcel, class of key is String, class of Value can parcelable
     * 
     * @param map Map
     * @param out Parcel
     * @param flags int
     */
    public static <V extends Parcelable> void writeHashMapStringKey(Map<String, V> map, Parcel out, int flags) {
        if (map == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(map.size());
            for (Entry<String, V> entry : map.entrySet()) {
                out.writeString(entry.getKey());
                out.writeParcelable(entry.getValue(), flags);
            }
        }
    }

    /**
     * Read a HashMap from a Parcel, class of key and value can parcelable both
     * 
     * @param in Parcel
     * @param loader ClassLoader
     * @return Map
     */
    public static <K extends Parcelable, V extends Parcelable> Map<K, V> readHashMap(@NonNull Parcel in, ClassLoader loader) {
        int size = in.readInt();
        if (size == -1) {
            return null;
        }
        Map<K, V> map = new HashMap<K, V>();
        K key;
        V value;
        for (int i = 0; i < size; i++) {
            key = in.readParcelable(loader);
            value = in.readParcelable(loader);
            map.put(key, value);
        }
        return map;
    }

    /**
     * Write a HashMap to a Parcel, class of key and value can parcelable both
     * 
     * @param map Map
     * @param out Parcel
     * @param flags int
     */
    public static <K extends Parcelable, V extends Parcelable> void writeHashMap(Map<K, V> map, Parcel out, int flags) {
        if (map == null) {
            out.writeInt(-1);
        } else {
            out.writeInt(map.size());
            for (Entry<K, V> entry : map.entrySet()) {
                out.writeParcelable(entry.getKey(), flags);
                out.writeParcelable(entry.getValue(), flags);
            }
        }
    }
}
