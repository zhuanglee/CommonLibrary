package cn.lzh.ui.widget.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势锁控件
 * 
 * @author lzh
 *
 */
public class GestureLockView extends View {

	/**
	 * 重置任务延时时长
	 */
	private static final long REST_DELAY_MILLIS = 1000;
	/**
	 * 振动时长
	 */
	private static final int VIBRATOR_MILLISECONDS = 30;
	/**
	 * 圆半径
	 */
	private static final int CIRCLE_RADIUS = 29;
	/**
	 * 圆间距
	 */
	private static final int CIRCLE_MARGIN = 12;
	/**
	 * 密码至少4位数
	 */
	private static final int MIN_PWD_LENGTH = 4;
	/**
	 * 最大尝试次数
	 */
	private int mTryTimes = 5;
	/**
	 * 每个边上圆的个数
	 */
	private int mCircleCount = 3;
	/**
	 * 圆的总个数
	 */
	private int mCircleSize;
	/**
	 * 圆的间距
	 */
	private int mCircleSpace = 6;
	/**
	 * 手势锁内边距
	 */
	private int mPadding = 2;
	/**
	 * 正常颜色
	 */
	private int mColorNormal = 0xff7f7f7f;
	/**
	 * 选中颜色
	 */
	private int mColorSelected = 0xff209b54;
	/**
	 * 正确颜色
	 */
	private int mColorSelectedRight = 0xff209b54;
	/**
	 * 密码错误时显示的颜色
	 */
	private int mColorError = 0xFFFF0000;
	/**
	 * 圆的半径
	 */
	private float mCircleRadius;
	/**
	 * 内圆的半径
	 */
	private float mInnerCircleRadius;
	/**
	 * 画笔宽度
	 */
	private int mStrokeWidth = 2;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	/**
	 * 连线路径
	 */
	private Path mPath;
	/**
	 * 圆心坐标
	 */
	private float cx, cy, lastCx, lastCy;
	/**
	 * 手势移动的位置
	 */
	private float mMoveX = -1, mMoveY = -1;
	/**
	 * 圆心坐标
	 */
	private CircleCenterPoint mCircleCenterPoint;
	/**
	 * 圆心坐标集合
	 */
	private ArrayList<CircleCenterPoint> mCircleCenterPoints;
	/**
	 * 绘制的实心圆索引列表
	 */
	private ArrayList<Integer> mSelectedCircleIndexs;
	/**
	 * 绘制的密码（密码索引=实心圆索引+1）
	 */
	private ArrayList<Integer> mPasswords;
	/**
	 * 是否自动计算尺寸：false为固定dp值
	 */
	private boolean mIsAutoComputeSize=false;
	/**
	 * 内边距是否与圆间隔相同
	 */
	private boolean mIsPaddingEqualsMargin=false;
	/**
	 * 是否显示绘制的手势路径(默认为true)
	 */
	private boolean mIsShowGesutreLockPath = true;
	/**
	 * 绘制的密码是否正确
	 */
	private boolean mIsRight = true;
	/**
	 * 振动器
	 */
	private Vibrator mVibrator;
	/**
	 * 手势锁监听
	 */
	private GestureLockViewListener mGestureLockViewListener;

	/**
	 * 重置任务
	 */
	private Runnable mResetRunnable = new Runnable() {
		@Override
		public void run() {
			reset();
		}
	};

	/**
	 * 
	 * 初始化手势锁预览图
	 * 
	 * @param context
	 * @param pointCount
	 *            每个边上圆点的个数
	 * @param pointSpace
	 *            圆点的间距
	 * @param colorNormal
	 *            正常颜色
	 * @param colorSelected
	 *            选中颜色
	 */
	public GestureLockView(Context context, int pointCount, int pointSpace,
			int colorNormal, int colorSelected) {
		this(context, null, 0);
		this.mCircleCount = pointCount;
		this.mCircleSpace = pointSpace;
		this.mColorNormal = colorNormal;
		this.mColorSelected = colorSelected;
	}

	public GestureLockView(Context context) {
		this(context, null);
	}

