package cn.lzh.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.lzh.utils.ScreenUtils;

/**
 * 自定义流式布局
 * @author lzh
 *
 */
public class FlowLayout extends ViewGroup {

	private static final String LOG_TAG = FlowLayout.class.getSimpleName();
	/**
	 * 默认间距
	 */
	private final int DEFAULT_SPACE = 10;
	/**
	 * 行中View的间距
	 */
	private int mHorizontalSpace;
	/**
	 * 行间距
	 */
	private int mVerticalSpace;
	/**
	 * 没有Padding值的ViewGroup的宽度
	 */
	private int mNoPaddingWidth;
	private List<Line> mLines;
	
	public FlowLayout(Context context) {
		this(context,null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLines=new ArrayList<Line>();//存储所有行
		mVerticalSpace=mHorizontalSpace= ScreenUtils.dip2px(context, DEFAULT_SPACE);
		setPadding(mVerticalSpace, mVerticalSpace, mVerticalSpace, mVerticalSpace);
	}
	
	public int getHorizontalSpace() {
		return mHorizontalSpace;
	}

	/**
	 * 设置水平间距
	 * @param horizontalSpace
	 */
	public void setHorizontalSpace(int horizontalSpace) {
		if(horizontalSpace>0){
			this.mHorizontalSpace = horizontalSpace;
		}		
	}

	public int getVerticalSpace() {
		return mVerticalSpace;
	}

	/**
	 * 设置垂直布局
	 * @param verticalSpace
	 */
	public void setVerticalSpace(int verticalSpace) {
		if(verticalSpace>0){
			this.mVerticalSpace = verticalSpace;
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);//ViewGroup的宽度
		mNoPaddingWidth=width-getPaddingLeft()-getPaddingRight();
		mLines.clear();//不清空会出现错乱
		int childCount=getChildCount();
		if(childCount==0){
//			throw new IllegalArgumentException("请添加子控件");
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			return;
		}
		Line line=new Line();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			childView.measure(0,0);//引起childView的onMeasure方法回调，从而保证后面的方法能够有值
			if(line.getViewCount()==0){//当前行还没有View
				line.addView(childView);
			}else if(line.getWidth()+childView.getMeasuredWidth()+mHorizontalSpace<mNoPaddingWidth){
				//当前行能够放下childView,则将childView添加到行
				line.addView(childView);
			}else{
				mLines.add(line);//保存前一行
				line=new Line();//换行
				line.addView(childView);
			}
		}
		mLines.add(line);//保存最后一行
		Log.d(LOG_TAG,"mLines.size()="+mLines.size());
		//重新测量该ViewGroup的高度
		int newHeight=getPaddingTop()+getPaddingBottom();
		newHeight+=(mLines.size()-1)*mVerticalSpace;//行间距总和
		for (Line currentLine : mLines) {
			newHeight+=currentLine.getHeight();//统计所有行高
			Log.d(LOG_TAG,"currentLine.getViewCount()="+currentLine.getViewCount());
			Log.d(LOG_TAG,"currentLine.getWidth()="+currentLine.getWidth());
		}
		//向父View申请对应的宽高
		setMeasuredDimension(width,newHeight);
	}

	@Override
	protected void onLayout(boolean changed,int l, int t, int r, int b) {
		int lineLeft=getPaddingLeft();
		int lineTop=getPaddingTop();//第一行的顶部为getPaddingTop()
		int viewCount = 0;
		float avgWidth = 0;
		int childViewNewWidth=0;
		int childViewWidthMeasureSpec=0;
		View childView= null;
		Line line = null;
		for (int i = 0; i < mLines.size(); i++) {
			line = mLines.get(i);
			viewCount = line.getViewCount();
			if(viewCount==0){
				continue;
			}
			lineLeft=getPaddingLeft();//行的初始左边界
			if(i>0){
				//当前行的顶部=前一行的顶部+前一行的高度+垂直间距
				lineTop+=mLines.get(i-1).getHeight()+mVerticalSpace;
			}
			//计算当前行的剩余宽度,平均分给当前行的每一个View
			avgWidth = (mNoPaddingWidth-line.getWidth())/viewCount;
			for(int j=0;j<viewCount;j++){
				if(j>0){
					//当前View的左边界=前一个View的左边界+前一个View的宽度+水平间距
					lineLeft+=line.getView(j-1).getWidth()+mHorizontalSpace;
					//当前View的左边界=前一个View的右边界+水平间距
//					lineLeft=line.getView(j-1).getRight()+mHorizontalSpace;
				}
				childView = line.getView(j);
				//每个View的宽度在原来的基础上增加平均分配的宽度
				childViewNewWidth=(int) (childView.getMeasuredWidth()+avgWidth);
				childViewWidthMeasureSpec=MeasureSpec.makeMeasureSpec(
						childViewNewWidth, MeasureSpec.EXACTLY);
				childView.measure(childViewWidthMeasureSpec, 0);//重新测量
				childView.layout(lineLeft, lineTop, 
						lineLeft+childView.getMeasuredWidth(), 
						lineTop+childView.getMeasuredHeight());
			}
		}
	}

	private class Line{
		private List<View> views;
		private int width;
		private int height;
		
		public Line() {
			views=new ArrayList<View>();
		}

		public void addView(View view){
			if(!views.contains(view)){
				views.add(view);
				width+=view.getMeasuredWidth();
				if(views.size()>1){
					//不是第一个则还要加上一个水平间距
					width+=mHorizontalSpace;
				}
				//行高取当前行中最高控件的高度
				height=Math.max(height, view.getMeasuredHeight());
			}
		}
		
		/**
		 * 获取改行第i个View
		 * @param position
		 * @return
		 */
		public View getView(int position){
			return views.get(position);
		}
		
		/**
		 * 获取该行存放View的个数
		 * @return
		 */
		public int getViewCount(){
			return views.size();
		}
		
		/**
		 * 获取行宽
		 * @return
		 */
		public int getWidth() {
			return width;
		}

		/**
		 * 获取行高
		 * @return
		 */
		public int getHeight() {
			return height;
		}
		
	}
	
}
