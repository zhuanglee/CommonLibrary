package cn.lzh.ui.helper;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 软键盘帮助类：软键盘弹出时，调整布局
 * @author from open source
 * @see #assistActivity(Activity)
 * @see #setSoftKeyBoardStatusListener(SoftKeyBoardStatusListener)
 */
public class SoftKeyBoardHelper {
    /**
     * 软键盘状态监听
     */
    public interface SoftKeyBoardStatusListener {
        /**
         * 状态改变时调用
         * @param isShow 是否显示
         */
        void onChange(boolean isShow);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private SoftKeyBoardStatusListener mSoftKeyBoardStatusListener;

    private SoftKeyBoardHelper(Activity activity) {
        //找到DecorView
        FrameLayout content = activity.findViewById(android.R.id.content);
        //获取到用户设置的View
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(this::possiblyResizeChildOfContent);


        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }


    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyBoard = mChildOfContent.getRootView().getHeight();
            //Activity中xml布局的高度
            int heightDifference = usableHeightSansKeyBoard - usableHeightNow;
            if (heightDifference > 100 /*(usableHeightSansKeyBoard / 4)*/) {
                frameLayoutParams.height = usableHeightSansKeyBoard - heightDifference;
            } else {
                frameLayoutParams.height = usableHeightSansKeyBoard;
            }
            mChildOfContent.requestLayout();
            if(mSoftKeyBoardStatusListener != null){
                mSoftKeyBoardStatusListener.onChange(usableHeightPrevious < usableHeightNow);
            }
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect rect = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(rect);
        //全屏时，直接返回rect.bottom,rect.top是状态栏的高度
//        return (rect.bottom - rect.top);
        return rect.bottom;
    }

    public void setSoftKeyBoardStatusListener(SoftKeyBoardStatusListener listener){
        this.mSoftKeyBoardStatusListener = listener;
    }

    public static SoftKeyBoardHelper assistActivity(Activity activity) {
        return new SoftKeyBoardHelper(activity);
    }
}
