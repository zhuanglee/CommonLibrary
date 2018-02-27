package cn.lzh.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 封装SharedPreferences的相关操作
 *
 * @author lzh
 */
@Deprecated
public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";

    private SPUtil() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context Context
     * @param key String
     * @param object 存储的值
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context Context
     * @param key String
     * @param defaultObject 默认值
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        Object result = null;
        if (defaultObject instanceof String) {
            result = sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            result = sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            result = sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            result = sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            result = sp.getLong(key, (Long) defaultObject);
        }

        return result;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context Context
     * @param key String
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context Context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context context
     * @param key String
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context Context
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author lzh
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @Nullable
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                return null;
            }
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                }
            } catch (Exception e) {
                editor.commit();
            }
        }
    }

}
