package cn.lzh.utils.io;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by lzh on 2017/7/17.<br/>
 * 常见的流操作
 */
public final class StreamUtil {

    private StreamUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }


    /**
     * 读取字符串
     * @param in
     * @return
     */
    @Nullable
    public static String readString(@NonNull InputStream in){
        byte[] data = readByteArray(in);
        try {
            return data == null ? null : new String(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取字节数组
     * @param in
     * @return
     */
    @Nullable
    public static byte[] readByteArray(@NonNull InputStream in){
        byte[] result = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream bout = null;
        try {
            bin = new BufferedInputStream(in);
            bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int read = -1;
            while ((read = bin.read(buffer)) != -1) {
                bout.write(buffer, 0, read);
            }
            result = bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin != null) {
                    bin.close();
                }
                if (bout != null) {
                    bout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
