package cn.lzh.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cn.lzh.ui.R;


/**
 * Created by lzh on 2017/10/20.<br/>
 * 底部显示的对话框
 */

public class BottomDialog extends AppCompatDialog {

    protected final View mContentView;

    public BottomDialog(@NonNull Context context, int layoutId, boolean cancelable) {
        super(context, R.style.BottomDialog);
        mContentView = LayoutInflater.from(context).inflate(layoutId, null);
        setContentView(mContentView);
        setCancelable(cancelable);
        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        mContentView.setLayoutParams(layoutParams);
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
        return mContentView;
    }

    public void addDimBehindFlag(){
        Window window = getWindow();
        if(window != null){
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    public void clearDimBehindFlag(){
        Window window = getWindow();
        if(window != null){
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
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
