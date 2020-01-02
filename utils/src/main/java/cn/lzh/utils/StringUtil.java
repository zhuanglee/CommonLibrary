package cn.lzh.utils;


import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关操作
 *
 * @author open source
 * @see #isBlank(String) isBlank
 * @see #isChinese(char) isChinese
 * @see #isChinese(String) isChinese
 * @see #isEmail(String) isEmail
 * @see #isEmpty(CharSequence) isEmpty
 * @see #isImgUrl(String) isImgUrl
 * @see #isNumeric(String) isNumeric
 * @see #isPhone(String) isPhone
 * @see #isUrl(String) isUrl
 * @see #capitalizeFirstLetter(String) capitalizeFirstLetter
 * @see #checkChinese(String) checkChinese
 * @see #checkNickname(String) checkNickname
 * @see #containsChinese(String) containsChinese
 * @see #formatPhone(String) formatPhone
 * @see #fullWidthToHalfWidth(String) fullWidthToHalfWidth
 * @see #getSubString(String, int, int) getSubString
 * @see #utf8Encode(String) utf8Encode
 * @see #utf8Encode(String, String) utf8Encode
 * @see #validateFirstChar(String) validateFirstChar
 * @see #getColorfulText(String, int, int, int) getColorfulText
 */
public class StringUtil {
	private final static Pattern EMAIL_PATTERN = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private final static Pattern IMG_URL = Pattern
			.compile(".*?(gif|jpeg|png|jpg|bmp)");

	private final static Pattern URL = Pattern
			.compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");


	private StringUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 *
	 * @param str String
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		if (str == null || "".equals(str) || str.trim().length() == 0)
			return true;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * is null or its length is 0
	 *
	 * <pre>
	 * isBlank(null) = true;
	 * isBlank(&quot;&quot;) = true;
	 * isBlank(&quot;  &quot;) = false;
	 * </pre>
	 *
	 * @param str CharSequence
	 * @return if string is null or its size is 0, return true, else return false.
	 */
	public static boolean isEmpty(CharSequence str) {
		return (str == null || str.length() == 0);
	}

