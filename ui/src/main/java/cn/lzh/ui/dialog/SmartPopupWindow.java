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
 * @author lzh
 *
 */
public class SmartPopupWindow extends PopupWindow implements
		PopupWindow.OnDismissListener {
	private static final int COLOR_TRANSPARENT=0x00000000;
	private View mInflateView;
	private Animation alpha_in;
	private Activity mActivity;

	public SmartPopupWindow(Context context) {
		super(context);
	}

	/**
	 * 布局文件必须设置固定尺寸
	 * @param activity
	 * @param layoutId 布局ID
	 */
	public SmartPopupWindow(Activity activity, int layoutId) {
		super(activity);
		mActivity=activity;
		mInflateView = View.inflate(activity, layoutId, null);
		setContentView(mInflateView);
		mInflateView.measure(0, 0);
		setWidth(mInflateView.getMeasuredWidth());
		setHeight(mInflateView.getMeasuredHeight());
		// 必须为PopupWindow设置一个背景，点击其他区域才能让PopupWindow消失
		setBackgroundDrawable(new ColorDrawable(COLOR_TRANSPARENT));
		setFocusable(true);//不设置点其他位置时，其他位置直接获得焦点
		setOutsideTouchable(true);
		setOnDismissListener(this);
		alpha_in = AnimationUtils.loadAnimation(activity, R.anim.alpha_in);
	}
	
	public SmartPopupWindow(Activity activity, int layoutId, int width,
							int height) {
		super(activity);
		mActivity=activity;
		mInflateView = View.inflate(activity, layoutId, null);
		setContentView(mInflateView);
		setWidth(width);
		setHeight(height);
		// 必须为PopupWindow设置一个背景，点击其他区域才能让PopupWindow消失
		setBackgroundDrawable(new ColorDrawable(COLOR_TRANSPARENT));
		setFocusable(true);
		setOnDismissListener(this);
		alpha_in = AnimationUtils.loadAnimation(activity, R.anim.alpha_in);
	}

	/**
	 * 显示在屏幕的中心(Activity中有ScrollView时无效)
	 */
	public void showScreenCenter() {
		mInflateView.startAnimation(alpha_in);
		setWindowAlpha(0.5f);
		showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
		setWindowAlpha(1f);
	}

}