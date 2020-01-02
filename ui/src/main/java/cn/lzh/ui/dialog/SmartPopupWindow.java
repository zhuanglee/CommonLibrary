package cn.lzh.ui.dialog;

import android.app.Activity;
import android.view.ViewGroup;


/**
 * 自定义的弹出框：显示在屏幕的中心，其他区域为透明色
 * @see SmartDialog
 * @author lzh
 */
@Deprecated
public class SmartPopupWindow extends SmartDialog{

    /**
     * @param activity Activity
     * @param layoutId 布局资源id（布局文件最外层的布局属性无效）
     */
    public SmartPopupWindow(Activity activity, int layoutId) {
        super(activity, layoutId, true);
    }

    public SmartPopupWindow(Activity activity, int layoutId,
                            int width, int height) {
        super(activity, layoutId, true, width, height);
    }

    /**
     * 显示在屏幕的中心(Activity中有ScrollView时无效)
     */
    public void showScreenCenter() {
        setGravityCenter();
        show();
    }
}