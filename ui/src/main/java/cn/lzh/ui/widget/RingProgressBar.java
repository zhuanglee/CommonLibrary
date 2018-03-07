package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import cn.lzh.ui.R;


/**
 * 可在中心显示文字的进度条，线程安全的View，可直接在线程中更新进度
 */
public class RingProgressBar extends View {

	public static final int STROKE = 0;

	public static final int FILL = 1;

	/**
	 * 画笔对象的引用
	 */
	private Paint mPaint;

	/**
	 * 背景圆环的宽度
	 */
	private float mBackgroundRingWidth;
	/**
	 * 进度圆环的宽度
	 */
	private float mProgressRingWidth;
	/**
	 * 圆环的颜色
	 */
	private int mBackgroundColor;
	/**
	 * 圆环进度的颜色
	 */
	private int mProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int mTextColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float mTextSize;

	/**
	 * 中间的文字
	 */
	private String mText;

	/**
	 * 是否显示中间的进度
	 */
	private boolean mTextVisibility;
	/**
	 * 最大进度
	 */
	private int mMax;
	/**
	 * 当前进度
	 */
	private int mProgress;
	/**
	 * 进度的风格，实心或者空心
	 */
	private int mProgressStyle;

	private int mProgressStartAngle;

	private int mCenter;

	private int mRadius;

	private RectF mRectF;

	public RingProgressBar(Context context) {
		this(context, null);
	}

	public RingProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RingProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义属性和默认值
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RingProgressBar);
		mMax = mTypedArray.getInteger(R.styleable.RingProgressBar_max, 100);
		mProgress = mTypedArray.getInteger(
				R.styleable.RingProgressBar_progress, 0);
		mProgressColor = mTypedArray.getColor(
				R.styleable.RingProgressBar_progressColor, Color.RED);
		mProgressRingWidth = mTypedArray.getDimension(
				R.styleable.RingProgressBar_progressRingWidth, 4);
		mProgressStartAngle = mTypedArray.getInteger(
				R.styleable.RingProgressBar_progressStartAngle, 0);
		mProgressStyle = mTypedArray.getInt(R.styleable.RingProgressBar_paintStyle,
				STROKE);
		mBackgroundColor = mTypedArray.getColor(
				R.styleable.RingProgressBar_backgroundColor, Color.BLACK);
		mBackgroundRingWidth = mTypedArray.getDimension(
				R.styleable.RingProgressBar_backgroundRingWidth, 6);
		mTextVisibility = mTypedArray.getBoolean(
				R.styleable.RingProgressBar_textVisibility, true);
		mTextColor = mTypedArray.getColor(
				R.styleable.RingProgressBar_centerTextColor, Color.GREEN);
		mTextSize = mTypedArray.getDimension(
				R.styleable.RingProgressBar_centerTextSize, 32);
		mText = mTypedArray.getString(R.styleable.RingProgressBar_centerText);
		mTypedArray.recycle();
		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mRectF = new RectF();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int minMeasureSpec = Math.min(widthMeasureSpec, heightMeasureSpec);
		int size = MeasureSpec.getSize(minMeasureSpec);
		mCenter = size / 2;
		mRadius = (int) (mCenter - mBackgroundRingWidth / 2);
		mRectF.left=mCenter - mRadius;
		mRectF.top=mCenter - mRadius;
		mRectF.right=mCenter + mRadius;
		mRectF.bottom= mCenter + mRadius;
		setMeasuredDimension(minMeasureSpec, minMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画最外层的大圆环
		 */
		mPaint.setColor(mBackgroundColor); // 设置圆环的颜色
		mPaint.setStyle(Paint.Style.STROKE); // 设置空心
		mPaint.setStrokeWidth(mBackgroundRingWidth); // 设置圆环的宽度
		canvas.drawCircle(mCenter, mCenter, mRadius, mPaint); // 画出圆环

		/**
		 * 画圆弧 ，画圆环的进度
		 */
		mPaint.setStrokeWidth(mProgressRingWidth); // 设置圆环的宽度
		mPaint.setColor(mProgressColor); // 设置进度的颜色
		switch (mProgressStyle) {
		// 设置进度是实心还是空心
		case STROKE:
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(mRectF, mProgressStartAngle, 360 * mProgress / mMax,
					false, mPaint); // 根据进度画圆弧
			break;
		case FILL:
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (mProgress != 0)
				canvas.drawArc(mRectF, mProgressStartAngle, 360 * mProgress
						/ mMax, true, mPaint); // 根据进度画圆弧
			break;
		}
		/**
		 * 画进度百分比
		 */
		if (mTextVisibility && mProgressStyle == STROKE) {
			mPaint.setStrokeWidth(0);
			mPaint.setColor(mTextColor);
			mPaint.setTextSize(mTextSize);
			// mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			if (TextUtils.isEmpty(mText)) {
				int percent = (int) (((float) mProgress / (float) mMax) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
				float textWidth = mPaint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
				if (percent != 0) {
					canvas.drawText(percent + "%", mCenter - textWidth / 2,
							mCenter + mTextSize / 2, mPaint); // 画出进度百分比
				}
			} else {
				float textWidth = mPaint.measureText(mText);
				canvas.drawText(mText, mCenter - textWidth / 2, mCenter
						+ mTextSize / 2, mPaint);
			}
		}
	}

	public synchronized int getMax() {
		return mMax;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.mMax = max;
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return mProgress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > mMax) {
			progress = mMax;
		}
		if (progress <= mMax) {
			this.mProgress = progress;
			postInvalidate();
		}

	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}

	public int getProgressColor() {
		return mProgressColor;
	}

	public void setProgressColor(int progressColor) {
		this.mProgressColor = progressColor;
	}

	public float getBackgroundRingWidth() {
		return mBackgroundRingWidth;
	}

	public void setBackgroundRingWidth(float backgroundRingWidth) {
		this.mBackgroundRingWidth = backgroundRingWidth;
	}

	public float getProgressRingWidth() {
		return mProgressRingWidth;
	}

	public void setProgressRingWidth(float progressRingWidth) {
		this.mProgressRingWidth = progressRingWidth;
	}

	public boolean isTextVisibility() {
		return mTextVisibility;
	}

	public void setTextVisibility(boolean textVisibility) {
		this.mTextVisibility = textVisibility;
	}

	public int getProgressStyle() {
		return mProgressStyle;
	}

	public void setProgressStyle(int progressStyle) {
		this.mProgressStyle = progressStyle;
	}

	public int getProgressStartAngle() {
		return mProgressStartAngle;
	}

	public void setProgressStartAngle(int progressStartAngle) {
		this.mProgressStartAngle = progressStartAngle;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTextSize(float textSize) {
		this.mTextSize = textSize;
	}

	public String getText() {
		return mText;
	}

	/**
	 * 设置中心显示的文字
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.mText = text;
	}

}
