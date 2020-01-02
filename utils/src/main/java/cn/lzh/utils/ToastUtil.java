package cn.lzh.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Updated by lzh on 2019/12/31.<br/>
 * 持有一个 Toast 实例
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

    /**
     * 初始化（通常在 Application 创建时初始化）
     * @param context Context
     */
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

    /**
     * 设置 Toast 布局
     * @param view View
     */
    public static void setView(@NonNull View view){
        if(sToast == null){
            throw new IllegalStateException("must be invoke init method");
        }
        sHandler.post(()-> sToast.setView(view));
    }

    /**
     * 显示提示消息
     * @param text 消息内容
     * @param duration {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
     */
    public static void show(@Nullable String text, int duration) {
        if(sToast == null){
            throw new IllegalStateException("must be invoke init method");
        }
        if(TextUtils.isEmpty(text)) return;
        sHandler.post(()->{
            sToast.setText(text);
            sToast.setDuration(duration);
            sToast.show();
        });
    }

    /**
     * 显示提示消息
     * @param text 消息内容
     * @param duration {@link Toast#LENGTH_SHORT} or {@link Toast#LENGTH_LONG}
     */
    public static void show(@StringRes int text, int duration) {
        if(sAppContext == null){
            throw new IllegalStateException("must be invoke init method");
        }
        show(sAppContext.getString(text), duration);
    }

    /**
     * 显示短暂的提示消息
     * @param text 消息内容
     */
    public static void show(@Nullable String text) {
        show(text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短暂的提示消息
     * @param text 消息内容
     */
    public static void show(@StringRes int text) {
        show(text, Toast.LENGTH_SHORT);
    }

}
