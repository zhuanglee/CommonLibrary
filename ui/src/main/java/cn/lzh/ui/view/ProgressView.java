package cn.lzh.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author naiyu(http://snailws.com)
 * @version 1.0
 * 
 * @author lzh
 * @version 1.1
 */
@Deprecated
public class ProgressView extends View {

	// 画实心圆的画笔
	private Paint mCirclePaint;
	// 画圆环的画笔
	private Paint mRingPaint;
	// 画字体的画笔
	private Paint mTextPaint;
	// 背景颜色
	private int mBgColor;
	// 进度值颜色
	private int mProgressColor;
	/**
	 * 字体颜色
	 */
	private int mTextColor;
	/**
	 * 绘制的类型：0圆环，1扇形
	 */
	private int mDrawType;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
	// 字的长度
	private float mTxtWidth;
	// 字的高度
	private float mTxtHeight;
	// 总进度
	private int mTotalProgress = 100;
	// 当前进度
	private int mProgress;
	private RectF oval;

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
//		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
//				R.styleable.ProgressView, 0, 0);
//		mDrawType = typeArray.getInteger(R.styleable.ProgressView_drawType, 0);
//		mRadius = typeArray.getDimension(R.styleable.ProgressView_radius, 80);
//		mStrokeWidth = typeArray.getDimension(R.styleable.ProgressView_strokeWidth, 10);
//		mBgColor = typeArray.getColor(R.styleable.ProgressView_bgColor, 0xFFFFFFFF);
//		mProgressColor = typeArray.getColor(R.styleable.ProgressView_progressColor, 0xFFFFFFFF);
//		mTextColor = typeArray.getColor(R.styleable.ProgressView_textColor, 0xFFFFFFFF);
//		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mBgColor);
		mCirclePaint.setStyle(Paint.Style.FILL);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mProgressColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);

		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setStyle(Paint.Style.FILL);
//		mTextPaint.setARGB(255, 255, 255, 255);
		mTextPaint.setTextSize(mRadius / 2);
		
		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		switch (mDrawType) {
		case 1:
			mCirclePaint.setColor(mBgColor);
			canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
			mCirclePaint.setColor(mProgressColor);//扇形颜色
			if (mProgress > 0 ) {
				oval.left = (mXCenter - mRingRadius);
				oval.top = (mYCenter - mRingRadius);
				oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
				oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
				canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, true, mCirclePaint); //
//				canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
				String txt = mProgress + "%";
				mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
				canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
			}
			break;
		default:
			canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
			if (mProgress > 0 ) {
				oval.left = (mXCenter - mRingRadius);
				oval.top = (mYCenter - mRingRadius);
				oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
				oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
				canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, false, mRingPaint); //
//				canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
				String txt = mProgress + "%";
				mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
				canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
			}
			break;
		}
	}
	
	public void setProgress(int progress) {
		mProgress = progress;
//		invalidate();
		postInvalidate();
	}

	public int getProgress() {
		return mProgress;
	}

	
}
