package cn.lzh.ui.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * 自动滚动的ScrollView<br/>
 * 1、添加View时，可自动滚动到新添加或删除的View处；<br/>
 * 2、不可在布局文件中添加子布局；<br/>
 * @author lzh
 *
 */
public class AutoScrollView extends HorizontalScrollView{
	
	/**
	 * 子布局
	 */
	protected ViewGroup mTabLayout;

	/**
	 * 当前选中项索引值
	 */
	protected int mSelectedTabIndex;

	protected Runnable mTabSelector;

    public AutoScrollView(Context context) {
        this(context, null);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabLayout = new LinearLayout(getContext());
        mTabLayout.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        addView(mTabLayout);
    }

	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);
        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex,true);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    protected void animateToTab(final View tabView,long delayMillis) {
		if(tabView == null){
        	return;
        }
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        postDelayed(mTabSelector, delayMillis);
	}

    protected void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        animateToTab(tabView,0);
    }
    
    /**
     * 获取TabLayout布局属性
     * @return
     */
    public final ViewGroup getTabLayout(){
    	return mTabLayout;
    }
    

    /**
     * 获取TabLayout布局过渡动画
     * @return
     */
    public final LayoutTransition getTabLayoutTransition(){
    	return mTabLayout.getLayoutTransition();
    }
    
    /**
     * 设置TabLayout布局过渡动画（添加、删除、显示、隐藏 子View时触发）
     * @param transition
     */
    public final void setTabLayoutTransition(LayoutTransition transition){
		mTabLayout.setLayoutTransition(transition);
    }

    /**
     * 添加View到指定位置
     * @param child
     * @param index -1则添加到最后
     */
    public final void addTabView(View child,int index){
    	mTabLayout.addView(child,index);
    	requestLayout();
    }
    
    /**
     * 添加View到最后
     * @param child
     */
    public final void addTabView(View child){
    	addTabView(child,-1);
    }

	public void removeAllTabViews() {
		mTabLayout.removeAllViews();
//		requestLayout();
		mSelectedTabIndex = 0;
	}

    /**
     * 移除已制定位置上的TabView
     * @param index
     */
    public final void removeTabViewAt(int index){
    	int childCount = mTabLayout.getChildCount();
    	if(index >= 0 && index < childCount){
    		mTabLayout.removeViewAt(index);
    		requestLayout();
    	}
    }
    
    /**
     * 移除指定TabView
     * @param view
     */
    public final void removeTabView(View view){
    	removeTabViewAt(mTabLayout.indexOfChild(view));
    }
    
    /**
     * 获取Tab个数
     * @return
     */
    public final int getTabCount(){
    	return mTabLayout.getChildCount();
    }
    
    /**
     * 获取指定索引处的TabView
     * @param index
     * @return
     */
    public final View getTabView(int index){
    	return mTabLayout.getChildAt(index);
    }

    /**
     * 获取指定TabView的索引
     * @param view
     * @return
     */
	public final int indexOfTabView(View view) {
		return mTabLayout.indexOfChild(view);
	}

	/**
	 * 设置当前项
	 * @param item
	 * @param doScroll 是否滚动
	 */
    public final void setCurrentItem(int item,boolean doScroll) {
    	mSelectedTabIndex = item;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
			if (doScroll && isSelected) {
                animateToTab(item);
            }
        }
    }
    
    /**
     * 设置当前项
     * @param item tabView index
     */
    public final void setCurrentItem(int item) {
    	setCurrentItem(item, true);
    }
    
    /**
     * 设置当前项
     * @param view tabView
     */
    public final void setCurrentItem(View view) {
    	setCurrentItem(indexOfTabView(view), true);
    }

}
