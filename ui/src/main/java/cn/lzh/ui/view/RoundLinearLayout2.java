package cn.lzh.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.lzh.ui.R;


/**
 * 圆角LinearLayout(圆角处不会有背景色，但是无法设置抗锯齿)
 * @author http://blog.csdn.net/angcyo/article/details/51171299
 *
 */
public class RoundLinearLayout2 extends LinearLayout {
	private float mLayoutRadius;
	private Path mRoundPath;
	private RectF mRectF;

	public RoundLinearLayout2(Context context) {
		this(context, null);
	}

	public RoundLinearLayout2(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundLayout);
		mLayoutRadius = typedArray.getDimension(
				R.styleable.RoundLayout_layoutRadius, 15);
		typedArray.recycle();

		init();
	}

	private void init() {
		setWillNotDraw(false);// 如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
		mRoundPath = new Path();
		mRectF = new RectF();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (mLayoutRadius > 0f) {
			// 添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
			mRoundPath.addRoundRect(mRectF, mLayoutRadius, mLayoutRadius,
					Path.Direction.CW);
			canvas.clipPath(mRoundPath);
		}
		super.draw(canvas);
	}
	
	/**
	 * 设置布局的圆角尺寸
	 * @param layoutRadius
	 */
	public void setLayoutRadius(float layoutRadius) {
		this.mLayoutRadius = layoutRadius;
		postInvalidate();
	}

}