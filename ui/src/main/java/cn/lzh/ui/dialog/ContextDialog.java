package cn.lzh.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by lzh on 2017/8/4.
 * 上下文对话框
 */
public class ContextDialog extends AlertDialog {

    /**
     * 创建对话框
     * @param context Context
     * @param title String
     * @param msg String
     * @param cancelable boolean
     * @return ContextDialog
     */
    public static ContextDialog create(Context context, String title, String msg, boolean cancelable) {
        ContextDialog contextDialog = new ContextDialog(context);
        contextDialog.setTitle(title);
        contextDialog.setMessage(msg);
        contextDialog.setCancelable(cancelable);
        return contextDialog;
    }

    /**
     * 创建没有标题的对话框
     * @param context Context
     * @param msg String
     * @param cancelable boolean
     * @return ContextDialog
     */
    public static ContextDialog create(Context context, String msg, boolean cancelable) {
        return create(context, null, msg, cancelable);
    }

    public ContextDialog(@NonNull Context context) {
        super(context);
    }

    public ContextDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public ContextDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 设置否定按钮（默认为"取消"，点击后隐藏对话框）
     */
    public ContextDialog setNegativeButton(){
        return this.setNegativeButton(android.R.string.no, null);
    }

    /**
     * 设置否定按钮
     * @param text string id
     * @param listener OnClickListener
     */
    public ContextDialog setNegativeButton(@StringRes int text, OnClickListener listener){
        return this.setNegativeButton(getContext().getString(text), listener);
    }

    /**
     * 设置否定按钮
     * @param text CharSequence
     * @param listener OnClickListener
     */
    public ContextDialog setNegativeButton(CharSequence text, OnClickListener listener){
        if(listener == null){
            listener = (dialog, which) -> dialog.dismiss();
        }
        super.setButton(DialogInterface.BUTTON_NEGATIVE, text, listener);
        return this;
    }

    /**
     * 设置肯定按钮
     * @param text string id
     * @param listener OnClickListener
     */
    public ContextDialog setPositiveButton(@StringRes int text, OnClickListener listener){
        return this.setPositiveButton(getContext().getString(text), listener);
    }

    /**
     * 设置肯定按钮
     * @param text CharSequence
     * @param listener OnClickListener
     */
    public ContextDialog setPositiveButton(CharSequence text, OnClickListener listener){
        if(listener == null){
            listener = (dialog, which) -> dialog.dismiss();
        }
        super.setButton(DialogInterface.BUTTON_POSITIVE, text, listener);
        return this;
    }

}
