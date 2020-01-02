package cn.lzh.utils.io;

import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * File Utils
 *@author open source
 * <ul>
 * Read or write file
 * <li>{@link #readFile(String, String)} read file</li>
 * <li>{@link #readLines(String, String)} read file to string list</li>
 * <li>{@link #writeFile(String, String, boolean)} write file from String</li>
 * <li>{@link #writeFile(String, List, boolean)} write file from String List</li>
 * <li>{@link #writeFile(String, InputStream, boolean)} write file</li>
 * <li>{@link #writeFile(File, InputStream, boolean)} write file</li>
 * </ul>
 * <ul>
 * Operate file
 * <li>{@link #moveFile(File, File)} or {@link #moveFile(String, String)}</li>
 * <li>{@link #copyFile(String, String)}</li>
 * <li>{@link #hasExternalStorage()}</li>
 * <li>{@link #isExists(String)}</li>
 * <li>{@link #delete(String)}</li>
 * <li>{@link #getFileSize(File)}</li>
 * <li>{@link #getFileSize(Context, File)}</li>
 * <li>{@link #getTotalCacheSize(Context)}</li>
 * <li>{@link #sortFiles(File[])}</li>
 * </ul>
 * </ul>
 */
public final class FileUtil {

    private FileUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath file path
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    @Nullable
    public static List<String> readLines(String filePath, String charsetName) {
        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            return null;
        }
        List<String> lines = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            CloseableUtils.close(reader);
        }
    }

    /**
     * read file
     *
     * @param filePath String
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    @Nullable
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            return null;
        }
        StringBuilder fileContent = new StringBuilder();
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            CloseableUtils.close(reader);
        }
    }

    /**
     * write file
     *
     * @param filePath 文件路径
     * @param content 文件内容
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        FileWriter fileWriter = null;
        try {
            mkdirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            CloseableUtils.close(fileWriter);
        }
    }

    /**
     * write file
     *
     * @param filePath 文件路径
     * @param lines 多行文本
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> lines, boolean append) {
        if (lines == null || lines.isEmpty()) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            mkdirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : lines) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            CloseableUtils.close(fileWriter);
        }
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(@NonNull String filePath, @NonNull InputStream stream, boolean append) {
        return writeFile(new File(filePath), stream, append);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(@NonNull File file, @NonNull InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            mkdirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte[] data = new byte[2048];
            int length;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            CloseableUtils.close(o);
            CloseableUtils.close(stream);
        }
    }

    /**
     * move file
     *
     * @param srcFilePath 源文件路径
     * @param destFilePath 目标文件路径
     */
    public static void moveFile(@NonNull String srcFilePath, @NonNull String destFilePath) throws IOException {
        moveFile(new File(srcFilePath), new File(destFilePath));
    }

    /**
     * move file
     *
     * @param srcFile 源文件
     * @param destFile 目标文件
     */
    public static void moveFile(@NonNull File srcFile, @NonNull File destFile) throws IOException {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            delete(srcFile.getAbsolutePath());
        }
    }

    /**
     * Copies one file into the other with the given paths.
     * In the event that the paths are the same, trying to copy one file to the other
     * will cause both files to become null.
     * Simply skipping this step if the paths are identical.
     */
    public static void copyFile(@NonNull String pathFrom, @NonNull String pathTo) throws IOException {
        if (pathFrom.equalsIgnoreCase(pathTo)) {
            return;
        }
        try (FileChannel outputChannel = new FileOutputStream(new File(pathTo)).getChannel();
             FileChannel inputChannel = new FileInputStream(new File(pathFrom)).getChannel()) {
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        }
    }

    /**
     * 是否有外部存储
     */
    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 必须是完整路径
     * @return boolean
     */
    public static boolean isExists(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }

    /**
     * 创建目录
     *
     * @param path 目录路径
     */
    public static boolean mkdirs(String path) {
        if (path == null) {
            return false;
        }
        File dir = new File(path);
        return dir.isDirectory() && (dir.exists() || dir.mkdirs());
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        return file.isFile() && (file.exists() || file.createNewFile()) ? file : null;
    }

    /**
     * 删除文件或文件夹
     *
     * @param path 文件或文件夹的路径
     * @return 是否删除
     */
    public static boolean delete(String path) {
        return !TextUtils.isEmpty(path) && delete(new File(path));
    }

    /**
     * 删除文件或文件夹
     *
     * @param file File
     * @return 是否删除
     */
    public static boolean delete(File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    boolean success = delete(f);
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 获取总缓存大小（内部缓存+外部缓存）
     *
     * @param context Context
     * @return 字节数
     */
    public static long getTotalCacheSize(Context context) {
        long cacheSize = getFileSize(context.getCacheDir());
        if (hasExternalStorage()) {
            File dir = context.getExternalCacheDir();
            if (dir != null) {
                cacheSize += getFileSize(dir);
            }
        }
        return cacheSize;
    }

    /**
     * 获取文件或文件夹的大小，并格式化
     *
     * @param file File
     * @return 字节数格式化后的文本
     */
    public static String getFileSize(@NonNull Context context, @NonNull File file) {
        return formatFileSize(context, getFileSize(file));
    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param file File
     * @return 字节数
     */
    public static long getFileSize(@NonNull File file) {
        if (file.isFile()) {
            return file.length();
        }
        long size = 0;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File subFile : files) {
                // 如果下面还有文件
                if (subFile.isDirectory()) {
                    size += getFileSize(subFile);
                } else {
                    size += subFile.length();
                }
            }
        }
        return size;
    }

    /**
     * 格式化文件大小
     *
     * @param context   Context
     * @param sizeBytes 字节数
     */
    public static String formatFileSize(@NonNull Context context, long sizeBytes) {
        return Formatter.formatFileSize(context, sizeBytes);
    }


    /**
     * 对文件数组按名称排序
     *
     * @param files 文件数组
     */
    public static void sortFiles(File[] files) {
        Arrays.sort(files, new Comparator<File>() {

            /**
             * return -1表示file1<file2，0 表示file1=file2，1表示file1>file2
             */
            @Override
            public int compare(File file1, File file2) {
                boolean isDirectory1 = file1.isDirectory();
                boolean isDirectory2 = file2.isDirectory();
                if (isDirectory1 && !isDirectory2)
                    return -1;
                else if (!isDirectory1 && isDirectory2)
                    return 1;
                else {
                    return file1.getName().compareTo(file2.getName());
                }
            }
        });
    }

}
