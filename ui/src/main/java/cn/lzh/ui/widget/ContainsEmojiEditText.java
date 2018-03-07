package cn.lzh.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Toast;

import cn.lzh.ui.R;


/**
 * @Description EditText限制输入表情符号
 * Created by admin on 2017/5/25.
 */

public class ContainsEmojiEditText extends AppCompatEditText {

    private boolean emojiEnable = true; // 是否允许输入表情符号
    // 输入表情前的光标位置
    private int cursorPos;
    // 输入表情前EditText中的文本
    private String inputAfterText = "";
    // 是否重置了EditText的内容
    private boolean resetText = false;
    private Context mContext;

    public boolean isEmojiEnable() {
        return emojiEnable;
    }

    public void setEmojiEnable(boolean emojiEnable) {
        this.emojiEnable = emojiEnable;
    }

    public ContainsEmojiEditText(Context context) {
        super(context);
        this.mContext = context;
        initEditText();
    }

    public ContainsEmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContainsEmojiEditText);
        // 获取自定义属性值
        emojiEnable = ta.getBoolean(R.styleable.ContainsEmojiEditText_emojiEnable, true);
        initEditText();
    }

    public ContainsEmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContainsEmojiEditText);
        // 获取自定义属性值
        emojiEnable = ta.getBoolean(R.styleable.ContainsEmojiEditText_emojiEnable, true);
        initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {
        if (emojiEnable) {
            return;
        }
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!resetText) {
                    cursorPos = getSelectionEnd();
                    // 这里用s.toString()而不直接用s是因为如果用s，
                    // 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
                    // inputAfterText也就改变了，那么表情过滤就失败了
                    inputAfterText = s.toString();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (resetText) {
                    resetText = false;
                } else {
                    if (count >= 2 && s.length() - inputAfterText.length() > 0) {// 表情符号的字符长度最小为2,
                        CharSequence input = s.subSequence(cursorPos, cursorPos + count);
                        if (containsEmoji(input.toString())) {
                            resetText = true;
                            Toast.makeText(mContext, "不支持输入表情符号！", Toast.LENGTH_SHORT).show();
                            // 是表情符号就将文本还原为输入表情符号之前的内容
                            setText(inputAfterText);
                            CharSequence text = getText();
                            if (text instanceof Spannable) {
                                Spannable spanText = (Spannable) text;
                                Selection.setSelection(spanText, text.length());
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
