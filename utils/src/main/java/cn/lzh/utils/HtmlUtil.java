package cn.lzh.utils;

import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML标签的相关操作
 * @author from open source
 * @see #formatHtml(String, String...) formatHtml
 * @see #delTag(String) delTag
 * @see #filterHtml(String) filterHtml
 * @see #filterHtmlTag(String, String) filterHtmlTag
 * @see #hasSpecialChars(String) hasSpecialChars
 * @see #htmlEncode(String) htmlEncode
 * @see #replaceHtmlTag(String, String, String, String, String) replaceHtmlTag
 */
public class HtmlUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

	private HtmlUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

    /**
     * 使用HTML标签格式化文本
     *
     * @param format 带有占位符的文本
     * @param args 需要增强显示的文本
     * @return Spanned
     */
    public static Spanned formatHtml(String format, String... args) {
        String font = "<font color='#ff0000'>%s</font>";
        Object[] formatArray = new Object[args.length];
        for (int i = 0; i < formatArray.length; i++) {
            formatArray[i] = String.format(font, args[i]);
        }
        return Html.fromHtml(String.format(format, formatArray));
    }

	/**
	 * 删除所有标签
	 * @param html HTML内容
	 * @return 网页文本内容
	 */
	public static String delTag(String html) {
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(html);
		html = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(html);
		html = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(html);
		html = m_html.replaceAll(""); // 过滤html标签

		return html.trim(); // 返回文本字符串
	}
	
	/**
	 * 基本功能：替换标记以正常显示
	 * @param html HTML内容
	 * @return String
	 * @see android.text.TextUtils#htmlEncode(String)
	 */
	public static String htmlEncode(@NonNull String html) {
		int length = html.length();
		StringBuilder sb = new StringBuilder(length);
		char c;
		for (int i = 0; i <= length - 1; i++) {
			c = html.charAt(i);
			switch (c) {
				case '<':
					sb.append("&lt;"); //$NON-NLS-1$
					break;
				case '>':
					sb.append("&gt;"); //$NON-NLS-1$
					break;
				case '&':
					sb.append("&amp;"); //$NON-NLS-1$
					break;
				case '\'':
					//http://www.w3.org/TR/xhtml1
					// The named character reference &apos; (the apostrophe, U+0027) was introduced in
					// XML 1.0 but does not appear in HTML. Authors should therefore use &#39; instead
					// of &apos; to work as expected in HTML 4 user agents.
					sb.append("&#39;"); //$NON-NLS-1$
					break;
				case '"':
					sb.append("&quot;"); //$NON-NLS-1$
					break;
				default:
					sb.append(c);
			}

		}
		return (sb.toString());
	}

	/**
	 * 基本功能：判断标记是否存在
	 * @param html HTML内容
	 * @return boolean
	 */
	public static boolean hasSpecialChars(String html) {
		boolean flag = false;
		if ((html != null) && (html.length() > 0)) {
			char c;
			for (int i = 0; i <= html.length() - 1; i++) {
				c = html.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 基本功能：过滤所有以"<"开头，以">"结尾的标签
	 * @param html HTML内容
	 * @return String
	 */
	public static String filterHtml(String html) {
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(html);
		StringBuffer sb = new StringBuffer();
		boolean result = matcher.find();
		while (result) {
			matcher.appendReplacement(sb, "");
			result = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 基本功能：过滤指定标签
	 * @param html HTML内容
	 * @param tag 指定标签
	 * @return String
	 */
	public static String filterHtmlTag(String html, String tag) {
		String regex = "<\\s*" + tag + "\\s+([^>]*)\\s*>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 基本功能：替换指定的标签
	 * exp：替换img标签的src属性值为[img]属性值[/img]
	 * @param html HTML内容
	 * @param beforeTag 要替换的标签
	 * @param tagAttr 要替换的标签属性值
	 * @param startTag 新标签开始标记
	 * @param endTag 新标签结束标记
	 * @return String
	 */
	public static String replaceHtmlTag(String html, String beforeTag,
			String tagAttr, String startTag, String endTag) {
		String regForTag = "<\\s*" + beforeTag + "\\s+([^>]*)\\s*>";
		String regForTagAttr = tagAttr + "=\"([^\"]+)\"";
		Pattern patternForTag = Pattern.compile(regForTag);
		Pattern patternForAttr = Pattern.compile(regForTagAttr);
		Matcher matcherForTag = patternForTag.matcher(html);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		while (result) {
			StringBuffer sbReplace = new StringBuffer();
			Matcher matcherForAttr = patternForAttr.matcher(matcherForTag
					.group(1));
			if (matcherForAttr.find()) {
				matcherForAttr.appendReplacement(sbReplace, startTag
						+ matcherForAttr.group(1) + endTag);
			}
			matcherForTag.appendReplacement(sb, sbReplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString();
	}


}
