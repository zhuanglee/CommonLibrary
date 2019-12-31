package cn.lzh.utils.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Base64Util
 * @author open source
 */
public class Base64Util {

    private static final String UTF_8 = "utf-8";

    private Base64Util() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    public static String encodeBase64(String text) {
        try {
            return new String(encodeBase64(text.getBytes()), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return Base64.encode(binaryData, Base64.DEFAULT);
    }

    /**
     * 解码base64编码的字符串
     * @param text base64编码的字符串
     * @return 解码后的字符串
     */
    public static String decodeBase64(String text) {
        try {
            return new String(decodeBase64(text.getBytes()), UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
    /**
     * 解码base64编码的字符串
     * @param bytes base64编码的字节数组
     * @return 字节数组
     */
    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.decode(bytes, Base64.DEFAULT);
    }

}
