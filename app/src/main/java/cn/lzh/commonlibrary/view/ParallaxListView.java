package cn.lzh.commonlibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author lzh
 * 
 * 自定义ListView,其头布局中的图片有视觉差效果
 * 
 * 3. 视差特效 (系统控件的增强)
    3.1. 分析实现方案. 问题: 以前是怎么做的? (加头, 处理touch事件), 今天换一个方法: 依然加头, 重写 overScrollBy 方法, 在这个方法里做处理
    3.2. 快速填充界面, 怎样给ListView 加头.
    3.3. 往ImageView中添加一个大小和它不同的图片, 会出现什么效果? 介绍 scaleType.
    3.4. 观察 overScrollBy 方法参数log, 分析 deltaY, isTouchEvent
    3.5. 当 deltaY < 0 , 并且 isTouchEvent 时, 修改 ImageView 的高度, 注意限定最大高度为图片的原始高度
    3.6. 怎样在ParallaxListView 中获取ImageView? 在 onCreate 方法中把 ImageView 传进来, 获取它的Drawable的原始高度 getDrawable. getIntrinsicHeight.
    3.7. 处理松手回弹
        3.7.1. 在哪里处理? onTouchEvent , 能否返回true? 不可以! 因为父类是 ListView, 有自己处理
        3.7.2. 怎样做属性动画, 初始值? 结束值? 初始值是ImageView的当前高度, 结束值是ImageView最初的高度, 此时发现获取到的ImageView高度为0
        3.7.3 如何在 onCreate 方法中获取控件宽高, 使用 view.post 方法, 或者 view.getViewTreeObserver.addOnGlobalLayoutChangeListener
    3.9 完成松手回弹效果.
 * 
 */
public class ParallaxListView extends ListView {

	private ImageView mIvHeader;
	private int mIvHeaderheight;
	private int mIvHeaderDrawableHeight;

	public ParallaxListView(Context context) {
		this(context, null);
	}

	public ParallaxListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

	}

	public void setHeaderImageView(ImageView ivHeader) {
		this.mIvHeader = ivHeader;
		mIvHeaderheight = mIvHeader.getHeight();
		mIvHeaderDrawableHeight = mIvHeader.getDrawable().getIntrinsicHeight();
	}

	// deltaY: Y轴方向的瞬间变化量
	// isTouchEvent: 是否为触摸滑动
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// 当ListView滑动到头, 继续滑动的时候调用
		// 当滑动到头, 继续触摸滑动的时候, 如果是向下拖动滑动, deltaY小于0,
		// 如果是向上拖动滑动, deltaY大于0, isTouchEvent 为true
		if (mIvHeader != null) {
			if (isTouchEvent && deltaY < 0) {
				LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader
						.getLayoutParams();
				layoutParams.height += Math.abs(deltaY / 2);
				if (layoutParams.height < mIvHeaderDrawableHeight) {
					mIvHeader.setLayoutParams(layoutParams);
				}
			}
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (ev.getAction() == MotionEvent.ACTION_UP) {
			if (mIvHeader != null) {
				// 创建一个0.0f-1.0f的值动画
				ValueAnimator animator = ValueAnimator.ofFloat(1.0f);
				animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animator) {
						float fraction = animator.getAnimatedFraction();// 获取百分比
						// 根据动画百分比计算当前头部的高度
						float currentHeight = mIvHeader.getHeight() + fraction * (mIvHeaderheight - mIvHeader.getHeight());
						LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mIvHeader
								.getLayoutParams();
						layoutParams.height = (int) currentHeight;
						mIvHeader.setLayoutParams(layoutParams);
					}
				});
				animator.setDuration(500);
				animator.setInterpolator(new OvershootInterpolator(4.0f));
				animator.start();
			}
		}

		return super.onTouchEvent(ev);
	}

}
