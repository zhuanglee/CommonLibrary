package cn.lzh.utils;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * 持有一个Toast实例，后一次的提示会覆盖前一次的提示
 * @see #init(Context)
 * @see #show(int)
 * @see #show(String)
 * @see #show(int, int)
 * @see #show(String, int)
 * @author from open source
 */
public final class ToastUtil {
    private static Toast mToast;
    private static Context mAppContext;

    private ToastUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    @UiThread
    public static void init(@NonNull Context context){
        mAppContext = context.getApplicationContext();
        mToast = new Toast(mAppContext);
        mToast.setView(LayoutInflater.from(context).inflate(R.layout.transient_notification, null));
    }

    public static void setView(View view){
        if(mToast == null){
            throw new IllegalStateException("mToast is null, must be invoke init method");
        }
        mToast.setView(view);
    }

    @UiThread
    public static void show(@NonNull String text, int duration) {
        if(mToast == null){
            throw new IllegalStateException("mToast is null, must be invoke init method");
        }
        mToast.setText(text);
        mToast.setDuration(duration);
        mToast.show();
    }

    @UiThread
    public static void show(@StringRes int text, int duration) {
        if(mAppContext == null){
            throw new IllegalStateException("mAppContext is null, must be invoke init method");
        }
        show(mAppContext.getString(text), duration);
    }

    @UiThread
    public static void show(@NonNull String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    @UiThread
    public static void show(@StringRes int text) {
        show(text, Toast.LENGTH_SHORT);
    }

}
