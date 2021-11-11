package cn.lzh.utils.io;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
     * @param in InputStream
     * @return List<String>
     */
    @NonNull
    public static List<String> getStrings(InputStream in) {
        List<String> fileContent = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContent;
    }

    /**
     * 读取字符串
     * @param in InputStream
     * @return String
     */
    @NonNull
    public static String readString(@NonNull InputStream in){
        StringBuilder builder = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    /**
     * 读取字节数组
     * @param in InputStream
     * @param bufferSize 缓冲区大小
     * @return byte[]
     */
    @Nullable
    public static byte[] readByteArray(@NonNull InputStream in, int bufferSize){
        if(bufferSize <= 0){
            bufferSize = 2048;
        }
        BufferedInputStream bin = null;
        ByteArrayOutputStream bout = null;
        try {
            bin = new BufferedInputStream(in);
            bout = new ByteArrayOutputStream();
            byte[] buffer = new byte[bufferSize];
            int read;
            while ((read = bin.read(buffer)) != -1) {
                bout.write(buffer, 0, read);
            }
            return bout.toByteArray();
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
        return null;
    }

}
