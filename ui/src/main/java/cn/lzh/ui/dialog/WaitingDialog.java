package cn.lzh.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.TextView;


import java.lang.reflect.Field;

import cn.lzh.ui.R;

/**
 * Created by lzh on 2017/8/3.
 * 等待对话框，可动态更新对话框的文本内容
 * @see #setMessage(CharSequence)
 */

public class WaitingDialog extends ProgressDialog {

    private TextView mContentView;

    private WaitingDialog(Context context) {
        this(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
    }

    private WaitingDialog(Context context, int theme) {
        super(context, theme);
        mContentView = getMessageViewByReflect();
    }

    /**
     * 通过反射获取MessageView
     */
    private TextView getMessageViewByReflect() {
        try {
            Field mMessageViewField = this.getClass().getDeclaredField("mMessageView");
            mMessageViewField.setAccessible(true);
            return (TextView) mMessageViewField.get(this);
        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setMessage(CharSequence message) {
        if(mContentView == null){
            super.setMessage(message);
        }else{
            mContentView.setText(message);
        }
    }

    /**
     * 显示进度对话框
     * @param context
     * @param msg 对话框的内容
     * @param cancelable
     */
    public static WaitingDialog show(Context context, String msg, boolean cancelable){
        WaitingDialog dialog = new WaitingDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }

    /**
     * 显示进度对话框
     * @param context
     * @param resId 对话框内容的资源Id
     * @param cancelable
     */
    public static WaitingDialog show(Context context, @StringRes int resId, boolean cancelable) {
        return show(context, context.getString(resId), cancelable);
    }

    /**
     * 显示进度对话框
     *
     * @param context
     * @param cancelable
     */
    public static WaitingDialog show(Context context, boolean cancelable) {
        return show(context, "", cancelable);
    }

    /**
     * 显示进度对话框，默认不可取消
     * @param context
     * @param msg 对话框的内容
     */
    public static WaitingDialog show(Context context, String msg){
        return show(context, msg, false);
    }

    /**
     * 显示进度对话框
     * @param context
     * @param resId 对话框内容的资源Id
     */
    public static WaitingDialog show(Context context, @StringRes int resId) {
        return show(context, context.getString(resId), false);
    }

    /**
     * 隐藏并释放
     */
    public static void dismissAndRelease(Dialog dialog) {
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

}
