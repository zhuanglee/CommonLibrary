package cn.lzh.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import cn.lzh.ui.R;
import cn.lzh.ui.utils.ToastUtil;


/**
 * 自定义日历控件：</br>
 * 日历单元格宽（高）=控件宽（高） - 控件左右（上下）边距 - 水平（垂直）单元格间距;</br>
 * @author lzh
 */
public class CalendarCard extends View {
	/**
	 * 日历标题：星期几
	 */
	private static final String[] WEEK_NAME = { "日", "一", "二", "三", "四", "五",
			"六" };
	/**
	 * 日历总列数
	 */
	private static final int TOTAL_COL = 7;
	/**
	 * 日历总行数,第一行是星期
	 */
	private static final int TOTAL_ROW = 7;
	/**
	 * 表格线的颜色
	 */
	private int mColorTableLine;
	/**
	 * 星期和日期之间的分割线的颜色
	 */
	private int mColorDivider;
	/**
	 * 日历边界阴影线的颜色
	 */
	private int mColorShadowLine;
	/**
	 * 星期的颜色
	 */
	private int mColorWeek;
	private int mColorNormal;
	private int mColorToday;
	
	private int mColorOtherMonth;
	private int mColorSelectedBg;
	private int mColorSelectedText;

	/**
	 * 是否绘制表格线(画表格线时，分割线和阴影线都不画),默认false
	 */
	private boolean mEnableDrawTableLine;
	/**
	 * 是否绘制其他月份的日期,默认false
	 */
	private boolean mEnableDrawOhterMonth;
	/**
	 * 点击其他月份的日期后是否翻页,默认为false
	 */
	private boolean mEndableChangePage;
	/**
	 * 未来的日期是否可点击(不可点击时,显示的颜色与本月的常规日期颜色不同),默认为false
	 */
	private boolean mEnableClickUnreachDay;
	/**
	 * 单元格水平间距
	 */
	private float mHorizontalCellSpace;
	/**
	 * 单元格垂直间距
	 */
	private float mVerticalCellSpace;
	/**
	 * 左边距
	 */
	private float mGetPaddingLeft;
	/**
	 * 上边距
	 */
	private float mGetPaddingTop;
	/**
	 * 单元格水平宽度=(控件宽度-2倍外边距-文本间距之和)/单元格列数
	 */
	private float mCellWidth;
	/**
	 * 单元格垂直宽度=(控件高度-2倍外边距-文本间距之和)/单元格行数
	 */
	private float mCellHeight;
	/**
	 * 单元格内字体宽度的中心
	 */
	private float mTextCenterX;
	/**
	 * 行数组，每个元素代表一行
	 */
	private Row mRows[] = new Row[TOTAL_ROW];
	/**
	 * 画笔
	 */
	private Paint mPaint;

	/**
	 * 记录当前显示的哪年哪月
	 */
	private CustomDate mShowDate;

	/**
	 * 不能采用CustomDate记录
	 */
	private String mSelectedDate;
	/**
	 * 表示滑动的时候，手的移动要大于这个距离才开始移动控件
	 */
	private int mTouchSlop;
	private float mDownX;
	private float mDownY;
	/**
	 * 单元格点击回调事件
	 */
	private OnClickCellListener mClickCellListener;

	public CalendarCard(Context context) {
		super(context);
		init(context, null);
	}

