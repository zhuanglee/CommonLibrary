package cn.lzh.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import cn.lzh.ui.R;


/**
 * Created by lzh on 2017/10/20.<br/>
 * 底部显示的对话框
 */

public class BottomDialog extends Dialog {

    private final View contentView;

    public BottomDialog(@NonNull Context context, int layoutId, boolean cancelable) {
        super(context, R.style.BottomDialog);
        contentView = LayoutInflater.from(context).inflate(layoutId, null);
        setCancelable(cancelable);
        setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        setGravityBottom();
    }

    private void setGravityBottom() {
        Window window = getWindow();
        if(window != null){
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.BottomDialog_Animation);
        }
    }

    public View getContentView() {
        return contentView;
    }

    /**
     * 创建底部显示的对话框
     * @param ctx Activity Context
     * @param layoutId 布局资源id
     * @param cancelable 是否可以取消
     * @return BottomDialog
     */
    public static BottomDialog create(@NonNull Context ctx, int layoutId, boolean cancelable) {
        return new BottomDialog(ctx, layoutId, cancelable);
    }
}