	public GestureLockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GestureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		// setLayerType(View.LAYER_TYPE_SOFTWARE, null);//TODO 禁止硬件加速
		mCircleSize = mCircleCount * mCircleCount;
		mCircleSpace = dip2px(getContext(), CIRCLE_MARGIN);
		mCircleRadius = dip2px(getContext(), CIRCLE_RADIUS);
		mInnerCircleRadius = getInnerCircleRadius();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setColor(mColorNormal);
		mPath = new Path();
		mSelectedCircleIndexs = new ArrayList<Integer>();
		mPasswords = new ArrayList<Integer>();
		// 初始化圆心坐标集合
		mCircleCenterPoints = new ArrayList<CircleCenterPoint>();
		for (int i = 0; i < mCircleSize; i++) {
			mCircleCenterPoint = new CircleCenterPoint(0, 0);
			mCircleCenterPoints.add(mCircleCenterPoint);
		}
		//TODO 注释后方可在布局文件中预览
//		mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		// 测试被选中的点的绘制
//		 mSelectedCircleIndexs.add(0);
//		 mSelectedCircleIndexs.add(4);
//		 mSelectedCircleIndexs.add(8);
	}

	/**
	 * 获取内圆的半径
	 * 
	 * @return
	 */
	private float getInnerCircleRadius() {
		return mCircleRadius / 3;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int space = 0;
		if (mIsAutoComputeSize) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = MeasureSpec.getSize(heightMeasureSpec);
			space = mCircleSpace
					* (mCircleCount + (mIsPaddingEqualsMargin ? 1 : -1));
			mCircleRadius = ((Math.min(width, height) - space) / mCircleCount) * 0.5f;
			mInnerCircleRadius = getInnerCircleRadius();
		} else {
			space = mCircleSpace
					* (mCircleCount + (mIsPaddingEqualsMargin ? 1 : -1));
		}
		//重设控件尺寸
		int circleSize = (int) (mCircleRadius * 2 * mCircleCount);
		int size = space + circleSize + mStrokeWidth;
		int measureSpec = MeasureSpec.makeMeasureSpec(size,
				MeasureSpec.UNSPECIFIED);
		setMeasuredDimension(measureSpec, measureSpec);
		mPadding = mIsPaddingEqualsMargin ? mCircleSpace : mStrokeWidth / 2;
		// 重新计算圆心坐标集合
		cx = cy = mCircleRadius + mPadding;
		for (int i = 0; i < mCircleSize; i++) {
			mCircleCenterPoints.get(i).setCenter(cx, cy);
			// 计算下一个圆的圆心坐标
			cx += mCircleRadius * 2 + mCircleSpace;// 两圆圆心距=半径之和+两圆间距
			if ((i + 1) % mCircleCount == 0) {
				cx = mCircleRadius + mPadding;// 每行第一个
				cy += mCircleRadius * 2 + mCircleSpace;// 换行，两圆圆心距=半径之和+两圆间距
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mColorSelected = mIsRight ? mColorSelectedRight : mColorError;
		mPaint.setStrokeWidth(mStrokeWidth);
		for (int i = 0; i < mCircleSize; i++) {
			mCircleCenterPoint = mCircleCenterPoints.get(i);
			cx = mCircleCenterPoint.cx;
			cy = mCircleCenterPoint.cy;
			if (mSelectedCircleIndexs.contains(i)
					&& (mIsShowGesutreLockPath || !mIsRight)) {
				mPaint.setColor(mColorSelected);
				mPaint.setStyle(Style.STROKE);
				mPaint.setAlpha(125);
				canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
				mPaint.setStyle(Style.FILL);
				mPaint.setAlpha(50);
				canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
				mPaint.setAlpha(255);
				canvas.drawCircle(cx, cy, mInnerCircleRadius, mPaint);
			} else {
				mPaint.setStyle(Style.STROKE);
				mPaint.setColor(mColorNormal);
				mPaint.setAlpha(255);
				canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
			}
		}
		mPaint.setStyle(Style.STROKE);
		mPaint.setColor(mColorSelected);
		mPaint.setStrokeWidth(mStrokeWidth * 2);
		if (mIsShowGesutreLockPath || !mIsRight) {
			if (!mPath.isEmpty()) {
				canvas.drawPath(mPath, mPaint);
			}
			if (mMoveX != -1 && mMoveY != -1) {
				canvas.drawLine(lastCx, lastCy, mMoveX, mMoveY, mPaint);
			}
		}
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 移除延时重置任务
			removeCallbacks(mResetRunnable);
			reset();
			computeSelectedCircleIndex(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			computeSelectedCircleIndex(x, y);
			mMoveX = x;
			mMoveY = y;
			break;
		case MotionEvent.ACTION_UP:
			if (mTryTimes > 0) {
				this.mTryTimes--;
			}
			if (mGestureLockViewListener != null) {
				if (mPasswords.size() < MIN_PWD_LENGTH) {
					mGestureLockViewListener.onPasswordTooShort(MIN_PWD_LENGTH);
					mGestureLockViewListener.onTryFailure(mTryTimes);
					mIsRight = false;
				} else {
					mIsRight = mGestureLockViewListener.onActionUp(mPasswords);
					if (!mIsRight) {
						mGestureLockViewListener.onTryFailure(mTryTimes);
					}
				}
			}
			mMoveX = -1;
			mMoveY = -1;
			// 延时重置
			postDelayed(mResetRunnable, REST_DELAY_MILLIS);
			break;

		}
		invalidate();
		return true;
	}

	/**
	 * 根据坐标计算选中某个手势圆
	 * 
	 * @param x
	 * @param y
	 */
	private void computeSelectedCircleIndex(int x, int y) {
		for (int i = 0; i < mCircleSize; i++) {
			mCircleCenterPoint = mCircleCenterPoints.get(i);
			if (x > mCircleCenterPoint.cx - mCircleRadius
					&& x < mCircleCenterPoint.cx + mCircleRadius
					&& y > mCircleCenterPoint.cy - mCircleRadius
					&& y < mCircleCenterPoint.cy + mCircleRadius) {
				if (!mSelectedCircleIndexs.contains(i)) {
					this.mSelectedCircleIndexs.add(i);
					this.mPasswords.add(i + 1);// 密码索引=实心圆索引+1
					if (mPath.isEmpty()) {
						mPath.moveTo(mCircleCenterPoint.cx,
								mCircleCenterPoint.cy);
					} else {
						mPath.lineTo(mCircleCenterPoint.cx,
								mCircleCenterPoint.cy);
					}
					lastCx = mCircleCenterPoint.cx;
					lastCy = mCircleCenterPoint.cy;
					if(mVibrator!=null){
						mVibrator.vibrate(VIBRATOR_MILLISECONDS);
					}
					if (mGestureLockViewListener != null)
						mGestureLockViewListener.onBlockSelected(i);
				}
				break;
			}
		}
	}

	/**
	 * 清空实心圆点序号列表,并重绘
	 */
	public void reset() {
		mSelectedCircleIndexs.clear();
		mPasswords.clear();
		mPath.reset();
		mIsRight = true;
		invalidate();
		if (mGestureLockViewListener != null)
			mGestureLockViewListener.onReset();
	}

	/**
	 * 设置内边距是否与圆间隔相同
	 * 
	 * @param isPaddingEqualsMargin
	 */
	public void setPaddingEqualsMargin(boolean isPaddingEqualsMargin) {
		this.mIsPaddingEqualsMargin = isPaddingEqualsMargin;
	}

	/**
	 * 设置是否显示绘制的手势路径(默认为true)
	 * 
	 * @param isShowGesutreLockPath
	 */
	public void setShowGesutreLockPath(boolean isShowGesutreLockPath) {
		this.mIsShowGesutreLockPath = isShowGesutreLockPath;
	}

	/**
	 * 设置尝试解锁次数
	 * 
	 * @param tryTimes
	 */
	public void setTryTimes(int tryTimes) {
		this.mTryTimes = tryTimes;
	}

	/**
	 * 设置手势锁监听
	 * 
	 * @param listener
	 */
	public void setGestureLockViewListener(GestureLockViewListener listener) {
		mGestureLockViewListener = listener;
	}

	public static interface GestureLockViewListener {
		/**
		 * 重置
		 */
		void onReset();

		/**
		 * 密码太短(通常在设置密码时会用到)
		 * 
		 * @param minPwdLength
		 *            最小长度
		 */
		void onPasswordTooShort(int minPwdLength);

		/**
		 * 单独选中元素的Id(通常在设置密码时会用到)
		 * 
		 * @param position
		 */
		void onBlockSelected(int position);

		/**
		 * 手势绘制结束,验证密码是否正确
		 * 
		 * @param password
		 *            绘制的密码序列
		 * @return 密码对错
		 */
		boolean onActionUp(List<Integer> password);

		/**
		 * 超过尝试次数(通常在验证密码时会用到)
		 * 
		 * @param tryTimes
		 *            还有几次机会
		 */
		void onTryFailure(int tryTimes);
	}

	/**
	 * 圆心坐标
	 */
	public static class CircleCenterPoint {
		float cx, cy;

		public CircleCenterPoint() {
		}

		public CircleCenterPoint(float cx, float cy) {
			super();
			this.cx = cx;
			this.cy = cy;
		}

		public void setCenter(float cx, float cy) {
			this.cx = cx;
			this.cy = cy;
		}
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

//	public static int px2dip(Context context, float pxValue) {
//		final float scale = context.getResources().getDisplayMetrics().density;
//		return (int) (pxValue / scale + 0.5f);
//	}

}
