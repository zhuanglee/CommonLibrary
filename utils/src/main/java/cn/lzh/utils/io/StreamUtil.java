package cn.lzh.utils.io;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * Created by lzh on 2017/7/17.<br/>
 * 常见的流操作
 */
public final class StreamUtil {

    private final static int BUFFER_SIZE = 4096;

    private StreamUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }


    /**
     * 读取字符串
     * @param in InputStream
     * @return String
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
     * @param in InputStream
     * @return byte[]
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

    public static String readStreamAsString(InputStream in, String charset)
            throws IOException {
        if (in == null)
            return "";

        Reader reader = null;
        Writer writer = new StringWriter();
        String result;

        char[] buffer = new char[1024];
        try{
            reader = new BufferedReader(
                    new InputStreamReader(in, charset));

            int n;
            while((n = reader.read(buffer)) > 0){
                writer.write(buffer, 0, n);
            }

            result = writer.toString();
        } finally {
            in.close();
            if (reader != null){
                reader.close();
            }
            writer.close();
        }

        return result;
    }

    public static byte[] readStreamAsBytesArray(InputStream in)
            throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        while ((len = in.read(buffer)) > -1) {
            output.write(buffer, 0, len);
        }
        output.flush();
        return output.toByteArray();
    }

    public static byte[] readStreamAsBytesArray(InputStream in, int readLength)
            throws IOException {
        if (in == null) {
            return new byte[0];
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        long readed = 0;
        while (readed < readLength && (len = in.read(buffer, 0, Math.min(2048, (int)(readLength - readed)))) > -1) {
            output.write(buffer, 0, len);
            readed += len;
        }
        output.flush();
        return output.toByteArray();
    }

    public static void safeClose(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) { }
        }
    }

    public static void readStreamToFile(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        int len;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }

    public static void safeClose(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {}
        }
    }
}
