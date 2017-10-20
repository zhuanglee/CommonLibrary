package cn.lzh.ui.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

/**
 * last modify date is 2017-10-20<br/>
 * 可以连续弹吐司，不用等上个吐司消失
 */
public final class ToastUtil {
    private static final String TAG = "ToastUtil";
    private static Toast toast;
    private static Context mAppContext;
    private ToastUtil() {
    }

    public static void init(@NonNull Context context){
        mAppContext = context;
    }

    public static void show(@NonNull String text, int duration) {
        if (toast == null) {
            if(mAppContext == null){
                Log.e(TAG, "mAppContext is null, must be invoke init method");
            }
            toast = Toast.makeText(mAppContext, text, duration);
        }
        toast.setText(text);
        toast.show();
    }

    public static void show(@NonNull String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int resId) {
        if(mAppContext == null){
            Log.e(TAG, "mAppContext is null, must be invoke init method");
        }
        show(mAppContext.getString(resId));
    }

    /**
     * 显示长时间的提示
     * @param text
     */
    public static void showLong(@NonNull String text) {
        show(text, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时间的提示
     * @param text
     */
    public static void showLong(@StringRes int text) {
        if(mAppContext == null){
            Log.e(TAG, "mAppContext is null, must be invoke init method");
        }
        show(mAppContext.getString(text), Toast.LENGTH_LONG);
    }

}
