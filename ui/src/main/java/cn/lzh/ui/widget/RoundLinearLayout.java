package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import cn.lzh.ui.R;


/**
 * 圆角矩形LinearLayout(可以设置抗锯齿，但是圆角处有背景色,因此使用时一定要设置一个shape背景，且圆角大小和布局圆角大小相等)
 * @博客地址 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/0420/4167.html
 * @源码地址 https://github.com/angcyo/RoundAngleLinearLayout
 * @author lzh
 */
public class RoundLinearLayout extends LinearLayout {

	private float mLayoutRadius;
	private float topLeftRadius;
	private float topRightRadius;
	private float bottomLeftRadius;
	private float bottomRightRadius;

	private RectF mRectF;
	private Paint roundPaint;
	private Paint saveLayerPaint;
	

	public RoundLinearLayout(Context context) {
		this(context, null);
	}

	public RoundLinearLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs,
					R.styleable.RoundLayout);
			mLayoutRadius = ta.getDimension(
					R.styleable.RoundLayout_layoutRadius, 15);
			topLeftRadius = ta.getDimension(
					R.styleable.RoundLayout_topLeftRadius, mLayoutRadius);
			topRightRadius = ta.getDimension(
					R.styleable.RoundLayout_topRightRadius, mLayoutRadius);
			bottomLeftRadius = ta.getDimension(
					R.styleable.RoundLayout_bottomLeftRadius, mLayoutRadius);
			bottomRightRadius = ta.getDimension(
					R.styleable.RoundLayout_bottomRightRadius,mLayoutRadius);
			ta.recycle();
		}
		init();
	}

	private void init() {
		setWillNotDraw(false);// 如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
		mRectF = new RectF();
		saveLayerPaint = new Paint();
		saveLayerPaint.setXfermode(null);
		roundPaint = new Paint();
		roundPaint.setColor(Color.WHITE);//圆角处有白色背景
		roundPaint.setAntiAlias(true);
		roundPaint.setStyle(Paint.Style.FILL);
		roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		mRectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.saveLayer(mRectF,saveLayerPaint, Canvas.ALL_SAVE_FLAG);
		super.dispatchDraw(canvas);
		drawTopLeft(canvas);// 用PorterDuffXfermode
		drawTopRight(canvas);// 用PorterDuffXfermode
		drawBottomLeft(canvas);// 用PorterDuffXfermode
		drawBottomRight(canvas);// 用PorterDuffXfermode
		canvas.restore();
	}
	
	private void drawTopLeft(Canvas canvas) {
		if (topLeftRadius > 0) {
			Path path = new Path();
			path.moveTo(0, topLeftRadius);
			path.lineTo(0, 0);
			path.lineTo(topLeftRadius, 0);
			path.arcTo(new RectF(0, 0, topLeftRadius * 2, topLeftRadius * 2),
					-90, -90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}

	private void drawTopRight(Canvas canvas) {
		if (topRightRadius > 0) {
			int width = getWidth();
			Path path = new Path();
			path.moveTo(width - topRightRadius, 0);
			path.lineTo(width, 0);
			path.lineTo(width, topRightRadius);
			path.arcTo(new RectF(width - 2 * topRightRadius, 0, width,
					topRightRadius * 2), 0, -90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}

	private void drawBottomLeft(Canvas canvas) {
		if (bottomLeftRadius > 0) {
			int height = getHeight();
			Path path = new Path();
			path.moveTo(0, height - bottomLeftRadius);
			path.lineTo(0, height);
			path.lineTo(bottomLeftRadius, height);
			path.arcTo(new RectF(0, height - 2 * bottomLeftRadius,
					bottomLeftRadius * 2, height), 90, 90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}

	private void drawBottomRight(Canvas canvas) {
		if (bottomRightRadius > 0) {
			int height = getHeight();
			int width = getWidth();
			Path path = new Path();
			path.moveTo(width - bottomRightRadius, height);
			path.lineTo(width, height);
			path.lineTo(width, height - bottomRightRadius);
			path.arcTo(new RectF(width - 2 * bottomRightRadius, height - 2
					* bottomRightRadius, width, height), 0, 90);
			path.close();
			canvas.drawPath(path, roundPaint);
		}
	}

	/**
	 * 设置布局的圆角尺寸，并重绘
	 * @param layoutRadius
	 */
	public void setLayoutRadius(float layoutRadius) {
		this.mLayoutRadius = layoutRadius;
		postInvalidate();
	}
}
