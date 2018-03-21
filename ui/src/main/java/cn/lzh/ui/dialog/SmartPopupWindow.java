package cn.lzh.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import cn.lzh.ui.R;


/**
 * 自定义的弹出框：显示在屏幕的中心，其他区域为透明色
 *
 * @author lzh
 */
public class SmartPopupWindow extends PopupWindow implements
        PopupWindow.OnDismissListener {
    private static final int COLOR_TRANSPARENT = 0x00000000;
    protected Activity mActivity;
    protected View mContentView;
    private Animation mAlphaIn;

    public SmartPopupWindow(Context context) {
        super(context);
        mActivity = (Activity) context;
    }

    /**
     * 布局文件必须设置固定尺寸
     *
     * @param activity Activity
     * @param layoutId 布局ID
     */
    public SmartPopupWindow(Activity activity, int layoutId) {
        this(activity);
        mContentView = View.inflate(activity, layoutId, null);
        setContentView(mContentView);
        mContentView.measure(0, 0);
        setWidth(mContentView.getMeasuredWidth());
        setHeight(mContentView.getMeasuredHeight());
        // 必须为PopupWindow设置一个背景，点击其他区域才能让PopupWindow消失
        setBackgroundDrawable(new ColorDrawable(COLOR_TRANSPARENT));
        setFocusable(true);//不设置点其他位置时，其他位置直接获得焦点
        setOutsideTouchable(true);
        setOnDismissListener(this);
        mAlphaIn = AnimationUtils.loadAnimation(activity, R.anim.alpha_in);
    }

    public SmartPopupWindow(Activity activity, int layoutId, int width,
                            int height) {
        this(activity, layoutId);
        setWidth(width);
        setHeight(height);
    }

    protected final <T extends View> T findViewById(int id){
        return mContentView.findViewById(id);
    }

    public void addDimBehindFlag(){
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void clearDimBehindFlag(){
        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 显示在屏幕的中心(Activity中有ScrollView时无效)
     */
    public void showScreenCenter() {
        mContentView.startAnimation(mAlphaIn);
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//        addDimBehindFlag();
        setWindowAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 背景透明度(0.0-1.0)
     */
    public void setWindowAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
//        clearDimBehindFlag();
        setWindowAlpha(1f);
    }

}