package cn.lzh.utils.security;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author from open source
 */
public class BinaryUtil {

    private static final String MD5 = "MD5";
    private static final String UTF_8 = "utf-8";

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

    /**
     * 计算byte数组的Md5
     * @param bytes 字节数组
     * @return md5字节数组
     */
    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 计算本地文件的MD5
     * @param filePath 文件路径
     * @return md5字节数组
     */
    @Nullable
    public static byte[] md5(@NonNull String filePath) {
        File file = new File(filePath);
        return md5(file);
    }

    /**
     * 计算本地文件的MD5
     * @param file 文件
     * @return md5字节数组
     */
    @Nullable
    public static byte[] md5(File file) {
        try (InputStream is = new FileInputStream(file)) {
            int lent;
            byte[] buffer = new byte[4 * 1024];
            MessageDigest md = MessageDigest.getInstance(MD5);
            while ((lent = is.read(buffer)) != -1) {
                md.update(buffer, 0, lent);
            }
            return md.digest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MD5sum生成的结果转换为字符串
     */
    public static String getMd5StrFromBytes(byte[] md5bytes) {
        if (md5bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte md5byte : md5bytes) {
            sb.append(String.format("%02x", md5byte));
        }
        return sb.toString();
    }
}
