package cn.lzh.ui;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzh on 2018/2/26.<br/>
 */

public class FlowLayoutHelper {

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
    private final ViewGroup mViewGroup;
    private final OnMeasureCallback mCallback;
    private List<Line> mLines;

    public FlowLayoutHelper(ViewGroup vg, OnMeasureCallback callback) {
        this.mViewGroup = vg;
        this.mCallback = callback;
        this.mLines = new ArrayList<Line>();//存储所有行
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mLines.clear();//不清空会出现错乱
        int childCount = mViewGroup.getChildCount();
        if (childCount == 0) {
//			throw new IllegalArgumentException("请添加子控件");
            mCallback.onCallSuperMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = View.MeasureSpec.getSize(widthMeasureSpec);//ViewGroup的宽度
        mHorizontalSpace = mViewGroup.getPaddingLeft();
        mVerticalSpace = mViewGroup.getPaddingTop();
        mNoPaddingWidth = width - mViewGroup.getPaddingLeft() - mViewGroup.getPaddingRight();
        Line line = new Line();
        for (int i = 0; i < childCount; i++) {
            View childView = mViewGroup.getChildAt(i);
            childView.measure(0, 0);//引起childView的onMeasure方法回调，从而保证后面的方法能够有值
            if (line.getViewCount() == 0) {//当前行还没有View
                line.addView(childView);
            } else if (line.getWidth() + childView.getMeasuredWidth() + mHorizontalSpace < mNoPaddingWidth) {
                //当前行能够放下childView,则将childView添加到行
                line.addView(childView);
            } else {
                mLines.add(line);//保存前一行
                line = new Line();//换行
                line.addView(childView);
            }
        }
        mLines.add(line);//保存最后一行
        //重新测量该ViewGroup的高度
        int newHeight = mViewGroup.getPaddingTop() + mViewGroup.getPaddingBottom();
        newHeight += (mLines.size() - 1) * mVerticalSpace;//行间距总和
        for (Line currentLine : mLines) {
            newHeight += currentLine.getHeight();//统计所有行高
        }
        // 向父View申请对应的宽高
        mCallback.setMeasuredDimension(width, newHeight);
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineTop = mViewGroup.getPaddingTop();//第一行的顶部为getPaddingTop()
        int lineLeft;
        int viewCount;
        float avgWidth;
        int childViewNewWidth;
        int childViewWidthMeasureSpec;
        View childView;
        Line line;
        for (int i = 0; i < mLines.size(); i++) {
            line = mLines.get(i);
            viewCount = line.getViewCount();
            if (viewCount == 0) {
                continue;
            }
            lineLeft = mViewGroup.getPaddingLeft();//行的初始左边界
            if (i > 0) {
                //当前行的顶部=前一行的顶部+前一行的高度+垂直间距
                lineTop += mLines.get(i - 1).getHeight() + mVerticalSpace;
            }
            //计算当前行的剩余宽度,平均分给当前行的每一个View
            avgWidth = (mNoPaddingWidth - line.getWidth()) / viewCount;
            for (int j = 0; j < viewCount; j++) {
                if (j > 0) {
                    //当前View的左边界=前一个View的左边界+前一个View的宽度+水平间距
                    lineLeft += line.getView(j - 1).getWidth() + mHorizontalSpace;
                    //当前View的左边界=前一个View的右边界+水平间距
//					lineLeft=line.getView(j-1).getRight()+mHorizontalSpace;
                }
                childView = line.getView(j);
                //每个View的宽度在原来的基础上增加平均分配的宽度
                childViewNewWidth = (int) (childView.getMeasuredWidth() + avgWidth);
                childViewWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        childViewNewWidth, View.MeasureSpec.EXACTLY);
                childView.measure(childViewWidthMeasureSpec, 0);//重新测量
                childView.layout(lineLeft, lineTop,
                        lineLeft + childView.getMeasuredWidth(),
                        lineTop + childView.getMeasuredHeight());
            }
        }
    }

    public int getHorizontalSpace() {
        return mHorizontalSpace;
    }

    /**
     * 设置水平方向控件的间距
     * @param horizontalSpace 间距
     */
    public void setHorizontalSpace(int horizontalSpace) {
        if (horizontalSpace > 0) {
            this.mHorizontalSpace = horizontalSpace;
        }
    }

    public int getVerticalSpace() {
        return mVerticalSpace;
    }

    /**
     * 设置垂直方向控件的间距
     * @param verticalSpace 间距
     */
    public void setVerticalSpace(int verticalSpace) {
        if (verticalSpace > 0) {
            this.mVerticalSpace = verticalSpace;
        }
    }

    private class Line {
        private List<View> views;
        private int width;
        private int height;

        public Line() {
            views = new ArrayList<View>();
        }

        public void addView(View view) {
            if (!views.contains(view)) {
                views.add(view);
                width += view.getMeasuredWidth();
                if (views.size() > 1) {
                    //不是第一个则还要加上一个水平间距
                    width += mHorizontalSpace;
                }
                //行高取当前行中最高控件的高度
                height = Math.max(height, view.getMeasuredHeight());
            }
        }

        /**
         * 获取改行第i个View
         *
         * @param position
         * @return
         */
        public View getView(int position) {
            return views.get(position);
        }

        /**
         * 获取该行存放View的个数
         *
         * @return
         */
        public int getViewCount() {
            return views.size();
        }

        /**
         * 获取行宽
         *
         * @return
         */
        public int getWidth() {
            return width;
        }

        /**
         * 获取行高
         *
         * @return
         */
        public int getHeight() {
            return height;
        }

    }

    public interface OnMeasureCallback{
        void onCallSuperMeasure(int widthMeasureSpec, int heightMeasureSpec);
        void setMeasuredDimension(int measuredWidth, int measuredHeight);
    }
}
