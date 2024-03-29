package cn.lzh.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ResourceUtil
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-26
 * @see #getResourceId(Context, String)
 */
public class ResourceUtil {

    private ResourceUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 根据资源id的名称获取资源的id
     *
     * @param context        Context
     * @param resourceIdName 资源名称
     */
    public static int getResourceId(Context context, String resourceIdName) {
        return context.getResources().getIdentifier(resourceIdName, null, null);
    }

}
