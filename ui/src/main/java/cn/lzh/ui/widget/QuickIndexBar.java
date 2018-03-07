package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义快速索引控件
 * 
 * @author lzh
 *
 */
public class QuickIndexBar extends android.support.v7.widget.AppCompatTextView {
	private static final int TEXT_COLOR_NORMAL = Color.BLACK;

	private static final int TEXT_COLOR_SELECTED = Color.GREEN;
	
	private static final int TEXT_COLOR_DISABLE = Color.GRAY;
	
	private static final List<String> LETTERS = Arrays.asList( "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" );
	/**
	 * 字母集合
	 */
	private List<String> mAllLetters;
	/**
	 * 字母是否有效
	 */
	private List<Boolean> mLetterUsefuls;
	/**
	 * 单元格水平宽度=(控件宽度-2倍外边距-左右内边距-文本间距之和)/单元格列数
	 */
	private float mCellWidth;
	/**
	 * 单元格垂直宽度=(控件高度-2倍外边距-文本间距之和)/单元格行数
	 */
	private float mCellHeight;
	/**
	 * 字体高度
	 */
	private int mTextFontHeight;

	/**
	 * 字体宽度
	 */
	private int mTextFontWidth;

	/**
	 * 单元格内字体宽度的中心
	 */
	private float mTextCenterX;
	/**
	 * 单元格内字体高度的中心
	 */
	private float mTextCenterY;

	/**
	 * 画笔
	 */
	private Paint mPaint;

	private int mTouchPosition,mSelectedPosition;
	
	private OnLetterChangeListener mOnLetterChangeListener;

	private ColorStateList textColors;

	private boolean isTouch;

	public QuickIndexBar(Context context) {
		this(context, null);
	}

	public QuickIndexBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuickIndexBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mAllLetters = new ArrayList<String>();
		mLetterUsefuls = new ArrayList<Boolean>();
		setAllLetters(LETTERS);
		textColors = getTextColors();
		mPaint = new Paint();	
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(getTextSize());
		mTextFontHeight = getTextFontHeight(mPaint);
		// mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		setOnLetterChangeListener(null);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCellWidth = w - getPaddingLeft() - getPaddingRight();
		mCellHeight = (h - getPaddingTop() - getPaddingBottom()) * 1.0f / mAllLetters.size();
		if(mCellHeight < mPaint.getTextSize()){
			mPaint.setTextSize(mCellHeight);
			mTextFontHeight = getTextFontHeight(mPaint);
		}
		mTextCenterY = (mCellHeight - mTextFontHeight) / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		int cellBottomY;// 单元格底部Y值
		for (int i = 0; i < mAllLetters.size(); i++) {
			if(mLetterUsefuls.get(i)){
				setSelected(mSelectedPosition == i);
				if(textColors!=null&&textColors.isStateful()){
					mPaint.setColor(textColors.getColorForState(getDrawableState(), 0));
				}else if(mSelectedPosition == i){
					mPaint.setColor(TEXT_COLOR_SELECTED);
				}else{
					mPaint.setColor(TEXT_COLOR_NORMAL);
				}
			}else {//if(isTouch){
				mPaint.setColor(TEXT_COLOR_DISABLE);
			}
			mTextFontWidth = getTextFontWidth(mPaint, mAllLetters.get(i));
			mTextCenterX = getPaddingLeft() + (mCellWidth - mTextFontWidth) * 0.5f;
			cellBottomY = (int) (getPaddingTop() + mCellHeight
					+ mCellHeight * i + 0.5f);
			canvas.drawText(mAllLetters.get(i), mTextCenterX, cellBottomY
					- mTextCenterY, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			isTouch = true;
			mTouchPosition = (int) ((touchY - getPaddingTop())
					/ mCellHeight + 0.5f);
			// 防止数组越界
			if (mTouchPosition < 0) {
				mTouchPosition = 0;
			} else if (mTouchPosition >= mAllLetters.size()) {
				mTouchPosition = mAllLetters.size() - 1;
			}
			if(mLetterUsefuls.get(mTouchPosition)){
				if (mSelectedPosition != mTouchPosition && mOnLetterChangeListener!=null) {
					mOnLetterChangeListener.onLetterChange(mAllLetters.get(mTouchPosition));
					mSelectedPosition = mTouchPosition;// 记录有效项的索引
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isTouch = false;
			if(mOnLetterChangeListener!=null){
				mOnLetterChangeListener.onActionUp();
			}
			break;
		}
		return true;
	}

	/**
	 * 设置所有字母集合
	 * @param letters
	 */
	public void setAllLetters(List<String> letters){
		if(letters!=null&&letters.size()>0){
			mAllLetters.clear();
			mAllLetters.addAll(letters);
			mLetterUsefuls.clear();
			for (int i = 0; i < mAllLetters.size(); i++) {
				mLetterUsefuls.add(true);
			}
		}
	}
	
	/**
	 * 设置有效字母集合
	 * @param letters
	 */
	public void setUsefulLetters(List<String> letters){
		if(letters!=null&&letters.size()>0){
			mLetterUsefuls.clear();
			for (int i = 0; i < mAllLetters.size(); i++) {
				mLetterUsefuls.add(letters.indexOf(mAllLetters.get(i)) != -1);
			}
		}
	}
	
	public OnLetterChangeListener getOnLetterChangeListener() {
		return mOnLetterChangeListener;
	}

	public void setOnLetterChangeListener(OnLetterChangeListener listener) {
		this.mOnLetterChangeListener = listener;
	}

	/**
	 * 设置当前选中的字母
	 * 
	 * @param letter
	 */
	public void setCurrentLetter(String letter) {
		if(isTouch){
			return;
		}
		for (int i = 0; i < mAllLetters.size(); i++) {
			if (mAllLetters.get(i).equals(letter)) {
				if (mSelectedPosition != i) {
					mSelectedPosition = i;
					invalidate();
					return;
				}
			}
		}
		
	}

	public interface OnLetterChangeListener {
		/**
		 * 当前所选字母发生改变
		 * 
		 * @param letter
		 *            当前所选字母
		 */
		void onLetterChange(String letter);
		/**
		 * 手指抬起
		 */
		void onActionUp();
	}

	/**
	 * 文本宽度
	 * 
	 * @param paint
	 *            画笔
	 * @param text
	 *            绘制的文本内容
	 * @return
	 */
	public static int getTextFontWidth(Paint paint, String text) {
		return (int) paint.measureText(text);
	}

	/**
	 * 文本高度
	 * 
	 * @param paint
	 *            画笔
	 * @return
	 */
	public static int getTextFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		int textHeight = (int) (Math.ceil(-fm.ascent));
		return textHeight;
	}

}
