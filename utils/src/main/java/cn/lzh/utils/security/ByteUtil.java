package cn.lzh.utils.security;

import android.support.annotation.NonNull;

/**
 * Created by lzh on 2017/9/18.<br/>
 * 字节数组与十六进制字符串之间的转换工具
 */
public final class ByteUtil {

    private ByteUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return String
     */
    public static String bytes2hex(@NonNull byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String hexStr;
        for (byte b : bytes) {
            hexStr = Integer.toHexString(b & 0XFF);
            if (hexStr.length() == 1)
                sb.append('0');
            sb.append(hexStr);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexStr 十六进制字符串
     * @return byte[]
     */
    public static byte[] hex2bytes(String hexStr) {
        byte[] hexBytes = hexStr.getBytes();
        if ((hexBytes.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] bytes = new byte[hexBytes.length / 2];
        for (int i = 0; i < hexBytes.length; i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(
                    new String(hexBytes, i, 2), 16);
        }
        return bytes;
    }

}
