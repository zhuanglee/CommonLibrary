package cn.lzh.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;

import java.util.List;

/**
 * 群组头像控件
 *
 * @author lzh
 * @see #setImageBitmaps(List)
 */
public class GroupImageView extends androidx.appcompat.widget.AppCompatImageView {

    private GroupImageHelper helper = new GroupImageHelper();

    public GroupImageView(Context context) {
        this(context, null);
    }

    public GroupImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        helper.setSpace(getPaddingLeft(), getPaddingTop());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // getMeasuredHeight要在measure后才有值，getHeight要在layout后才有值
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingLeft();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        if (width > 0 && height > 0) {
            helper.setSize(width, height);
        }
    }

    /**
     * 设置群组图片的集合
     *
     * @param bitmaps 图片集合
     */
    public void setImageBitmaps(List<Bitmap> bitmaps) {
        setImageBitmap(helper.drawGroupImage(bitmaps));
    }

    /**
     * 群组头像帮助类
     *
     * @author lzh
     * @see #setImageBitmaps(List)
     */
    private static class GroupImageHelper {
        /**
         * 最多绘制9个图片
         */
        private static final int MAX_SIZE = 9;
        /**
         * 宽高
         */
        private int mWidth, mHeight;
        /**
         * 边距
         */
        private int mMarginLeft, mMarginTop;
        /**
         * 间距
         */
        private int mHorizontalSpace, mVerticalSpace;
        /**
         * 行列数
         */
        private int row, col;

        /**
         * 设置图片间隔
         *
         * @param horizontalSpace 水平间隔
         * @param verticalSpace   垂直间隔
         */
        void setSpace(int horizontalSpace, int verticalSpace) {
            this.mHorizontalSpace = horizontalSpace;
            this.mVerticalSpace = verticalSpace;
        }

        /**
         * 设置群组图片尺寸
         *
         * @param width  宽
         * @param height 高
         */
        void setSize(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        /**
         * 绘制群组图片
         *
         * @param bitmaps 图片集合
         * @return Bitmap
         */
        Bitmap drawGroupImage(List<Bitmap> bitmaps) {
            int size = getSubBitmapSize(bitmaps.size());
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_4444);
            drawBitmaps(new Canvas(bitmap), bitmaps, size, size);
            return bitmap;
        }

        /**
         * 绘制组合图片
         *
         * @param canvas Canvas
         * @param width  int
         * @param height int
         */
        private void drawBitmaps(Canvas canvas, List<Bitmap> bitmaps, int width, int height) {
            if (bitmaps.isEmpty()) {
                return;
            }
            Rect src = new Rect();
            Rect dst = new Rect();
            int size = Math.min(MAX_SIZE, bitmaps.size());// 图片个数
            int columnInFirstRow = size - (row - 1) * col;// 第一行的列数
            for (int i = row - 1; i >= 0; i--) {
                int marginLeft;
                if (i == 0) {// 第一行需要特殊处理
                    if (columnInFirstRow == col) {
                        // 每行个数相同
                        marginLeft = mMarginLeft;
                    } else if (columnInFirstRow == 1) {
                        // 第1行只有1列，水平居中
                        marginLeft = (mWidth - width) >> 1;
                    } else {
                        // 第1行有2列，两张图片水平居中
                        marginLeft = (mWidth - (width << 1) - mHorizontalSpace) >> 1;
                    }
                } else {
                    marginLeft = mMarginLeft;
                }
                int top = mMarginTop + i * (height + mVerticalSpace);
                int column = (i == 0 ? columnInFirstRow - 1 : col - 1);
                for (int j = column; j >= 0; j--) {
                    int left = marginLeft + j * (width + mHorizontalSpace);
                    Bitmap bitmap = bitmaps.get(--size);
                    src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    dst.set(left, top, left + width, top + height);
                    canvas.drawBitmap(bitmap, src, dst, null);
                }
            }
        }

        /**
         * 获取组合图片中每个图片的尺寸
         *
         * @param length 组合图片个数
         * @return 尺寸
         */
        private int getSubBitmapSize(int length) {
            computeLayout(length);
            int subWidth = (mWidth - mHorizontalSpace * (col - 1)) / col;
            int subHeight = (mHeight - mVerticalSpace * (row - 1)) / row;
            // 宽高相等,取较小的值
            int minSize = Math.min(subWidth, subHeight);
            // 将剩余尺寸平均设为边距
            mMarginTop = (mHeight - minSize * row - mVerticalSpace * (row - 1)) >> 1;
            mMarginLeft = (mWidth - minSize * col - mHorizontalSpace * (col - 1)) >> 1;
            return minSize;
        }

        /**
         * 计算行列数（最多3*3）
         *
         * @param length 组合图片个数
         */
        private void computeLayout(int length) {
            if (length > 6) {
                row = 3;
                col = 3;
            } else if (length > 4) {
                row = 2;
                col = 3;
            } else if (length > 2) {
                row = 2;
                col = 2;
            } else if (length == 2) {
                row = 1;
                col = 2;
            } else {
                row = 1;
                col = 1;
            }
        }

    }
}