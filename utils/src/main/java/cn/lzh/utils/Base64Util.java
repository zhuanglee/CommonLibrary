package cn.lzh.utils;

import android.util.Base64;

/**
 * Created by lzh on 2017/2/9 17:44.
 */

public final class Base64Util{

    private Base64Util() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 加密
     * @param text 原文
     * @return 加密后的文本
     */
    public static String encode(String text){
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT);
    }

    /**
     * 解密
     * @param text 密文
     * @return 解密后的文本
     */
    public static String decode(String text){
        return new String(Base64.decode(text, Base64.DEFAULT));
    }

}
