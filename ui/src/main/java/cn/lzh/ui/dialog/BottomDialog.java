package cn.lzh.ui.dialog;

import android.content.Context;
import androidx.annotation.NonNull;

import cn.lzh.ui.R;


/**
 * Created by lzh on 2017/10/20.<br/>
 * 底部显示的对话框
 * @see SmartDialog
 */
@Deprecated
public class BottomDialog extends SmartDialog {

    private BottomDialog(@NonNull Context context, int layoutId, boolean cancelable) {
        super(context, layoutId, cancelable);
        setWindowAnimations(R.style.AlphaAnimation);
        setGravityBottom();
    }

    /**
     * 创建底部显示的对话框
     * @param ctx Activity Context
     * @param layoutId 布局资源id（布局文件最外层的布局属性无效）
     * @param cancelable 是否可以取消
     * @return BottomDialog
     */
    public static BottomDialog create(@NonNull Context ctx, int layoutId, boolean cancelable) {
        return new BottomDialog(ctx, layoutId, cancelable);
    }
}
