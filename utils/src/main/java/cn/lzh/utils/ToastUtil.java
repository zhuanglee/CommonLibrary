package cn.lzh.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 可以连续弹吐司，不用等上个吐司消失
 */
public final class ToastUtil {
    private static Toast mToast;
    private static Context mAppContext;

    private ToastUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    public static void init(@NonNull Context context){
        mAppContext = context.getApplicationContext();
    }

    @SuppressLint("ShowToast")
    public static void show(@NonNull String text, int duration) {
        if (mToast == null) {
            if(mAppContext == null){
                throw new IllegalStateException("mAppContext is null, must be invoke init method");
            }
            mToast = Toast.makeText(mAppContext, text, duration);
        }else{
            mToast.setText(text);
            mToast.setDuration(duration);
        }
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
