package cn.lzh.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 支持侧拉操作的布局
 * @author lzh
 * 
 */
@Deprecated
public class SlidingLayout extends FrameLayout {
	// * 侧拉删除 (ViewGroup)
		// 1. 创建 SwipeLayout, 继承FrameLayout
		// 2. 创建 ViewDragHelper, 获取两个View, mFrontView,mBackView
		// 3. 如何摆放两个View的初始位置, 重写 onLayout, 将 backView 放置到右边
		// 4. 修正水平偏移值, 两个View都要修正
		// 5. 移动任意一个View的时候, 让另外一个也跟着移动
		// 6. 在onViewReleased 方法中处理手指抬起后的打开/关闭
		// 7. 在打开/关闭方法中, 重新摆放两个View, 使用计算矩形的方式
		// 8. 平滑动画
		// 9. 状态和回调. 有几种状态? 3种, 需要几个回调? 5个. 写好之后, 测试.
		// 10.放入ListView中
		// 10.1. 只能有一个打开. 怎让知道某个条目打开了, 关闭了? 通过回调.
		// 10.2. ListView滑动的时候全部关闭, 怎样获取ListView的滑动状态? 设置 onScrollListener
	private ViewDragHelper mViewDragHelper;
	private ViewGroup mViewGroupBack;
	private ViewGroup mViewGroupFront;
	private int mWidth, mHeight;
	private int mViewGroupBackWidth;

	private DragState mDragState= DragState.CLOSE;
	
	private OnDragStateChangeListener mOnDragStateChangeListener;
	/**
	 * 是否被打开
	 */
	private boolean mIsOpen;
	/**
	 * 是否允许滑动
	 */
	private boolean mIsEnableSliding;
	
	public SlidingLayout(Context context) {
		this(context, null);
	}