	/***
	 * 截取字符串
	 *
	 * @param start
	 *            从那里开始，0算起
	 * @param num
	 *            截取多少个
	 * @param str
	 *            截取的字符串
	 */
	public static String getSubString(String str, int start, int num) {
		if (str == null) {
			return "";
		}
		int len = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > len) {
			start = len;
		}
		if (num < 0) {
			num = 1;
		}
		int end = start + num;
		if (end > len) {
			end = len;
		}
		return str.substring(start, end);
	}

	/**
	 * 检测字符串中只能包含：中文、数字、下划线(_)、横线(-)
	 * @param str String
	 */
	public static boolean checkNickname(String str) {
		final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(str);
		return !matcher.find();
	}

	/**
	 * 格式化手机号码:去除" "和"-",以及"+86"
	 *
	 * @param phone String
	 */
	public static String formatPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return "";
		}
		if (phone.contains("-"))
			phone = phone.replace("-", "");
		if (phone.contains(" "))
			phone = phone.replace(" ", "");
		if (phone.startsWith("+86")) {
			phone = phone.replace("+86", "");
		}
		return phone;
	}

	/**
	 * 验证手机号码格式
	 *
	 * @param phone String
	 */
	public static boolean isPhone(@NonNull String phone) {
		return phone.matches("^1[3-9]\\d{9}$");
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 *
	 * @param email String
	 */
	public static boolean isEmail(@NonNull String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	/**
	 * 判断一个url是否为图片url
	 *
	 * @param url String
	 */
	public static boolean isImgUrl(@NonNull String url) {
		return IMG_URL.matcher(url).matches();
	}

	/**
	 * 判断是否为一个合法的url地址
	 *
	 * @param str String
	 */
	public static boolean isUrl(@NonNull String str) {
		return URL.matcher(str).matches();
	}

	/**
	 * 判断字符串是否仅为数字:
	 * 1、用正则表达式；
	 * 2、用{@link Character#isDigit(char)}判断；
	 * 3、用数字的ASCII码值的范围判断；
	 * @param str String
	 */
	public static boolean isNumeric(String str) {
		return Pattern.compile("[0-9]*").matcher(str).matches();
	}

	/**
	 * 判断一个字符串的首字符是否为字母
	 * 
	 * @param s String
	 */
	public static boolean validateFirstChar(String s) {
		char c = s.charAt(0);
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
	}

	/**
	 * 检查是否包含汉字
	 * @param str String
	 */
	public static boolean containsChinese(String str) {
		final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * 检测String是否全是中文
	 *
	 * @param str String
	 */
	public static boolean checkChinese(String str) {
		boolean res = true;
		char[] cTemp = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if (!isChinese(cTemp[i])) {
				res = false;
				break;
			}
		}
		return res;
	}

	/**
	 * 判断是否为汉字
	 *
	 * @param c char
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
	}

	/**
	 * 判断是否为汉字
	 * 
	 * @param str String
	 */
	public static boolean isChinese(String str) {
		char[] chars = str.toCharArray();
		boolean isGB2312 = false;
		for (char c : chars) {
			byte[] bytes = String.valueOf(c).getBytes();
			if (bytes.length == 2) {
				int[] ints = new int[2];
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isGB2312 = true;
					break;
				}
			}
		}
		return isGB2312;
	}

	/**
	 * capitalize first letter
	 *
	 * <pre>
	 * capitalizeFirstLetter(null)     =   null;
	 * capitalizeFirstLetter("")       =   "";
	 * capitalizeFirstLetter("2ab")    =   "2ab"
	 * capitalizeFirstLetter("a")      =   "A"
	 * capitalizeFirstLetter("ab")     =   "Ab"
	 * capitalizeFirstLetter("Abc")    =   "Abc"
	 * </pre>
	 *
	 * @param str String
	 * @return 首字母大写
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isBlank(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : Character.toUpperCase(c) + str.substring(1);
	}

	/**
	 * encoded in utf-8
	 *
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 *
	 * @param str String
	 */
	public static String utf8Encode(String str) {
		return utf8Encode(str, str);
	}

	/**
	 * encoded in utf-8, if exception, return defaultReturn
	 *
	 * @param str 待编码文本内容
	 * @param defaultReturn 默认值
	 * @return
	 */
	public static String utf8Encode(String str, String defaultReturn) {
		if (!isBlank(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return defaultReturn;
			}
		}
		return str;
	}

	/**
	 * 全角转半角：transform half width char to full width char
	 *
	 * <pre>
	 * fullWidthToHalfWidth(null) = null;
	 * fullWidthToHalfWidth("") = "";
	 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
	 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
	 * </pre>
	 *
	 * @param s String
	 */
	public static String fullWidthToHalfWidth(String s) {
		if (isBlank(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char)(source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * 半角转全角：transform full width char to half width char
	 *
	 * <pre>
	 * halfWidthToFullWidth(null) = null;
	 * halfWidthToFullWidth("") = "";
	 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
	 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
	 * </pre>
	 *
	 * @param s String
	 */
	public static String halfWidthToFullWidth(String s) {
		if (isBlank(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char)12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			} else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char)(source[i] + 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}


	/**
	 * 获取颜色丰富的文本
	 * @param text 原文本
	 * @param start 开始位置
	 * @param end 结束为止
	 * @param color 特殊文本（start-end之间的文本）的颜色
	 */
	public static SpannableString getColorfulText(String text, int start, int end, @ColorInt int color){
		if(start > end){
			throw new IllegalArgumentException("start > end");
		}
		if (end > text.length()) {
			end = text.length();
		}
		SpannableString colorfulText = new SpannableString(text);
		colorfulText.setSpan(new ForegroundColorSpan(color),
				start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return colorfulText;
	}

}
