package cn.lzh.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;

import cn.lzh.ui.R;


/**
 * Created by lzh on 2019/12/31.<br/>
 * @see #setWindowAnimations(int)
 * @see #setGravityTop()
 * @see #setGravityCenter()
 * @see #setGravityBottom()
 */
public class SmartDialog extends AppCompatDialog {

    protected final View mContentView;

    /**
     * @param context Context
     * @param layoutId 布局资源id（布局文件最外层的布局属性无效）
     * @param cancelable 能否取消
     */
    public SmartDialog(@NonNull Context context, int layoutId, boolean cancelable) {
        super(context, R.style.CustomDialog);
        mContentView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(mContentView);
        setCancelable(cancelable);
    }

    public SmartDialog(Activity activity, int layoutId, boolean cancelable,
                            int width, int height) {
        this(activity, layoutId, cancelable);
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
    }

    public SmartDialog setWindowAnimations(@StyleRes int resId) {
        Window window = getWindow();
        if(window != null){
            window.setWindowAnimations(resId);
        }
        return this;
    }

    public SmartDialog setGravityTop() {
        Window window = getWindow();
        if(window != null){
            window.setGravity(Gravity.TOP);
        }
        return this;
    }

    public SmartDialog setGravityCenter() {
        Window window = getWindow();
        if(window != null){
            window.setGravity(Gravity.CENTER);
        }
        return this;
    }

    public SmartDialog setGravityBottom() {
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        mContentView.setLayoutParams(layoutParams);
        Window window = getWindow();
        if(window != null){
            window.setGravity(Gravity.BOTTOM);
        }
        return this;
    }

    /**
     * 创建对话框
     * @param ctx Activity Context
     * @param layoutId 布局资源id（布局文件最外层的布局属性无效）
     * @param cancelable 是否可以取消
     * @return BottomDialog
     */
    public static SmartDialog create(@NonNull Context ctx, int layoutId, boolean cancelable) {
        return new SmartDialog(ctx, layoutId, cancelable);
    }
}
