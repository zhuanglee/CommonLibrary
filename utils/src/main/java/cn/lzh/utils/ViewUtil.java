package cn.lzh.utils;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lzh on 2018/12/25.<br/>
 * @see #getText(EditText)
 * @see #getText(EditText, String)
 * @see #getTextWithoutHint(EditText)
 * @see #isEmpty(EditText)
 * @see #showUnderline(TextView)
 * @see #showUnderline(TextView, boolean)
 */
public class ViewUtil {

    /**
     * 判断编辑框内容是否为空
     * @param et 编辑框
     */
    public static boolean isEmpty(EditText et) {
        return et.getText().toString().trim().isEmpty();
    }

    /**
     * 获取编辑框的文本内容，为空时抛出异常
     * @param et 编辑框
     * @return 编辑框的文本内容
     */
    public static String getText(EditText et){
        CharSequence hint = et.getHint();
        return getText(et, hint == null ? null :hint.toString());
    }

    /**
     * 获取编辑框的文本内容，为空时抛出异常，但是没有异常提示信息
     * @param et 编辑框
     * @return 编辑框的文本内容
     */
    public static String getTextWithoutHint(EditText et){
        return getText(et, null);
    }

    /**
     * 获取编辑框的文本内容，为空时抛出异常
     * @param et 编辑框
     * @param hint 内容为空时的提示信息
     * @return 编辑框的文本内容
     */
    public static String getText(EditText et, String hint){
        String text = et.getText().toString().trim();
        if(text.isEmpty()){
            et.requestFocus();
            AnimationUtil.shakeView(et);
            throw new AssertionError(hint == null ? "" : hint);
        }
        return text;
    }

    /**
     * 显示下划线
     * @param tv TextView
     */
    public static void showUnderline(TextView tv) {
        showUnderline(tv, true);
    }

    /**
     * 显示下划线
     * @param tv TextView
     */
    public static void showUnderline(TextView tv, boolean flag) {
        tv.getPaint().setUnderlineText(flag);
    }
}
