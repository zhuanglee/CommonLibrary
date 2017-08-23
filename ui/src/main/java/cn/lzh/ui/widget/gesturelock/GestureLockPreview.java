package cn.lzh.ui.widget.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * 手势锁图案预览控件
 * 
 * @author lzh
 *
 */
public class GestureLockPreview extends View {
	
	/**
	 * 每个边上圆点的个数
	 */
	private int mPointCount = 3;
	/**
	 * 圆点的间距
	 */
	private int mPointSpace = 6;
	private int mColorNormal = 0xff7f7f7f;
	private int mColorSelected = 0xff209b54;
	private float mStrokeWidth = 2;
	private Paint mPaint;
	/**
	 * 圆点的半径
	 */
	private float mPointRadius;
	/**
	 * 绘制的实心点索引列表
	 */
	private ArrayList<Integer> mPoints;

	/**
	 * 初始化手势锁预览图
	 * 
	 * @param context
	 * @param pointCount
	 *            每个边上圆点的个数
	 * @param pointSpace
	 *            圆点的间距
	 */
	public GestureLockPreview(Context context, int pointCount, int pointSpace,
			int colorNormal, int colorSelected) {
		this(context);
		this.mPointCount = pointCount;
		this.mPointSpace = pointSpace;
		this.mColorNormal = colorNormal;
		this.mColorSelected = colorSelected;
	}

	public GestureLockPreview(Context context) {
		this(context, null);
	}

	public GestureLockPreview(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GestureLockPreview(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setColor(mColorNormal);
		mPoints=new ArrayList<Integer>();
//		测试被选中的点的绘制
//		mPoints.add(0);
//		mPoints.add(4);
//		mPoints.add(8);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int space=mPointSpace*(mPointCount+1);
		mPointRadius = ((Math.min(width, height) - space) / mPointCount) * 0.5f;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float cx=mPointRadius+mPointSpace,cy=mPointRadius+mPointSpace;
		for(int i=0;i<mPointCount*mPointCount;i++){
			if(mPoints.contains(i)){
				mPaint.setStyle(Style.FILL);
				mPaint.setColor(mColorSelected);
			}else{
				mPaint.setStyle(Style.STROKE);
				mPaint.setColor(mColorNormal);
			}
			canvas.drawCircle(cx, cy, mPointRadius, mPaint);
			//计算下一个圆的圆心坐标
			cx+=mPointRadius*2+mPointSpace;//两圆圆心距=半径之和+两圆间距
			if((i+1)%mPointCount==0){
				cx=mPointRadius+mPointSpace;//每行第一个
				cy+=mPointRadius*2+mPointSpace;//换行，两圆圆心距=半径之和+两圆间距
			}
		}
		super.onDraw(canvas);
	}
	
	/**
	 * 添加选中的点的序号,并重绘
	 * @param position
	 */
	public void addSelectedPoint(int position) {
		if(!mPoints.contains(position)){
			this.mPoints.add(position);
			invalidate();
		}
	}
	
	/**
	 * 清空实心圆点序号列表,并重绘
	 */
	public void reset(){
		this.mPoints.clear();
		invalidate();
	}
}
