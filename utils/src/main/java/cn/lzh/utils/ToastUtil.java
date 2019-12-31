package cn.lzh.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * 持有一个Toast实例，后一次的提示会覆盖前一次的提示
 * @see #init(Context)
 * @see #setView(int)
 * @see #show(int)
 * @see #show(String)
 * @see #show(int, int)
 * @see #show(String, int)
 */
public final class ToastUtil {
    private static Toast sToast;
    private static Context sAppContext;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private ToastUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    @UiThread
    public static void init(@NonNull Context context){
        sAppContext = context.getApplicationContext();
        sToast = new Toast(sAppContext);
        setView(R.layout.transient_notification);
    }

    /**
     * 设置 Toast 布局
     * @param layoutId 布局id
     */
    public static void setView(@LayoutRes int layoutId){
        if(sAppContext == null || sToast == null){
            throw new IllegalStateException("must be invoke init method");
        }
        sHandler.post(()-> sToast.setView(LayoutInflater.from(sAppContext).inflate(layoutId, null)));
    }

    public static void setView(@NonNull View view){
        if(sToast == null){
            throw new IllegalStateException("must be invoke init method");
        }
        sHandler.post(()-> sToast.setView(view));
    }

    public static void show(@NonNull String text, int duration) {
        if(sToast == null){
            throw new IllegalStateException("must be invoke init method");
        }
        sHandler.post(()->{
            sToast.setText(text);
            sToast.setDuration(duration);
            sToast.show();
        });
    }

    public static void show(@StringRes int text, int duration) {
        if(sAppContext == null){
            throw new IllegalStateException("must be invoke init method");
        }
        show(sAppContext.getString(text), duration);
    }

    public static void show(@NonNull String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int text) {
        show(text, Toast.LENGTH_SHORT);
    }

}
