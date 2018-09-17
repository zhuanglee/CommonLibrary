package cn.lzh.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 持有一个Toast实例，后一次的提示会覆盖前一次的提示
 */
public final class ToastUtil {
    private static Toast mToast;
    private static Context mAppContext;

    private ToastUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    public static void init(@NonNull Context context){
        mAppContext = context.getApplicationContext();
        mToast = new Toast(mAppContext);
    }

    public static void show(@NonNull String text, int duration) {
        if(mToast == null){
            throw new IllegalStateException("mToast is null, must be invoke init method");
        }
        mToast.setText(text);
        mToast.setDuration(duration);
        mToast.show();
    }

    public static void show(@StringRes int text, int duration) {
        if(mAppContext == null){
            throw new IllegalStateException("mAppContext is null, must be invoke init method");
        }
        show(mAppContext.getString(text), duration);
    }

    public static void show(@NonNull String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int text) {
        show(text, Toast.LENGTH_SHORT);
    }

}
