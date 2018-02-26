package cn.lzh.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.lzh.ui.R;

/**
 * 组合图片控件：
 * 1、通过setImageBitmaps方法设置要显示的图片集合；
 * 2、通过setBackgroundRadius背景图的圆角；
 * @author lzh
 *
 */
@Deprecated
public class GroupImageView extends AppCompatImageView {

	private static final String TAG = GroupImageView.class.getSimpleName();
	/**
	 * 最多绘制9个图片
	 */
	private static final int MAX_SIZE = 9;
	/**
	 * 普通的IamgeView
	 */
	private static final int DRAW_TYPE_DEFAULT = -1;
	/**
	 * 绘制组合图片
	 */
	private static final int DRAW_TYPE_GROUP_BITMAP = 666;
	/**
	 * 宽高
	 */
	private int mWidth, mHeight;
	/**
	 * 组合图片的水平、垂直间距
	 */
	private float mHorizontalSpace, mVerticalSpace;
	/**
	 * 边距
	 */
	private float mMarginLeft, mMarginTop;
	/**
	 * 行数
	 */
	private int mRow;
	/**
	 * 最大列数
	 */
	private int mMaxColumn;
	/**
	 * 组合图片的集合
	 */
	private ArrayList<Bitmap> mImageBitmaps;
	/**
	 * 组合图片的集合(压缩处理后，用于绘制)
	 */
	private ArrayList<Bitmap> mDrawBitmaps;
	/**
	 * 绘制图片的类型：0图片的资源ID,1图片路径,2Bitmap
	 */
	private int mDrawType = DRAW_TYPE_DEFAULT;
	/**
	 * 默认图片
	 */
	private Bitmap mDefaultBitmap;
	/**
	 * 背景矩形
	 */
	private RectF mRectBg;
	/**
	 * 背景矩形的圆角(计算小图尺寸时要减去该值求平均值)
	 */
	private float mRoundRectRadius;
	/**
	 * 背景色
	 */
	private int mColorBg;
	/**
	 * 画笔
	 */
	private Paint mPaint;

	public GroupImageView(Context context) {
		super(context);
		init(context, null);
	}

