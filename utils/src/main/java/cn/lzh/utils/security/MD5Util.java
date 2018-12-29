package cn.lzh.utils.security;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.lzh.utils.io.CloseableUtils;

/**
 * Created by lzh on 2018/12/29.<br/>
 */
public class MD5Util {

    private static final String MD5 = "MD5";

    private MD5Util() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 计算文本的Md5
     * @param text 文本
     * @return md5值
     */
    public static String getMd5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(text.getBytes());
            return convertMd5BytesToStr(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }
    /**
     * 计算byte数组的Md5
     * @param bytes 字节数组
     * @return md5值
     */
    public static String getMd5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(bytes);
            return convertMd5BytesToStr(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * 计算本地文件的MD5
     * @param filePath 文件路径
     * @return md5值
     */
    @Nullable
    public static String getFileMd5(@NonNull String filePath) {
        return getFileMd5(new File(filePath));
    }

    /**
     * 计算本地文件的MD5
     * @param file 文件
     * @return md5值
     */
    @Nullable
    public static String getFileMd5(File file) {
        FileInputStream is = null;
        FileChannel channel = null;
        try {
            is = new FileInputStream(file);
            channel = is.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
            MessageDigest md5 = MessageDigest.getInstance(MD5);
            md5.update(buffer);
            return convertMd5BytesToStr(md5.digest());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(channel);
            CloseableUtils.close(is);
        }
        return null;
    }

    /**
     * 计算本地文件的MD5
     * @param filePath 文件路径
     * @return md5值
     */
    @Nullable
    public static String getBigFileMd5(@NonNull String filePath) {
        return getBigFileMd5(new File(filePath));
    }

    /**
     * 计算本地文件的MD5
     * @param file 文件
     * @return md5值
     */
    @Nullable
    public static String getBigFileMd5(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            int lent;
            byte[] buffer = new byte[4 * 1024];
            MessageDigest md = MessageDigest.getInstance(MD5);
            while ((lent = is.read(buffer)) != -1) {
                md.update(buffer, 0, lent);
            }
            return convertMd5BytesToStr(md.digest());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(is);
        }
        return null;
    }

    /**
     * 将MD5生成的字节数组转换为字符串
     */
    private static String convertMd5BytesToStr(byte[] md5bytes) {
        if (md5bytes == null || md5bytes.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte md5byte : md5bytes) {
            sb.append(String.format("%02x", md5byte));
        }
        return sb.toString();
    }
}
