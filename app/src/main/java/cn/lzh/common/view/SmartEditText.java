package cn.lzh.common.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义文本输入框：
 * 1、可控制输入字符长度的EditText；
 * 2、增加清空按钮；
 * @deprecated 点击软键盘中的下一项不能跳转到下一个编辑框
 * @author lzh
 */
@Deprecated
public class SmartEditText extends AppCompatEditText {

    private Drawable mLeft, mTop, mRight, mBottom;

    private Rect mBounds;

    /**
     * 清空按钮的显示状态（true 显示， false隐藏）
     */
    private boolean mBtnClearVisibility;

    /**
     * 清空编辑框内容的事件监听
     */
    private OnClearListener mOnClearListener;
    private OnClickListener mOnClickListener;

    private int maxInputLength;
    public InputLengthHintListener mInputLengthHintListener;
    public FilterChineseListener mfChineseListener;
    public GetInputLengthListener mGetInputLengthListener;
    private MyWatcher mWatcher;

    public SmartEditText(Context context) {
        super(context);
        init();
    }

    public SmartEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnClickListener(null);// 设置默认单击监听
//        setDrawable();
        // 增加文本监听器.
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
    }

    // 输入框右边的图标显示控制
    private void setDrawable() {
        if (isEnabled() && isFocusable() && length() > 0) {
            mBtnClearVisibility = true;
            super.setCompoundDrawables(mLeft, mTop, mRight, mBottom);
        } else if (mBtnClearVisibility) {
            mBtnClearVisibility = false;
            super.setCompoundDrawables(mLeft, mTop, null, mBottom);
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        this.mLeft = left;
        this.mTop = top;
        this.mBottom = bottom;
        this.mRight = right;
//        super.setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mBtnClearVisibility && mRight != null) {
                    this.mBounds = mRight.getBounds();
                    mRight.getIntrinsicWidth();
                    int eventX = (int) event.getX();
                    int width = mBounds.width();
                    int right = getRight();
                    int left = getLeft();
                    if (eventX > (right - width * 2 - left)) {
                        setText("");
                        event.setAction(MotionEvent.ACTION_CANCEL);
                        if (mOnClearListener != null) {
                            mOnClearListener.onClear();
                        }
                    }
                }
//                this.performClick();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setDrawable();
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        setDrawable();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.mLeft = null;
        this.mTop = null;
        this.mRight = null;
        this.mBottom = null;
        this.mBounds = null;
    }

    @Override
    public void setOnClickListener(@Nullable final OnClickListener listener) {
        mOnClickListener = listener;
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
    }

    public OnClearListener getOnClearListener() {
        return mOnClearListener;
    }

    public void setOnClearListener(OnClearListener listener) {
        this.mOnClearListener = listener;
    }

    public void setOnMaxInputListener(int maxInputLength,
                                      InputLengthHintListener Listener) {
        this.maxInputLength = maxInputLength;
        mInputLengthHintListener = Listener;
        setTextWatcher();
    }

    public void setOnGetInputLengthListener(GetInputLengthListener listener) {
        mGetInputLengthListener = listener;
        setTextWatcher();
    }

    public void setOnFilterChineseListener(FilterChineseListener listener) {
        mfChineseListener = listener;
        setTextWatcher();
    }

    private void setTextWatcher() {
        if (mWatcher == null) {
            mWatcher = new MyWatcher();
        }
        addTextChangedListener(mWatcher);
    }

    public interface OnClearListener {
        void onClear();
    }

    public interface InputLengthHintListener {
        void onOverFlowHint();
    }

    public interface GetInputLengthListener {
        void getInputLength(int length);
    }

    public interface FilterChineseListener {
        void inputChineseHint();
    }

    private class MyWatcher implements TextWatcher {

        private String oldText;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 要有递归出口
            // Logg.w("afterTextChanged:oldText="+oldText);
            String text = s.toString();
            if (text.equals(oldText)) {
                // Logg.w("afterTextChanged:文本并未改变,return");
                setSelection(oldText.length());
                return;
            }
            int textLength = s.length();
            StringBuilder sb = new StringBuilder(text);
            // Logg.w("afterTextChanged:s.length()=" + textLength + ",s=" + s);
            if (textLength > maxInputLength) {
                if (mInputLengthHintListener != null) {
                    mInputLengthHintListener.onOverFlowHint();
                    this.oldText = sb.delete(maxInputLength, s.length())
                            .toString();// 会再次触发文本改变
                    setText(oldText);
                    setSelection(oldText.length());
                }
            }
            if (mGetInputLengthListener != null) {
                mGetInputLengthListener
                        .getInputLength((textLength > maxInputLength) ? maxInputLength
                                : textLength);
            }
            if (mfChineseListener != null) {
                for (int i = 0; i < textLength; i++) {
                    if (isChinese(s.charAt(i))) {
                        mfChineseListener.inputChineseHint();
                        s.delete(i, s.length());
                    }
                }
                this.oldText = s.toString();
            }
        }
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

}