	public GroupImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public GroupImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setCustomAttributes(context, attrs);
		mDrawBitmaps = new ArrayList<Bitmap>();
		mImageBitmaps = new ArrayList<Bitmap>();
		mDefaultBitmap = new BitmapDrawable().getBitmap();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mColorBg);
	}


	/**
	 * TODO 设置自定义属性
	 * 
	 * @param context
	 * @param attrs
	 */
	private void setCustomAttributes(Context context, AttributeSet attrs) {
		if (context == null || attrs == null) {
			return;
		}
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.GroupImageView);
		mColorBg=typedArray.getColor(R.styleable.GroupImageView_colorBG, Color.GRAY);
		mRoundRectRadius=typedArray.getDimension(R.styleable.GroupImageView_bgRoundRectRadius, 5);
		mHorizontalSpace = typedArray.getDimension(R.styleable.GroupImageView_horizontalSpace, getPaddingLeft());
		mVerticalSpace =  typedArray.getDimension(R.styleable.GroupImageView_verticalSpace, getPaddingTop());
		typedArray.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 原因是getMeasuredHeight要在measure后才有值，getHeight要在layout后才有值
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		Log.w(TAG, "onMeasure:mWidth=" + mWidth + ",mHeight=" + mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		try {
			if (mDrawType == DRAW_TYPE_DEFAULT) {
				super.onDraw(canvas);
			}else if (mDrawType == DRAW_TYPE_GROUP_BITMAP) {
				if (mDrawBitmaps.size() != mImageBitmaps.size()) {
					Size subBitmapSize = getSubBitmapSize(mImageBitmaps.size());
					for (Bitmap bitmap : mImageBitmaps) {
						mDrawBitmaps.add(ThumbnailUtils.extractThumbnail(bitmap,
								subBitmapSize.width, subBitmapSize.height));
					}
				}
				// 绘制背景(圆角矩形)
				if (mRectBg == null) {
					mRectBg = new RectF(0, 0, mWidth, mHeight);
				}
				canvas.drawRoundRect(mRectBg, mRoundRectRadius, mRoundRectRadius,
						mPaint);
				// 绘制组合图片
				drawBitmaps(canvas, mDrawBitmaps.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 绘制组合图片
	 * 
	 * @param canvas
	 * @param size
	 */
	private void drawBitmaps(Canvas canvas, int size) {
		if (size <= 0) {
			//TODO 绘制默认图片
//			canvas.drawBitmap(mDefaultBitmap, 0, 0, null);
			return;
		}
		if (size > MAX_SIZE) {
			size = MAX_SIZE;// 最多9张图
		}
		int columnInFirstRow = size - (mRow - 1) * mMaxColumn;// 第一行的列数
		Bitmap bitmap;
		float left, top;
		for (int i = mRow - 1; i >= 0; i--) {
			int column = (i == 0 ? columnInFirstRow - 1 : mMaxColumn - 1);
			for (int j = column; j >= 0; j--) {
				size--;
				bitmap = mDrawBitmaps.get(size);
				if (i == 0) {// 第一行需要特殊处理
					if (columnInFirstRow == 1) {
						// 第1行只有1列： size == 3 || size == 7
						// 水平居中
						left = (mWidth - bitmap.getWidth()) * 0.5f;
						top = mMarginTop;
					} else if (columnInFirstRow == 2) {
						// size == 5 || size == 8
						// 两个图片水平居中
						left = (mWidth - 2 * bitmap.getWidth() - mHorizontalSpace)
								* 0.5f
								+ j
								* (bitmap.getWidth() + mHorizontalSpace);
						top = mMarginTop;
					} else {
						// 每行个数相同 ：size == 4 || size == 6 || size = 9
						left = mMarginLeft + j
								* (bitmap.getWidth() + mHorizontalSpace);
						top = mMarginTop + i
								* (bitmap.getHeight() + mVerticalSpace);
					}
				} else {
					left = mMarginLeft + j
							* (bitmap.getWidth() + mHorizontalSpace);
					top = mMarginTop + i
							* (bitmap.getHeight() + mVerticalSpace);
				}
				canvas.drawBitmap(bitmap, left, top, null);
			}
		}
	}

	/**
	 * 计算行列数
	 * 
	 * @param length
	 */
	private void computeLayout(int length) {
		if (length > 6) {
			mRow = 3;
			mMaxColumn = 3;
		} else if (length > 4) {
			mRow = 2;
			mMaxColumn = 3;
		} else if (length > 2) {
			mRow = 2;
			mMaxColumn = 2;
		} else if (length == 2) {
			mRow = 1;
			mMaxColumn = 2;
		} else {
			mRow = 1;
			mMaxColumn = 1;
		}
	}

	/**
	 * 获取组合图片中每个图片的尺寸
	 * 
	 * @param length
	 *            组合图片个数
	 * @return
	 */
	private Size getSubBitmapSize(int length) {
		computeLayout(length);
		int subWidth = (int) ((mWidth - getPaddingLeft() - getPaddingRight() - mHorizontalSpace
				* (mMaxColumn - 1)-mRoundRectRadius) / mMaxColumn);
		int subHeight = (int) ((mHeight - getPaddingTop() - getPaddingBottom() - mVerticalSpace
				* (mRow - 1)-mRoundRectRadius) / mRow);
		// 宽高相等,取较小的值
		if (subWidth > subHeight) {
			subWidth = subHeight;
		} else {
			subHeight = subWidth;
		}
		// 将剩余尺寸平均设为边距
		mMarginTop = (mHeight - subHeight * mRow - mVerticalSpace * (mRow - 1)) * 0.5f;
		mMarginLeft = (mWidth - subWidth * mMaxColumn - mHorizontalSpace
				* (mMaxColumn - 1)) * 0.5f;
		Log.w(TAG, "getSubBitmapSize:subWidth=" + subWidth + ",subHeight="
				+ subHeight + ",mMarginLeft=" + mMarginLeft + ",mMarginTop="
				+ mMarginTop);
		return new Size(subWidth, subHeight);
	}

	/**
	 * 设置组合图片的集合
	 * 
	 * @param bitmaps
	 */
	public void setImageBitmaps(List<Bitmap> bitmaps) {
		mDrawBitmaps.clear();
		mImageBitmaps.clear();
		if (bitmaps != null && bitmaps.size() > 0) {
			mImageBitmaps.addAll(bitmaps);
		}
		mDrawType = DRAW_TYPE_GROUP_BITMAP;
		invalidate();
	}
	
	public Bitmap getDefaultBitmap() {
		return mDefaultBitmap;
	}

	/**
	 * 设置默认显示的图片(可以在子线程中直接调用)
	 * @param mDefaultBitmap
	 */
	public void setDefaultBitmap(Bitmap mDefaultBitmap) {
		this.mDefaultBitmap = mDefaultBitmap;
		postInvalidate();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		mDrawType=DRAW_TYPE_DEFAULT;
		super.setImageBitmap(bm);
		invalidate();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		mDrawType=DRAW_TYPE_DEFAULT;
		super.setImageDrawable(drawable);
		invalidate();
	}
	
	@Override
	public void setImageURI(Uri uri) {
		mDrawType=DRAW_TYPE_DEFAULT;
		super.setImageURI(uri);
		invalidate();
	}
	
	@Override
	public void setImageResource(int resId) {
		mDrawType=DRAW_TYPE_DEFAULT;
		super.setImageResource(resId);
		invalidate();
	}

	@Override
	public void setBackgroundResource(int resid) {
		Log.e(TAG,"setBackgroundResource不可用");
	}
	
	@Override
	public void setBackgroundDrawable(Drawable background) {
		Log.e(TAG,"setBackgroundDrawable不可用");
	}

	@Override
	public void setBackground(Drawable background) {
		Log.e(TAG,"setBackground不可用");
	}

	@Override
	public void setBackgroundColor(int color) {
		if(mColorBg!=color){
			mColorBg=color;
			mPaint.setColor(mColorBg);
			invalidate();
		}
	}

	/**
	 * 设置背景圆角
	 * @param roundRectRadius
	 */
	public void setBackgroundRadius(int roundRectRadius) {
		if(mRoundRectRadius!=roundRectRadius){
			mRoundRectRadius=roundRectRadius;
			invalidate();
		}
	}
	
	public static class Size {
		private int width;
		private int height;

		public Size() {
		}

		public Size(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		@Override
		public String toString() {
			return "Size [width=" + width + ", height=" + height + "]";
		}

	}
}
