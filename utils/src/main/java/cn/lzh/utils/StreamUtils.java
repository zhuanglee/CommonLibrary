package cn.lzh.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by lzh on 2017/7/17.
 */

public class StreamUtils {
    public static final String LOG_TAG = "StreamUtils";

    /**
     * 读取输入流数据
     *
     * @param inStream
     * @return
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    public static String readStream(InputStream in) throws IOException {
        BufferedReader br = null;
        StringWriter sw = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            sw = new StringWriter();
            String str;
            while ((str = br.readLine()) != null) {
                sw.write(str);
            }
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sw != null) {
                sw.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return "";
    }


    public static String getString(InputStream is) throws IOException {
        String result = "";
        if (is != null) {
            BufferedInputStream bis = null;
            ByteArrayOutputStream baos = null;
            try {
                bis = new BufferedInputStream(is);
                baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int read = -1;
                while ((read = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                result = baos.toString("utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    bis.close();
                }
                if (baos != null) {
                    baos.close();
                }
            }
        }
        return result;
    }

}
