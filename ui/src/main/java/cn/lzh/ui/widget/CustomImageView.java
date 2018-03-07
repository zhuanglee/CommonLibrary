package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.lzh.ui.R;
import cn.lzh.utils.BitmapUtil;

/**
 * 自定义View，实现圆角，圆形等效果
 * 
 * @author Administrator
 * 
 */
public class CustomImageView extends View {

	/**
	 * TYPE_CIRCLE / TYPE_ROUND
	 */
	private int type;
	private static final int TYPE_NORMAL = 0;
	private static final int TYPE_ROUND = 1;
	private static final int TYPE_CIRCLE = 2;
	/**
	 * 图片
	 */
	private Bitmap mSrc;

	/**
	 * 圆角的大小
	 */
	private float mRadius;

	/**
	 * 控件的宽度
	 */
	private int mWidth;
	/**
	 * 控件的高度
	 */
	private int mHeight;

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context) {
		this(context, null);
	}

	/**
	 * 初始化一些自定义的参数
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(attrs!=null){
			float defaultRadius=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
					getResources().getDisplayMetrics());
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.CustomImageView);
			int srcResId=typedArray.getResourceId(R.styleable.CustomImageView_src, 0);
			mSrc = BitmapFactory.decodeResource(getResources(),srcResId);
			type = typedArray.getInt(R.styleable.CustomImageView_type, 0);
			mRadius = typedArray.getDimension(R.styleable.CustomImageView_borderRadius, defaultRadius);
			typedArray.recycle();
		}
	}

	/**
	 * 计算控件的高度和宽度
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mWidth = specSize;
		} else {
			// 由图片决定的宽
			int desireByImg = getPaddingLeft() + getPaddingRight()
					+ mSrc.getWidth();
			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mWidth = Math.min(desireByImg, specSize);
			} else

				mWidth = desireByImg;
		}

		/***
		 * 设置高度
		 */
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY)// match_parent , accurate
		{
			mHeight = specSize;
		} else {
			int desire = getPaddingTop() + getPaddingBottom()
					+ mSrc.getHeight();

			if (specMode == MeasureSpec.AT_MOST)// wrap_content
			{
				mHeight = Math.min(desire, specSize);
			} else
				mHeight = desire;
		}

		setMeasuredDimension(mWidth, mHeight);

	}

	/**
	 * 绘制
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		switch (type) {
		case TYPE_NORMAL:
			canvas.drawBitmap(mSrc, 0, 0, null);
			break;
		case TYPE_ROUND:
			canvas.drawBitmap(BitmapUtil.toRoundCorner(mSrc, (int)mRadius), 0, 0, null);
			break;
		case TYPE_CIRCLE:// 如果是TYPE_CIRCLE绘制圆形
			int min = Math.min(mWidth, mHeight);
			if(min!=mSrc.getWidth()&&min!=mSrc.getHeight()){
				/**
				 * 长度如果不一致，按小的值进行压缩
				 */
				mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
			}
			canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
			break;
		}
	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int min) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(min / 2, min / 2, min / 2, paint);
		/**
		 * 使用SRC_IN，参考上面的说明
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * 根据原图添加圆角
	 * 
	 * @param source
	 * @return
	 */
	private Bitmap createRoundConerImage(Bitmap source) {
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(target);
		RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rect, mRadius, mRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
}
