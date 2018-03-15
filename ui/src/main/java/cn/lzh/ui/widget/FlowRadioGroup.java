package cn.lzh.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import cn.lzh.ui.FlowLayoutHelper;
import cn.lzh.utils.DeviceUtil;

/**
 * 自定义流式单选按钮组
 * @author lzh
 *
 */
public class FlowRadioGroup extends RadioGroup {

	private FlowLayoutHelper mFlowLayoutHelper;

	public FlowRadioGroup(Context context) {
		this(context,null);
	}

	public FlowRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		mFlowLayoutHelper = new FlowLayoutHelper(this, new FlowLayoutHelper.OnMeasureCallback() {
			@Override
			public void onCallSuperMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				FlowRadioGroup.super.measure(widthMeasureSpec, heightMeasureSpec);
			}

			@Override
			public void setMeasuredDimension(int measuredWidth, int measuredHeight) {
				FlowRadioGroup.this.setMeasuredDimension(measuredWidth, measuredHeight);
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
