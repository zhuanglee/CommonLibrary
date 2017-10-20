package cn.lzh.utils.io.security;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import cn.lzh.utils.io.StreamUtil;

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

    /**
     * 对文件进行Base64编码
     * @param filePath 待编码的文件路径
     * @return
     */
    public static String encodeFile(@NonNull String filePath) throws FileNotFoundException {
        byte[] data = StreamUtil.readByteArray(new FileInputStream(filePath));
        if(data == null){
            return null;
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    /**
     * 将Base64字符串解码为文件
     * @param base64Str
     * @param filePath 解码后的文件路径
     * @return
     */
    public static boolean decodeToFile(@NonNull String base64Str, @NonNull String filePath) {
        try {
            byte[] b = Base64.decode(base64Str, Base64.DEFAULT);
            for (int i = 0; i < b.length; ++i)
                if (b[i] < 0) {
                    int tmp36_34 = i;
                    byte[] tmp36_33 = b;
                    tmp36_33[tmp36_34] = (byte) (tmp36_33[tmp36_34] + 256);
                }
            OutputStream out = new FileOutputStream(filePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

}
