package cn.lzh.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆点
 * @author lzh
 *
 */
public class DotView extends View {

	private int mWidth;
	private int mHeight;
	private Paint mPaint;
	private int mPaintColor = 0xff209b54;
	

	public DotView(Context context) {
		this(context,null);
	}

	public DotView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mPaintColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int minMeasureSpec=Math.min(widthMeasureSpec, heightMeasureSpec);
		mWidth=mHeight=MeasureSpec.getSize(minMeasureSpec);
		setMeasuredDimension(minMeasureSpec, minMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(mWidth/2, mHeight/2, Math.min(mWidth, mHeight)/2, mPaint);
	}
	
	public void resize(int width,int height){
		mWidth=mHeight=Math.min(width, height);
		invalidate();
	}
	
	public void setPaintColor(int color){
		mPaintColor=color;
		mPaint.setColor(mPaintColor);
		invalidate();
	}
}
