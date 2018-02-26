package cn.lzh.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import cn.lzh.ui.R;

/**
 * 自定义输入身份证号码的编辑框
 * @author Administrator
 * 已设置为单行显示，不可编辑
 */
public class InputIDCardEditText extends AppCompatEditText {

	private static final int COLOR_LINE = 0xff979797;
	private Paint mPaint;
	private RectF mRectF;
	//自定义属性
	private boolean mIsShowText;
	private int mTextCount=6;
	private float mStrokeWidth=1;
	private float mStrokeRadius=15;
	private float mCircleRadius=12;
	//动态计算
	private float mCellWidth,mCellHeight;
	private FontMetricsInt fontMetricsInt;
	private int mTextHeight;

	public InputIDCardEditText(Context context) {
		super(context);
		init(context,null);
	}
	
	public InputIDCardEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}
	
	public InputIDCardEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setSingleLine();
		setEnabled(false);
		mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		if(attrs!=null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputIDCardEditText);
			mIsShowText=a.getBoolean(R.styleable.InputIDCardEditText_isShowText, false);
			mTextCount=a.getInteger(R.styleable.InputIDCardEditText_textCount, mTextCount);
			mStrokeWidth=a.getDimension(R.styleable.InputIDCardEditText_strokeWidth, mStrokeWidth);
			mStrokeRadius=a.getDimension(R.styleable.InputIDCardEditText_strokeRadius, mStrokeRadius);
			mCircleRadius=a.getDimension(R.styleable.InputIDCardEditText_circleRadius, mCircleRadius);
			a.recycle();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		if(mCellWidth==0){
			measure(0, 0);
			mCellWidth=(float)(getWidth()-getPaddingLeft()-getPaddingRight())/mTextCount;
			mCellHeight=(float)(getHeight()-getPaddingTop()-getPaddingBottom());
			mRectF=new RectF(getPaddingLeft(), getPaddingTop(), 
					getWidth()-getPaddingRight(), getHeight()-getPaddingBottom());
		}
//		canvas.drawText("mCellWidth="+mCellWidth,  50, 20, mPaint);
		mPaint.setColor(COLOR_LINE);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setStyle(Style.STROKE);
		canvas.drawRoundRect(mRectF, mStrokeRadius, mStrokeRadius, mPaint);
		for (int i = 1; i < mTextCount; i++) {
			canvas.drawLine(getPaddingLeft()+mCellWidth*i, getPaddingTop(), 
					getPaddingLeft()+mCellWidth*i, getHeight()-getPaddingBottom(), mPaint);
		}
		String text = getText().toString().trim();
		int textLength = text.length();
		mPaint.setStyle(Style.FILL);
		if(mIsShowText){
			mPaint.setColor(Color.BLACK);
			mPaint.setTextSize(getTextSize());
			fontMetricsInt = mPaint.getFontMetricsInt();
			mTextHeight=fontMetricsInt.descent+fontMetricsInt.ascent;
			String subStr = null;
			float[] widths = new float[1];
			float x,y;
			for (int i = 0; i < mTextCount; i++) {
				if(i>=textLength){
					break;
				}
				subStr=text.substring(i, i+1);
				mPaint.getTextWidths(subStr, widths);
				x=getPaddingLeft()+mCellWidth*i+(mCellWidth-widths[0])/2f;
				y=getPaddingTop()+(mCellHeight-mTextHeight)/2f;
				canvas.drawText(subStr, x, y, mPaint);
			}
		}else{
			for (int i = 0; i < mTextCount; i++) {
				if(i>=textLength){
					break;
				}
				canvas.drawCircle(getPaddingLeft()+mCellWidth*i+mCellWidth/2f,
						getPaddingTop()+mCellHeight/2, mCircleRadius, mPaint);
			}
		}
	}
	
	/**
	 * 获取文字个数
	 */
	public int getTextCount() {
		return mTextCount;
	}

	/**
	 * 设置文字个数
	 */
	public void setTextCount(int textCount) {
		this.mTextCount = textCount;
		postInvalidate();
	}
	
	/**
	 * 清空文本内容（已在主线程中延迟300ms更新）
	 */
	public void clearText(){
		postDelayed(new Runnable() {
			public void run() {
				setText("");
				invalidate();
			}
		}, 300);
	}
}
