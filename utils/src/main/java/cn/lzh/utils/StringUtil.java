package cn.lzh.utils;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java判断字符串是否为数字或中文或字母
 * 
 * @author Administrator
 * 
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
			return "号码为空";
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
		return phone.matches("^1[34578]\\d{9}$");
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
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isBlank(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
				.append(Character.toUpperCase(c)).append(str.substring(1)).toString();
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
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException if an error occurs
	 */
	public static String utf8Encode(String str) {
		if (!isBlank(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * encoded in utf-8, if exception, return defaultReturn
	 *
	 * @param str
	 * @param defaultReturn
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
	 * get innerHtml from href
	 *
	 * <pre>
	 * getHrefInnerHtml(null)                                  = ""
	 * getHrefInnerHtml("")                                    = ""
	 * getHrefInnerHtml("mp3")                                 = "mp3";
	 * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
	 * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
	 * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
	 * </pre>
	 *
	 * @param href
	 * @return <ul>
	 *         <li>if href is null, return ""</li>
	 *         <li>if not match regx, return source</li>
	 *         <li>return the last string that match regx</li>
	 *         </ul>
	 */
	public static String getHrefInnerHtml(String href) {
		if (isBlank(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

	/**
	 * 将html中的特殊符号的编码替换为特殊符号字符串：process special char in html
	 *
	 * <pre>
	 * htmlEscapeCharsToString(null) = null;
	 * htmlEscapeCharsToString("") = "";
	 * htmlEscapeCharsToString("mp3") = "mp3";
	 * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
	 * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
	 * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
	 * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
	 * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
	 * </pre>
	 *
	 * @param source 网页内容
	 */
	public static String htmlEscapeCharsToString(String source) {
		return isBlank(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
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
}
