package cn.lzh.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

public class WatermarkImageView extends androidx.appcompat.widget.AppCompatImageView {

    /**
     * 矩形最小宽高
     */
    private static final int MIN_RECT_SIZE = 100;
    private final List<Rect> rects = new ArrayList<>();
    private final Paint paint;
    private int normalColor = Color.GRAY;
    private int selectedColor = Color.RED;
    private Rect selectedRect;
    private Rect copySelectedRect;
    private Point touchPoint;
    private TouchType touchType;

    public WatermarkImageView(Context context) {
        this(context, null);
    }

    public WatermarkImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatermarkImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(normalColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Rect rect : rects) {
            paint.setColor(rect.equals(selectedRect) ? selectedColor : normalColor);
            paint.setStyle(rect.equals(selectedRect) ? Paint.Style.FILL_AND_STROKE : Paint.Style.FILL);
            canvas.drawRect(rect, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void handleActionDown(MotionEvent event) {
        touchPoint = new Point((int) event.getX(), (int) event.getY());
        selectedRect = getSelectedRect(touchPoint.x, touchPoint.y);
        touchType = getTouchType(selectedRect, touchPoint);
        if (selectedRect == null) {
            copySelectedRect = null;
        } else {
            copySelectedRect = new Rect(selectedRect.left, selectedRect.top, selectedRect.right, selectedRect.bottom);
        }
        invalidate();
    }

    private void handleActionMove(MotionEvent event) {
        int dx = (int) event.getX() - touchPoint.x;
        int dy = (int) event.getY() - touchPoint.y;
        TouchType type = getTouchType(selectedRect, new Point((int) event.getX(), (int) event.getY()));
        if(!type.equals(touchType)){
            // 移动过程中 TouchType 发生变化, 说明滑动过快，忽略
            return;
        }
        // 根据触摸位置决定移动还是改变大小
        switch (type) {
            case CENTER:
                resetSelectedRect(
                        copySelectedRect.left + dx,
                        copySelectedRect.top + dy,
                        copySelectedRect.right + dx,
                        copySelectedRect.bottom + dy
                );
                break;
            case LEFT:
                resetSelectedRect(
                        copySelectedRect.left + dx,
                        copySelectedRect.top,
                        copySelectedRect.right,
                        copySelectedRect.bottom
                );
                break;
            case TOP:
                resetSelectedRect(
                        copySelectedRect.left,
                        copySelectedRect.top + dy,
                        copySelectedRect.right,
                        copySelectedRect.bottom
                );
                break;
            case RIGHT:
                resetSelectedRect(
                        copySelectedRect.left,
                        copySelectedRect.top,
                        copySelectedRect.right + dx,
                        copySelectedRect.bottom
                );
                break;
            case BOTTOM:
                resetSelectedRect(
                        copySelectedRect.left,
                        copySelectedRect.top,
                        copySelectedRect.right,
                        copySelectedRect.bottom + dy
                );
                break;
            case LEFT_TOP:
                resetSelectedRect(
                        copySelectedRect.left + dx,
                        copySelectedRect.top + dy,
                        copySelectedRect.right,
                        copySelectedRect.bottom
                );
                break;
            case LEFT_BOTTOM:
                resetSelectedRect(
                        copySelectedRect.left + dx,
                        copySelectedRect.top,
                        copySelectedRect.right,
                        copySelectedRect.bottom + dy
                );
                break;
            case RIGHT_TOP:
                resetSelectedRect(
                        copySelectedRect.left,
                        copySelectedRect.top + dy,
                        copySelectedRect.right + dx,
                        copySelectedRect.bottom
                );
                break;
            case RIGHT_BOTTOM:
                resetSelectedRect(
                        copySelectedRect.left,
                        copySelectedRect.top,
                        copySelectedRect.right + dx,
                        copySelectedRect.bottom + dy
                );
                break;
            default:
                // do nothing
                break;
        }
        invalidate();
    }

    /**
     * 重设矩形范围：可以限制矩形不超出控件范围
     */
    private void resetSelectedRect(int left, int top, int right, int bottom) {
        // 限制矩形最小宽高
        if (right - left <= MIN_RECT_SIZE || bottom - top <= MIN_RECT_SIZE) {
            return;
        }
        Rect rect = selectedRect;
        if (left < 0) {
            right = rect.width();
            left = 0;
        }
        if (top < 0) {
            bottom = rect.height();
            top = 0;
        }
        if (right > getWidth()) {
            left = getWidth() - rect.width();
            right = getWidth();
        }
        if (bottom > getHeight()) {
            top = getHeight() - rect.height();
            bottom = getHeight();
        }
        // 限制矩形最小宽高
        if (right - left <= MIN_RECT_SIZE || bottom - top <= MIN_RECT_SIZE) {
            return;
        }
        rect.set(
                left,
                top,
                right,
                bottom
        );
    }

    /**
     * 获取触摸类型
     *
     * @param selectedRect 选择的区域
     * @param point        当前触摸的坐标
     */
    private TouchType getTouchType(Rect selectedRect, Point point) {
        TouchType type = TouchType.NONE;
        if (selectedRect != null) {
            // 将选中区域宽高等分为 4 块
            int w = selectedRect.width() / 4;
            int h = selectedRect.height() / 4;
            int left = selectedRect.left + w;
            int top = selectedRect.top + h;
            int right = selectedRect.right - w;
            int bottom = selectedRect.bottom - h;
            if (point.x <= left) {
                // 左侧
                if (point.y <= top) {
                    type = TouchType.LEFT_TOP;
                } else if (point.y >= bottom) {
                    type = TouchType.LEFT_BOTTOM;
                } else {
                    type = TouchType.LEFT;
                }
            } else if (point.x >= right) {
                // 右侧
                if (point.y <= top) {
                    type = TouchType.RIGHT_TOP;
                } else if (point.y >= bottom) {
                    type = TouchType.RIGHT_BOTTOM;
                } else {
                    type = TouchType.RIGHT;
                }
            } else {
                // 中间区域
                if (point.y <= top) {
                    type = TouchType.TOP;
                } else if (point.y >= bottom) {
                    type = TouchType.BOTTOM;
                } else {
                    type = TouchType.CENTER;
                }
            }
        }
        return type;
    }

    @Nullable
    private Rect getSelectedRect(int x, int y) {
        // 倒序遍历，优先选择后添加的矩形
        for (int i = rects.size() - 1; i >= 0; i--) {
            if (rects.get(i).contains(x, y)) {
                return rects.get(i);
            }
        }
        return null;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (rects != null) {
            rects.clear();
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (rects != null) {
            rects.clear();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (rects != null) {
            rects.clear();
        }
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        if (rects != null) {
            rects.clear();
        }
    }

    @UiThread
    public void addRect() {
        int w = getWidth() / 4;
        int h = getHeight() / 4;
        int left = getWidth() / 2 + w / 2;
        int top = getHeight() / 2 + h / 2;
        rects.add(new Rect(left, top, left + w, top + h));
        invalidate();
    }

    public void setNormalColor(@ColorInt int normalColor) {
        this.normalColor = normalColor;
        invalidate();
    }

    public void setSelectedColor(@ColorInt int selectedColor) {
        this.selectedColor = selectedColor;
        invalidate();
    }

    private enum TouchType {
        NONE, CENTER, LEFT, TOP, RIGHT, BOTTOM, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

}
