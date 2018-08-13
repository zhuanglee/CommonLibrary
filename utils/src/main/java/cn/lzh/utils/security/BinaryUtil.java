/**
 * Copyright (C) Alibaba Cloud Computing, 2015
 * All rights reserved.
 * <p>
 * 版权所有 （C）阿里巴巴云计算，2015
 */

package cn.lzh.utils.security;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BinaryUtil {

    public static String toBase64String(String text){
        return toBase64String(text.getBytes());
    }

    public static String toBase64String(byte[] binaryData) {
        //    org.apache.commons.codec.binary.Base64
        return Base64.encodeToString(binaryData, Base64.DEFAULT);
    }

    /**
     * 反解base64编码的字符串
     */
    public static byte[] fromBase64String(String base64String) {
        return Base64.decode(base64String.getBytes(), Base64.DEFAULT);
    }

    /**
     * 计算byte数组的Md5
     */
    public static byte[] calculateMd5(byte[] binaryData) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
        messageDigest.update(binaryData);
        return messageDigest.digest();

    }

    /**
     * 计算本地文件的MD5
     */
    public static byte[] calculateMd5(String filePath) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found.");
        }
        byte[] buffer = new byte[4 * 1024];
        InputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
            int lent;
            while ((lent = is.read(buffer)) != -1) {
                digest.update(buffer, 0, lent);
            }
            return digest.digest();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                is.close();
            }
        }
        return null;
    }

    /**
     * 计算byte数组的Md5，返回Md5字符串
     */
    public static String calculateMd5Str(byte[] binaryData) {
        return getMd5StrFromBytes(calculateMd5(binaryData));
    }

    /**
     * 计算本地文件的Md5，返回Md5字符串
     */
    public static String calculateMd5Str(String filePath) throws IOException {
        return getMd5StrFromBytes(calculateMd5(filePath));
    }

    /**
     * 计算byte数组的Md5，返回base64加密后的字符串
     */
    public static String calculateBase64Md5(byte[] binaryData) {
        return toBase64String(calculateMd5(binaryData));
    }

    /**
     * 计算本地文件的Md5，返回base64加密后的字符串
     */
    public static String calculateBase64Md5(String filePath) throws IOException {
        return toBase64String(calculateMd5(filePath));
    }

    /**
     * MD5sum生成的结果转换为字符串
     */
    public static String getMd5StrFromBytes(byte[] md5bytes) {
        if (md5bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < md5bytes.length; i++) {
            sb.append(String.format("%02x", md5bytes[i]));
        }
        return sb.toString();
    }
}
