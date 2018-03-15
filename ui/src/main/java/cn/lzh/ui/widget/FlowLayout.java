package cn.lzh.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import cn.lzh.ui.FlowLayoutHelper;
import cn.lzh.utils.DeviceUtil;

/**
 * 自定义流式布局
 * @author lzh
 *
 */
public class FlowLayout extends ViewGroup {

	private FlowLayoutHelper mFlowLayoutHelper;

	public FlowLayout(Context context) {
		this(context,null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mFlowLayoutHelper = new FlowLayoutHelper(this, new FlowLayoutHelper.OnMeasureCallback() {
			@Override
			public void onCallSuperMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				FlowLayout.super.measure(widthMeasureSpec, heightMeasureSpec);
			}

			@Override
			public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
				FlowLayout.this.setMeasuredDimension(measuredWidth, measuredHeight);
			}
		});
		int space = DeviceUtil.dip2px(context, 8);
		setPadding(space, space, space, space);
	}
	
	public int getHorizontalSpace() {
		return mFlowLayoutHelper.getHorizontalSpace();
	}

	/**
	 * 设置水平方向控件的间距
	 * @param horizontalSpace 间距
	 */
	public void setHorizontalSpace(int horizontalSpace) {
		mFlowLayoutHelper.setHorizontalSpace(horizontalSpace);
	}

	public int getVerticalSpace() {
		return mFlowLayoutHelper.getVerticalSpace();
	}

	/**
	 * 设置垂直方向控件的间距
	 * @param verticalSpace 间距
	 */
	public void setVerticalSpace(int verticalSpace) {
		mFlowLayoutHelper.setVerticalSpace(verticalSpace);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mFlowLayoutHelper.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed,int l, int t, int r, int b) {
		mFlowLayoutHelper.onLayout(changed, l, t, r, b);
	}
	
}
