package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import cn.lzh.ui.R;
import cn.lzh.utils.BitmapUtil;


/**
 * 圆形ImageView，可设置最多两个宽度不同且颜色不同的圆形边框。
 * 设置颜色在xml布局文件中由自定义属性配置参数指定
 */
public class RoundImageView extends android.support.v7.widget.AppCompatImageView {
    private static final int DEFAULT_COLOR = 0xFFFFFFFF;
    private int mBorderThickness;
    // 如果只有其中一个有值，则只画一个圆形边框
    private int mBorderOutsideColor = DEFAULT_COLOR;
    private int mBorderInsideColor = DEFAULT_COLOR;
    // 控件默认长、宽 
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private Bitmap mRoundBitmap;
    private ColorDrawable mColorDrawable;
    private Paint mPaint;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            mBorderThickness = ta.getDimensionPixelSize(R.styleable.RoundImageView_borderThickness, 1);
            mBorderOutsideColor = ta.getColor(R.styleable.RoundImageView_borderOutsideColor, DEFAULT_COLOR);
            mBorderInsideColor = ta.getColor(R.styleable.RoundImageView_borderInsideColor, DEFAULT_COLOR);
            ta.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        compute();
    }

    private void compute() {
        if (mWidth == 0 || mHeight == 0) {
            mWidth = getWidth();
            mHeight = getHeight();
        }
        if (mRadius == 0 && mWidth != 0 && mHeight != 0) {
            if (mBorderInsideColor != DEFAULT_COLOR && mBorderOutsideColor != DEFAULT_COLOR) {
                mRadius = (mWidth < mHeight ? mWidth : mHeight) / 2 - 2 * mBorderThickness;
            } else if (mBorderInsideColor != DEFAULT_COLOR) {// 定义画一个边框
                mRadius = (mWidth < mHeight ? mWidth : mHeight) / 2 - mBorderThickness;
            } else if (mBorderOutsideColor != DEFAULT_COLOR) {// 定义画一个边框
                mRadius = (mWidth < mHeight ? mWidth : mHeight) / 2 - mBorderThickness;
            } else {// 没有边框
                mRadius = (mWidth < mHeight ? mWidth : mHeight) / 2;
            }
            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            if (drawable instanceof ColorDrawable) {
                mColorDrawable = (ColorDrawable) drawable;
            } else if (drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                mRoundBitmap = BitmapUtil.getCroppedRoundBitmap(bitmapDrawable.getBitmap(), mRadius);
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRoundBitmap == null && mColorDrawable == null) {
            super.onDraw(canvas);
            return;
        }
        // 定义画两个边框，分别为外圆边框和内圆边框
        int radius = mRadius;
        if (mRoundBitmap == null) {
            mPaint.setColor(mColorDrawable.getColor());
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);
        } else {
            canvas.drawBitmap(mRoundBitmap, mWidth / 2 - radius, mHeight / 2 - radius, null);
        }
        if (mBorderInsideColor != DEFAULT_COLOR && mBorderOutsideColor != DEFAULT_COLOR) {
            // 画内圆
            drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);
            // 画外圆 
            drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);
        } else {// 定义画一个边框
            drawCircleBorder(canvas, radius + mBorderThickness / 2,
                    mBorderOutsideColor == DEFAULT_COLOR ? mBorderInsideColor : mBorderOutsideColor);
        }
    }

    /**
     * 边缘画圆
     */
    private void drawCircleBorder(Canvas canvas, int radius, int color) {
        mPaint.setColor(color);
        /* 设置paint的　style　为STROKE：空心 */
        mPaint.setStyle(Paint.Style.STROKE);
        /* 设置paint的外框宽度 */
        mPaint.setStrokeWidth(mBorderThickness);
        canvas.drawCircle(mWidth / 2, mHeight / 2, radius, mPaint);
    }
}