package cn.lzh.utils.io;

import android.support.annotation.NonNull;

/**
 * Created by lzh on 2017/9/18.<br/>
 */

public final class ByteUtil {

    private ByteUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes
     * @return
     */
    public static String toHexString(@NonNull byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String hexStr;
        for (int i = 0; i < bytes.length; i++) {
            hexStr = Integer.toHexString(bytes[i] & 0XFF);
            if (hexStr.length() == 1)
                sb.append('0');
            sb.append(hexStr);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexStr
     * @return
     */
    public static byte[] hex2byte(String hexStr) {
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