	public CalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CalendarCard(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		setCustomAttributes(context, attrs);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Paint.Align.CENTER);// 字体居中
		for (int i = 0; i < mRows.length; i++) {
			mRows[i] = new Row(i, TOTAL_COL);
		}
		mShowDate = new CustomDate();
		mSelectedDate = mShowDate.toString();
	}

	/**
	 * 设置自定义属性
	 * 
	 * @param context
	 * @param attrs
	 */
	private void setCustomAttributes(Context context, AttributeSet attrs) {
		if (context == null || attrs == null) {
			return;
		}
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CalendarCard);
		mColorTableLine = typedArray.getColor(
				R.styleable.CalendarCard_colorTableLine, Color.GRAY);
		mColorDivider = typedArray.getColor(
				R.styleable.CalendarCard_colorDivider, Color.GRAY);
		mColorShadowLine = typedArray.getColor(
				R.styleable.CalendarCard_colorShadowLine, Color.GRAY);
		mColorWeek = typedArray.getColor(R.styleable.CalendarCard_colorWeek,
				Color.BLACK);
		mColorNormal = typedArray.getColor(
				R.styleable.CalendarCard_colorNormal, Color.BLACK);
		mColorToday = typedArray.getColor(R.styleable.CalendarCard_colorToday,
				Color.RED);
		mColorOtherMonth = typedArray.getColor(
				R.styleable.CalendarCard_colorOtherMonth, Color.GRAY);
		mColorSelectedBg = typedArray.getColor(
				R.styleable.CalendarCard_colorSelectedBg,
				Color.parseColor("#209b54"));
		mColorSelectedText = typedArray.getColor(
				R.styleable.CalendarCard_colorSelectedText, Color.WHITE);
		mEnableDrawTableLine = typedArray.getBoolean(
				R.styleable.CalendarCard_enableDrawTableLine, false);
		mEnableDrawOhterMonth = typedArray.getBoolean(
				R.styleable.CalendarCard_enableDrawOhterMonth, false);
		mEndableChangePage = typedArray.getBoolean(
				R.styleable.CalendarCard_endableChangePage, false);
		mEnableClickUnreachDay = typedArray.getBoolean(
				R.styleable.CalendarCard_enableClickUnreachDay, false);
		mHorizontalCellSpace = typedArray.getDimension(
				R.styleable.CalendarCard_horizontalCellSpace, 0);
		mVerticalCellSpace = typedArray.getDimension(
				R.styleable.CalendarCard_verticalCellSpace, 0);
		if(mEndableChangePage){
			// 如果点击其他月份的日期可以翻页,则一定要绘制其他月份的日期
			mEnableDrawOhterMonth=true;
		}
		if(!mEnableDrawOhterMonth){
			// 如果不绘制其他月份的日期,则不绘制其他月份的日期
			mEnableDrawTableLine=false;
		}
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// TODO 尺寸值用整型会有偏差
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		float totalHorizantalPadding = getPaddingLeft() + getPaddingRight();
		float totalVerticalPadding = getPaddingTop() + getPaddingBottom();
		float totalHorizontalSpace = mHorizontalCellSpace * (TOTAL_COL - 1);// 水平间距之和
		float totalVerticalSpace = mVerticalCellSpace * (TOTAL_ROW - 1);// 垂直间距之和
		mCellWidth = (w - totalHorizantalPadding - totalHorizontalSpace)
				/ TOTAL_COL;
		mCellHeight = (h - totalVerticalPadding - totalVerticalSpace)
				/ TOTAL_ROW;
		mPaint.setTextSize(Math.min(mCellWidth, mCellHeight) / 2);// 设置字体大小
		mTextCenterX = (mCellWidth - mPaint.measureText(WEEK_NAME[6])) * 0.5f;
		mGetPaddingLeft = getPaddingLeft();
		mGetPaddingTop = getPaddingTop();
		fillDate();
	}

	/**
	 * 更新日期，并填充到行列对象中
	 */
	private void fillDate() {
		int monthDay = CustomDate.getDayOfMonth(); // 今天
		// 上个月的天数
		int daysOfLastMonth = CustomDate.getDaysOfMonth(mShowDate.year,
				mShowDate.month - 1);
		// 本月天数
		int daysOfCurrentMonth = CustomDate.getDaysOfMonth(mShowDate.year,
				mShowDate.month);
		int mFirstDayWeek = CustomDate.getWeekDayOfMonthFirstDay(
				mShowDate.year, mShowDate.month);
		boolean isCurrentMonth = CustomDate.isCurrentMonth(mShowDate);
		int day = 0;
		for (int i = 1; i < TOTAL_ROW; i++) {// 行,i=0是日历标题（星期几）
			for (int j = 0; j < TOTAL_COL; j++) {// 列
				// 单元格位置
				int position = j + (i - 1) * TOTAL_COL;
				if (position >= mFirstDayWeek
						&& position < mFirstDayWeek + daysOfCurrentMonth) {// 这个月的
					day++;
					mRows[i].cells[j].update(mShowDate.year, mShowDate.month,
							day, Status.CURRENT_MONTH_DAY, i, j);
					// 今天
					if (isCurrentMonth && day == monthDay) {
						mRows[i].cells[j].update(mShowDate.year,
								mShowDate.month, day, Status.TODAY, i, j);
					} else if (!mEnableClickUnreachDay && isCurrentMonth
							&& day > monthDay) { // 如果比这个月的今天要大，表示还没到
						mRows[i].cells[j].update(mShowDate.year,
								mShowDate.month, day, Status.UNREACH_DAY, i, j);
					}
				} else if (position < mFirstDayWeek) { // 过去一个月
					mRows[i].cells[j].update(mShowDate.year,
							mShowDate.month - 1, daysOfLastMonth
									- (mFirstDayWeek - position - 1),
							Status.PAST_MONTH_DAY, i, j);
				} else if (position >= mFirstDayWeek + daysOfCurrentMonth) { // 下个月
					mRows[i].cells[j].update(mShowDate.year,
							mShowDate.month + 1, position - mFirstDayWeek
									- daysOfCurrentMonth + 1,
							Status.NEXT_MONTH_DAY, i, j);
				}
			}
		}
		invalidate();
	}

	/**
	 * TODO 获取单元格的左边X值
	 * 
	 * @param col
	 *            第几列
	 * @return
	 */
	private float getCellLeft(int col) {
		return mGetPaddingLeft + (mCellWidth + mHorizontalCellSpace) * col;
	}

	/**
	 * TODO 获取单元格底部Y值(可以方便绘制文字)
	 * 
	 * @param row
	 *            第几行
	 * @return
	 */
	private float getCellBottom(int row) {
		float top = mGetPaddingTop + (mCellHeight + mVerticalCellSpace) * row;
		return top + mCellHeight;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制日历标题（星期几）
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mPaint.setColor(mColorWeek);
		FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
		float baseline = (2 * getCellBottom(0) - mCellHeight
				- fontMetrics.bottom - fontMetrics.top) * 0.5f;
		for (int i = 0; i < TOTAL_COL; i++) {
			// targetRect.centerX()实现水平居中，baseline实现垂直居中
			canvas.drawText(WEEK_NAME[i], getCellLeft(i) + mCellWidth * 0.5f,
					baseline, mPaint);
		}
		// 绘制日期单元格
		mPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
		for (int i = 1; i < TOTAL_ROW; i++) {
			if (mRows[i] != null) {
				mRows[i].drawCells(canvas);
			}
		}
		int paddingRight=getPaddingRight(),paddingBottom=getPaddingBottom();
		if (mEnableDrawTableLine) {
			if(mHorizontalCellSpace + mVerticalCellSpace <= 0){
				//绘制第一行的水平线
				canvas.drawLine(mGetPaddingLeft, mGetPaddingTop, getWidth() - paddingRight, mGetPaddingTop, mPaint);
				float verticalLineStartY= mGetPaddingTop;//垂直线开始位置
				float cellBottom,cellLeft;
				for (int i = 0; i <= TOTAL_ROW; i++) {
					cellBottom = getCellBottom(i);//在第i+1行底下Y值
					cellLeft = getCellLeft(i);
					//绘制水平线
					canvas.drawLine(mGetPaddingLeft, cellBottom, getWidth() - paddingRight, cellBottom, mPaint);
					//绘制垂直线
					canvas.drawLine(cellLeft, verticalLineStartY, cellLeft, getHeight() - paddingBottom, mPaint);
				}
			}
		}else{
			// 绘制分割线
			mPaint.setColor(mColorDivider);
			float dividerLineStartX = mGetPaddingLeft + mTextCenterX;
			float bottomY = getCellBottom(0);// 标题行底部
			canvas.drawLine(dividerLineStartX, bottomY, getWidth()
					- dividerLineStartX, bottomY, mPaint);
			// 绘制阴影线
			mPaint.setColor(mColorShadowLine);
			canvas.drawLine(0, getHeight()-paddingBottom, getWidth(), getHeight()-paddingBottom, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float disX = event.getX() - mDownX;
			float disY = event.getY() - mDownY;
			// if (Math.abs(disX) < mTouchSlop && Math.abs(disY) < mTouchSlop) {
			if (Math.abs(disX) < mCellWidth && Math.abs(disY) < mCellHeight) {
				int row = (int) ((mDownY - mGetPaddingTop) / (mCellHeight + mVerticalCellSpace));
				int col = (int) ((mDownX - mGetPaddingLeft) / (mCellWidth + mHorizontalCellSpace));
				if (mClickCellListener != null
						&& mClickCellListener.isClickable()) {
					measureClickCell(row, col);
				}
			} else {
				// TODO 左右滑动,切换到其他月份
				if (Math.abs(disX) > Math.abs(disY)) {
					int sliding = mTouchSlop;// px2dip(getContext(), 50);
					if (disX > sliding) {
						slideToLeft();
					} else if (disX < -sliding) {
						slideToRight();
					}
				}
			}
			break;
		}
		return true;
	}

	/**
	 * 计算点击的单元格
	 * 
	 * @param col
	 * @param row
	 */
	private void measureClickCell(int row, int col) {
		if (row <= 0 || row >= TOTAL_ROW || col >= TOTAL_COL)
			return;// 第0行为星期，屏蔽点击
		if (mRows[row] != null) {
			if (!mEnableDrawOhterMonth
					&& (mRows[row].cells[col].status == Status.PAST_MONTH_DAY || mRows[row].cells[col].status == Status.NEXT_MONTH_DAY)) {
				return;// 屏蔽其他月份的日期的单击事件
			}
			if (isValidDate(mRows[row].cells[col].date)) {// 只处理有效日期的单击事件
				String selectedDate = mRows[row].cells[col].date.toString();
				if (!mSelectedDate.equals(selectedDate)) {
					// 记录被选中的日期
					mSelectedDate = selectedDate;
					processClickListener(col, row);
				}
			}
		}
	}

	/**
	 * 判断日期是否有效
	 * 
	 * @param customDate
	 * @return
	 */
	private boolean isValidDate(CustomDate customDate) {
		boolean isValidDate = true;
		if (!mEnableClickUnreachDay) {
			// 未来日期无效
			CustomDate mTodayDate = new CustomDate();
			if (customDate.year > mTodayDate.year) {
				isValidDate = false;
			} else if (customDate.year == mTodayDate.year) {
				if (customDate.month > mTodayDate.month) {
					isValidDate = false;
				}
				if (customDate.month == mTodayDate.month
						&& customDate.day > mTodayDate.day) {
					isValidDate = false;
				}
			}
		}
		return isValidDate;
	}

	/**
	 * 处理单击事件，点击其他月份的日期，切换到相应月份的日历
	 * 
	 * @param col
	 * @param row
	 */
	private void processClickListener(int col, int row) {
		// 判断所选日期是否属于当前日历视图所对应的月份，不属于则跳转到对应月份的日历视图
		SildeDirection sildeDirection = SildeDirection.NO_SILDE;
		if (mEndableChangePage) {
			float date1 = mShowDate.year + mShowDate.month / 100f;
			float date2 = mRows[row].cells[col].date.year
					+ mRows[row].cells[col].date.month / 100f;
			if (date1 > date2) {
				// 跳转到上个月日历视图
				sildeDirection = SildeDirection.LEFT;
			} else if (date1 < date2) {
				// 跳转到下个月日历视图
				sildeDirection = SildeDirection.RIGHT;
			}
		}
		if (mClickCellListener != null) {
			// TODO 必须先回调单击事件,再刷新日历
			mClickCellListener.onClickDate(mRows[row].cells[col].date,
					sildeDirection);
		}
		if (sildeDirection == SildeDirection.LEFT) {
			slideToLeft();
		} else if (sildeDirection == SildeDirection.RIGHT) {
			slideToRight();
		} else {
			update(false);
		}
	}

	/**
	 * 刷新界面
	 * 
	 * @param isChange
	 *            日历是否改变
	 */
	private void update(boolean isChange) {
		fillDate();
		if (mClickCellListener != null) {
			mClickCellListener.onChangeCalendar(mShowDate);
		}
	}

	/**
	 * 从左往右划，上一个月
	 */
	public void slideToLeft() {
		if (mShowDate.month == 1) {
			mShowDate.month = 12;
			mShowDate.year -= 1;
		} else {
			mShowDate.month -= 1;
		}
		update(true);
	}

	/**
	 * 从右往左划，下一个月
	 */
	public void slideToRight() {
		// TODO 最多向右滑动到本月
		if (isValidDate(new CustomDate(mShowDate.year, mShowDate.month, 33))) {
			if (mShowDate.month == 12) {
				mShowDate.month = 1;
				mShowDate.year += 1;
			} else {
				mShowDate.month += 1;
			}
			update(true);
		} else {
			ToastUtil.show("最后一页了");
		}
	}

	public void setSelectedDate(CustomDate customDate) {
		setSelectedDate(customDate.toString());
	}

	public void setSelectedDate(String date) {
		mSelectedDate = date;
		for (int j = 0; j < mRows.length; j++) {
			for (int i = 0; i < mRows[0].cells.length; i++) {
				if (mRows[j].cells[i].date.toString().equals(mSelectedDate)) {
					processClickListener(i, j);
					return;// break;双层循环
				}
			}
		}
	}

	public String getSelectedDate() {
		return mSelectedDate;
	}

	/**
	 * 获取当前显示的是哪年哪月
	 * 
	 * @return
	 */
	public CustomDate getShowDate() {
		return mShowDate;
	}

	public OnClickCellListener getClickCellListener() {
		return mClickCellListener;
	}

	/**
	 * 设置监听事件时才创建日历
	 * 
	 * @param listener
	 */
	public void setClickCellListener(OnClickCellListener listener) {
		this.mClickCellListener = listener;
	}

	/**
	 * 行
	 * 
	 */
	private class Row {
		/**
		 * 行号索引
		 */
		public int index;

		public Cell[] cells;

		/**
		 * 
		 * @param index
		 *            行号索引
		 * @param column
		 *            列的个数
		 */
		public Row(int index, int column) {
			this.index = index;
			cells = new Cell[column];
			for (int i = 0; i < column; i++) {
				cells[i] = new Cell();
			}
		}

		/**
		 * 绘制日期单元格
		 * @param canvas
		 */
		public void drawCells(Canvas canvas) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null) {
					cells[i].drawSelf(canvas);
				}
			}
		}

	}

	/**
	 * 日期单元格
	 */
	private class Cell {
		public CustomDate date;
		public Status status;
		private RectF targetRect;

		public Cell() {
			date = new CustomDate();
			status = Status.CURRENT_MONTH_DAY;
			targetRect = new RectF(0, 0, 0, 0);
		}

		/**
		 * 更新单元格数据
		 * 
		 * @param year
		 *            年
		 * @param month
		 *            月
		 * @param day
		 *            日
		 * @param status
		 *            状态
		 * @param row
		 *            行号
		 * @param col
		 *            第几列（星期几）
		 */
		public void update(int year, int month, int day, Status status,
				int row, int col) {
			// 不能直接对象赋值
			this.date.year = year;
			this.date.month = month;
			this.date.day = day;
			this.date.week = col;
			this.status = status;
			float cellLeft = getCellLeft(col);
			float cellBottom = getCellBottom(row);
			targetRect.set(cellLeft, cellBottom - mCellHeight, cellLeft
					+ mCellWidth, cellBottom);
		}

		/**
		 * 画单元格
		 * 
		 * @param canvas
		 */
		public void drawSelf(Canvas canvas) {
			if (mEnableDrawTableLine
					&& mHorizontalCellSpace + mVerticalCellSpace > 0) {
				mPaint.setColor(mColorTableLine);
				mPaint.setStyle(Style.STROKE);
				canvas.drawRect(targetRect, mPaint);
			}
			mPaint.setStyle(Style.FILL);
			switch (status) {
			case CURRENT_MONTH_DAY:
				mPaint.setColor(mColorNormal);
				break;
			case TODAY:
				mPaint.setColor(mColorToday);
				break;
			case UNREACH_DAY:
				mPaint.setColor(mColorOtherMonth);
				break;
			case PAST_MONTH_DAY:
			case NEXT_MONTH_DAY:
				mPaint.setColor(mColorOtherMonth);
				if (mEnableDrawOhterMonth) {
					break;
				} else {
					return;// 屏蔽其他月份
				}
			}
			if (mSelectedDate != null && mSelectedDate.equals(date.toString())) {
				// 绘制被选日期的背景
				mPaint.setColor(mColorSelectedBg);
				mPaint.setStyle(Style.FILL);
				if (mEnableDrawTableLine) {
					canvas.drawRect(targetRect, mPaint);
				} else {
					float radius = Math.min(mCellWidth, mCellHeight) * 0.4f;
					// 绘制文本是从文本左下角开始的，要使圆心坐标与文字中心坐标重合,此时文字中心就是方格中心
					canvas.drawCircle(targetRect.centerX(),
							targetRect.centerY(), radius, mPaint);
				}
				// 设置被选日期的字体颜色
				mPaint.setColor(mColorSelectedText);
			}
			// 将文本画在方格中心
			String content = (date.day > 9 ? "" : "0") + date.day;
			FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
			float baseline = (targetRect.bottom + targetRect.top
					- fontMetrics.bottom - fontMetrics.top) / 2;
			// targetRect.centerX()实现水平居中，baseline实现垂直居中
			canvas.drawText(content, targetRect.centerX(), baseline, mPaint);
		}
	}

	/**
	 * 单元格点击的回调接口
	 * 
	 */
	public interface OnClickCellListener {
		/**
		 * 点击日期
		 * 
		 * @param date
		 * @param sildeDirection
		 */
		void onClickDate(CustomDate date, SildeDirection sildeDirection);

		/**
		 * 切换日历面板时回调
		 * 
		 * @param date
		 */
		void onChangeCalendar(CustomDate date);

		/**
		 * 判断日历是否可单击
		 * 
		 * @return
		 */
		public boolean isClickable();
	}

	/**
	 * 日期的状态
	 * 
	 * @author lzh
	 *
	 */
	public static enum Status {
		/**
		 * 今天
		 */
		TODAY,
		/**
		 * 当前月的日期
		 */
		CURRENT_MONTH_DAY,
		/**
		 * 上个月
		 */
		PAST_MONTH_DAY,
		/**
		 * 下个月
		 */
		NEXT_MONTH_DAY,
		/**
		 * 本月还未到的日期
		 */
		UNREACH_DAY;
	}

	/**
	 * 滑动的方向
	 * 
	 * @author lzh
	 *
	 */
	public static enum SildeDirection {
		RIGHT, LEFT, NO_SILDE;
	}

	/**
	 * 自定义的日期，包括year,month,day
	 *
	 * @author lzh
	 *
	 */
	public static class CustomDate implements Serializable {

		private static final long serialVersionUID = 1L;
		public int year;
		public int month;
		public int day;
		public int week;

		public CustomDate() {
			Calendar calendar = Calendar.getInstance();
			this.year = calendar.get(Calendar.YEAR);
			this.month = calendar.get(Calendar.MONTH) + 1;
			this.day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		public CustomDate(Calendar calendar) {
			this.year = calendar.get(Calendar.YEAR);
			this.month = calendar.get(Calendar.MONTH) + 1;
			this.day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		public CustomDate(int year, int month, int day) {
			if (month > 12) {
				month = 1;
				year++;
			} else if (month < 1) {
				month = 12;
				year--;
			}
			this.year = year;
			this.month = month;
			this.day = day;
		}

		public CustomDate(CustomDate date) {
			this.year = date.year;
			this.month = date.month;
			this.day = date.day;
		}

		/*
		 * public static CustomDate modifiDayForObject(CustomDate date,int day){
		 * CustomDate modifiDate = new CustomDate(date.year,date.month,day);
		 * return modifiDate; }
		 */

		@Override
		public String toString() {
			return year + "-" + (month > 9 ? "" : "0") + month + "-"
					+ (day > 9 ? "" : "0") + day;
		}


		/**
		 * 判断指定日期是否是当前月份的日期
		 *
		 * @param date
		 * @return
		 */
		public static boolean isCurrentMonth(CustomDate date) {
			Calendar calendar = Calendar.getInstance();
			return (date.year == calendar.get(Calendar.YEAR)
					&& date.month == calendar.get(Calendar.MONTH) + 1);
		}

		public static int getDayOfMonth() {
			return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}

		/**
		 * 获取指定年月份的天数
		 *
		 * @param year
		 * @param month
		 *            实际月份
		 * @return
		 */
		public static int getDaysOfMonth(int year, int month) {
			// 日历中的月份=实际月份-1
			GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
			return getDaysOfMonth(calendar);
		}

		/**
		 * 获取指定月历的天数
		 *
		 * @param calendar
		 * @return
		 */
		public static int getDaysOfMonth(Calendar calendar) {
			return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		}

		/**
		 * 获取指定年月的第一天是星期几
		 *
		 * @param year
		 * @param month
		 *            实际月份
		 * @return
		 */
		public static int getWeekDayOfMonthFirstDay(int year, int month) {
			// 日历中的月份=实际月份-1
			GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
			int currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			return currentWeekDay;
		}
	}
}