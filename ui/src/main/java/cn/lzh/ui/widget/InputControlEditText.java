package cn.lzh.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * 可控制输入字符长度的EditText
 * 
 * @author Administrator
 * 
 */
public class InputControlEditText extends AppCompatEditText {

	private int maxInputLength;
	public InputLengthHintListener mInputLengthHintListener;
	public FilterChineseListener mfChineseListener;
	public GetInputLengthListener mGetInputLengthListener;
	private MyWatcher mWatcher;

	public InputControlEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public InputControlEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public InputControlEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
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
			// TODO 要有递归出口
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
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

}