	public SlidingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() < 2) {
			throw new IllegalArgumentException("至少添加2个ViewGroup");
		}
		if (getChildAt(0) instanceof ViewGroup
				&& getChildAt(1) instanceof ViewGroup) {
			mViewGroupBack = (ViewGroup) getChildAt(0);
			mViewGroupFront = (ViewGroup) getChildAt(1);
		} else {
			throw new IllegalArgumentException("前两个子控件必须是ViewGroup");
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mViewGroupBackWidth = mViewGroupBack.getMeasuredWidth();
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		//TODO 如何摆放两个View的初始位置, 重写 onLayout, 将 backView 放置到右边
		mViewGroupBack.layout(mWidth, 0, mWidth + mViewGroupBackWidth, mHeight);
	}
	
	private void init() {
		Callback cb = new Callback() {
			@Override
			public boolean tryCaptureView(View arg0, int arg1) {
				return true;
			}

			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				//TODO 修正水平偏移值, 两个View都要修正
				if (child == mViewGroupFront) {
					if (left < -mViewGroupBackWidth) {
						left = -mViewGroupBackWidth;
					} else if (left > 0) {
						left = 0;
					}
				} else if (child == mViewGroupBack) {
					if (left < mWidth - mViewGroupBackWidth) {
						left = mWidth - mViewGroupBackWidth;
					} else if (left > mWidth) {
						left = mWidth;
					}
				}
				return left;
			}

			@Override
			public int getViewHorizontalDragRange(View child) {
				return mViewGroupBackWidth;
			}

			@Override
			public void onViewPositionChanged(View changedView, int left,
					int top, int dx, int dy) {
				//TODO 移动任意一个View的时候, 让另外一个也跟着移动
				if (changedView == mViewGroupFront) {
					mViewGroupBack.offsetLeftAndRight(dx);
				} else if (changedView == mViewGroupBack) {
					mViewGroupFront.offsetLeftAndRight(dx);
				}
				dispatchDragState(mViewGroupFront.getLeft());
				invalidate();
			}

			@Override
			public void onViewReleased(View releasedChild, float xvel,
					float yvel) {
				//TODO 处理手指抬起后的打开/关闭
				if (xvel == 0
						&& mViewGroupFront.getLeft() < -mViewGroupBackWidth * 0.5f) {
					open();
				} else if (xvel < 0) {
					open();
				} else {
					close();
				}
			}
		};
		mViewDragHelper = ViewDragHelper.create(this, cb);
	}

	/**
	 * 更新侧滑面板的状态
	 * 
	 * @param left
	 */
	protected void dispatchDragState(int left) {
		DragState lastDragState=mDragState;
		mDragState=updateDragState(left);
		if(mOnDragStateChangeListener!=null){
			if(lastDragState!=mDragState){
				if(mDragState== DragState.OPEN){
					mOnDragStateChangeListener.onOpen(this);
				}else if(mDragState== DragState.CLOSE){
					mOnDragStateChangeListener.onClose(this);
				}else if(mDragState== DragState.DRAGGING){
					if(lastDragState== DragState.OPEN){
						mOnDragStateChangeListener.onStartClose(this);
					}else if(lastDragState== DragState.CLOSE){
						mOnDragStateChangeListener.onStartOpen(this);
					}
				}				
			}else if(mDragState== DragState.DRAGGING){
				mOnDragStateChangeListener.onDragging();
			}
		}
		
	}
	
	/**
	 * 更新侧滑面板的状态
	 * 
	 * @param left
	 */
	private DragState updateDragState(int left) {
		if(left==-mViewGroupBackWidth){
			return DragState.OPEN;
		}else if(left==0){
			return DragState.CLOSE;
		}
		return DragState.DRAGGING;
	}

	/**
	 * 打开侧滑,带有动画
	 */
	public void open() {
		open(true);
	}
	
	/**
	 * 关闭侧滑,带有动画
	 */
	public void close() {
		close(true);
	}

	/**
	 * 打开侧滑
	 * @param isSmooth 是否带有动画
	 */
	public void open(boolean isSmooth) {
		if (isSmooth) {
			// 平滑动画
			mViewDragHelper.smoothSlideViewTo(mViewGroupFront,
					-mViewGroupBackWidth, 0);
			invalidate();
		} else {
			layoutContent(true);
		}
		mIsOpen=true;
	}

	/**
	 * 关闭侧滑
	 * @param isSmooth 是否带有动画
	 */
	public void close(boolean isSmooth) {
		if (isSmooth) {
			mViewDragHelper.smoothSlideViewTo(mViewGroupFront, 0, 0);
			invalidate();
		} else {
			layoutContent(false);
		}
		mIsOpen=false;
	}

	/**
	 * 更新组件布局 在打开/关闭方法中, 重新摆放两个View, 使用计算矩形的方式
	 * 
	 * @param isOpen
	 */
	private void layoutContent(boolean isOpen) {
		Rect frontRect = computeFrontRect(isOpen);
		Rect backRect = computeBackRectFromFront(frontRect);
		mViewGroupFront.layout(frontRect.left, frontRect.top, frontRect.right,
				frontRect.bottom);
		mViewGroupBack.layout(backRect.left, backRect.top, backRect.right,
				backRect.bottom);
	}

	private Rect computeFrontRect(boolean isOpen) {
		int left = 0;
		if (isOpen) {
			left = -mViewGroupBackWidth;
		}
		return new Rect(left, 0, left + mWidth, mHeight);
	}

	private Rect computeBackRectFromFront(Rect frontRect) {
		return new Rect(frontRect.right, frontRect.top, frontRect.right
				+ mViewGroupBackWidth, frontRect.bottom);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(mIsEnableSliding){
			return mViewDragHelper.shouldInterceptTouchEvent(ev);
		}else{
			return super.onInterceptTouchEvent(ev);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mIsEnableSliding){
			mViewDragHelper.processTouchEvent(event);
			return true;
		}else{
			return super.onTouchEvent(event);
		}
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	/**
	 * 判断侧滑是否打开
	 * @return
	 */
	public boolean isOpen() {
		return mIsOpen;
	}

	public void setEnableSliding(boolean isEnableSliding){
		this.mIsEnableSliding = isEnableSliding;
		if(!mIsEnableSliding){
			close();
		}
	}
	
	public boolean isEnableSliding() {
		return mIsEnableSliding;
	}

	public OnDragStateChangeListener getOnDragStateChangeListener() {
		return mOnDragStateChangeListener;
	}

	public void setOnDragStateChangeListener(
			OnDragStateChangeListener listener) {
		this.mOnDragStateChangeListener = listener;
	}

	/**
	 * 侧滑面板状态
	 * @author Administrator
	 *
	 */
	public enum DragState {
		CLOSE, OPEN, DRAGGING
	}

	/**
	 * 侧滑面板状态监听接口
	 * @author Administrator
	 *
	 */
	public interface OnDragStateChangeListener {
		/**
		 * 从滑动(拖动)状态到打开状态
		 * @param layout
		 */
		void onOpen(SlidingLayout layout);
		/**
		 * 从滑动(拖动)状态到关闭状态
		 * @param layout
		 */
		void onClose(SlidingLayout layout);

		/**
		 * 滑动(拖动)中
		 */
		void onDragging();

		/**
		 * 从关闭状态到滑动(拖动)状态
		 * @param layout
		 */
		void onStartOpen(SlidingLayout layout);

		/**
		 * 从打开状态到滑动(拖动)状态
		 * @param layout
		 */
		void onStartClose(SlidingLayout layout);
	}

	/**
	 * 侧滑面板状态监听接口的实现类
	 * @author Administrator
	 *
	 */
	public static class SimpleOnDragStateChangeAdapter implements OnDragStateChangeListener {

		@Override
		public void onStartOpen(SlidingLayout layout) {
			System.out.println("onStartOpen");
		}

		@Override
		public void onStartClose(SlidingLayout layout) {
			System.out.println("onStartClose");
		}

		@Override
		public void onOpen(SlidingLayout layout) {
			System.out.println("onOpen");
		}

		@Override
		public void onDragging() {
//			System.out.println("onDragging");
		}

		@Override
		public void onClose(SlidingLayout layout) {
			System.out.println("onClose");
		}
	};
}
