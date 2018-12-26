/**
 * Copyright 2014 Zhenguo Jin
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.lzh.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * DownloadManagerPro
 * <ul>
 * <strong>Get download info</strong>
 * <li>{@link #getStatusById(long)} get download status</li>
 * <li>{@link #getDownloadBytes(long)} get downloaded byte, total byte</li>
 * <li>{@link #getBytesAndStatus(long)} get downloaded byte, total byte and
 * download status</li>
 * <li>{@link #getFileName(long)} get download file name</li>
 * <li>{@link #getUri(long)} get download uri</li>
 * <li>{@link #getReason(long)} get failed code or paused reason</li>
 * <li>{@link #getPausedReason(long)} get paused reason</li>
 * <li>{@link #getErrorCode(long)} get failed error code</li>
 * </ul>
 * <ul>
 * <strong>Operate download</strong>
 * <li>{@link #isExistPauseAndResumeMethod()} whether exist pauseDownload and
 * resumeDownload method in {@link DownloadManager}</li>
 * <li>{@link #pauseDownload(long...)} pause download. need
 * pauseDownload(long...) method in {@link DownloadManager}</li>
 * <li>{@link #resumeDownload(long...)} resume download. need
 * resumeDownload(long...) method in {@link DownloadManager}</li>
 * </ul>
 * <ul>
 * <strong>RequestPro</strong>
 * <li>{@link RequestPro#setNotiClass(String)} set noti class</li>
 * <li>{@link RequestPro#setNotiExtras(String)} set noti extras</li>
 * </ul>
 *
 * @author jingle1267@163.com
 */
@Deprecated
@SuppressLint("InlinedApi")
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class DownloadManagerPro {

    public static final Uri CONTENT_URI = Uri
            .parse("content://downloads/my_downloads");

    public static final String METHOD_NAME_PAUSE_DOWNLOAD = "pauseDownload";
    public static final String METHOD_NAME_RESUME_DOWNLOAD = "resumeDownload";

    private static boolean isInitPauseDownload = false;
    private static boolean isInitResumeDownload = false;

    private static Method pauseDownload = null;
    private static Method resumeDownload = null;

    private DownloadManager downloadManager;

    public DownloadManagerPro(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    /**
     * get download status
     *
     * @param downloadId
     * @return
     */
    public int getStatusById(long downloadId) {
        return getInt(downloadId, DownloadManager.COLUMN_STATUS);
    }

    /**
     * get downloaded byte, total byte
     *
     * @param downloadId
     * @return a int array with two elements
     * <ul>
     * <li>result[0] represents downloaded bytes, This will initially be
     * -1.</li>
     * <li>result[1] represents total bytes, This will initially be -1.</li>
     * </ul>
     */
    public int[] getDownloadBytes(long downloadId) {
        int[] bytesAndStatus = getBytesAndStatus(downloadId);
        return new int[]{bytesAndStatus[0], bytesAndStatus[1]};
    }

    /**
     * get downloaded byte, total byte and download status
     *
     * @param downloadId
     * @return a int array with three elements
     * <ul>
     * <li>result[0] represents downloaded bytes, This will initially be
     * -1.</li>
     * <li>result[1] represents total bytes, This will initially be -1.</li>
     * <li>result[2] represents download status, This will initially be
     * 0.</li>
     * </ul>
     */
    public int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{-1, -1, 0};
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c
                        .getInt(c
                                .getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c
                        .getInt(c
                                .getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    /**
     * pause download
     *
     * @param ids the IDs of the downloads to be paused
     * @return the number of downloads actually paused, -1 if exception or
     * method not exist
     */
    public int pauseDownload(long... ids) {
        initPauseMethod();
        if (pauseDownload == null) {
            return -1;
        }

        try {
            return (Integer) pauseDownload.invoke(downloadManager, ids);
        } catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException,
             * NoSuchMethodException, InvocationTargetException,
             * NullPointException
             */
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * resume download
     *
     * @param ids the IDs of the downloads to be resumed
     * @return the number of downloads actually resumed, -1 if exception or
     * method not exist
     */
    public int resumeDownload(long... ids) {
        initResumeMethod();
        if (resumeDownload == null) {
            return -1;
        }

        try {
            return (Integer) resumeDownload.invoke(downloadManager, ids);
        } catch (Exception e) {
            /**
             * accept all exception, include ClassNotFoundException,
             * NoSuchMethodException, InvocationTargetException,
             * NullPointException
             */
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * whether exist pauseDownload and resumeDownload method in
     * {@link DownloadManager}
     *
     * @return
     */
    public static boolean isExistPauseAndResumeMethod() {
        initPauseMethod();
        initResumeMethod();
        return pauseDownload != null && resumeDownload != null;
    }

    private static void initPauseMethod() {
        if (isInitPauseDownload) {
            return;
        }

        isInitPauseDownload = true;
        try {
            pauseDownload = DownloadManager.class.getMethod(
                    METHOD_NAME_PAUSE_DOWNLOAD, long[].class);
        } catch (Exception e) {
            // accept all exception
            e.printStackTrace();
        }
    }

    private static void initResumeMethod() {
        if (isInitResumeDownload) {
            return;
        }

        isInitResumeDownload = true;
        try {
            resumeDownload = DownloadManager.class.getMethod(
                    METHOD_NAME_RESUME_DOWNLOAD, long[].class);
        } catch (Exception e) {
            // accept all exception
            e.printStackTrace();
        }
    }

    /**
     * get download file name
     *
     * @param downloadId
     * @return
     */
    public String getFileName(long downloadId) {
        String fileName;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            fileName = Uri.parse(getString(downloadId, DownloadManager.COLUMN_LOCAL_URI)).getPath();
        }else{
            //DownloadManager.COLUMN_LOCAL_FILENAME Android7.0 会报异常
            fileName = getString(downloadId, DownloadManager.COLUMN_LOCAL_FILENAME);
        }
        return fileName;
    }

    /**
     * get download uri
     *
     * @param downloadId
     * @return
     */
    public String getUri(long downloadId) {
        return getString(downloadId, DownloadManager.COLUMN_URI);
    }

    /**
     * get failed code or paused reason
     *
     * @param downloadId
     * @return <ul>
     * <li>if status of downloadId is
     * {@link DownloadManager#STATUS_PAUSED}, return
     * {@link #getPausedReason(long)}</li>
     * <li>if status of downloadId is
     * {@link DownloadManager#STATUS_FAILED}, return
     * {@link #getErrorCode(long)}</li>
     * <li>if status of downloadId is neither
     * {@link DownloadManager#STATUS_PAUSED} nor
     * {@link DownloadManager#STATUS_FAILED}, return 0</li>
     * </ul>
     */
    public int getReason(long downloadId) {
        return getInt(downloadId, DownloadManager.COLUMN_REASON);
    }

    /**
     * get paused reason
     *
     * @param downloadId
     * @return <ul>
     * <li>if status of downloadId is
     * {@link DownloadManager#STATUS_PAUSED}, return one of
     * {@link DownloadManager#PAUSED_WAITING_TO_RETRY}<br/>
     * {@link DownloadManager#PAUSED_WAITING_FOR_NETWORK}<br/>
     * {@link DownloadManager#PAUSED_QUEUED_FOR_WIFI}<br/>
     * {@link DownloadManager#PAUSED_UNKNOWN}</li>
     * <li>else return {@link DownloadManager#PAUSED_UNKNOWN}</li>
     * </ul>
     */
    public int getPausedReason(long downloadId) {
        return getInt(downloadId, DownloadManager.COLUMN_REASON);
    }

    /**
     * get failed error code
     *
     * @param downloadId
     * @return one of {@link DownloadManager#ERROR_UNKNOWN}
     */
    public int getErrorCode(long downloadId) {
        return getInt(downloadId, DownloadManager.COLUMN_REASON);
    }

    public static class RequestPro extends Request {

        public static final String METHOD_NAME_SET_NOTI_CLASS = "setNotiClass";
        public static final String METHOD_NAME_SET_NOTI_EXTRAS = "setNotiExtras";

        private static boolean isInitNotiClass = false;
        private static boolean isInitNotiExtras = false;

        private static Method setNotiClass = null;
        private static Method setNotiExtras = null;

        /**
         * @param uri the HTTP URI to download.
         */
        public RequestPro(Uri uri) {
            super(uri);
        }

        /**
         * set noti class, only init once
         *
         * @param className full class name
         */
        public void setNotiClass(String className) {
            synchronized (this) {

                if (!isInitNotiClass) {
                    isInitNotiClass = true;
                    try {
                        setNotiClass = Request.class.getMethod(
                                METHOD_NAME_SET_NOTI_CLASS, CharSequence.class);
                    } catch (Exception e) {
                        // accept all exception
                        e.printStackTrace();
                    }
                }
            }

            if (setNotiClass != null) {
                try {
                    setNotiClass.invoke(this, className);
                } catch (Exception e) {
                    /**
                     * accept all exception, include ClassNotFoundException,
                     * NoSuchMethodException, InvocationTargetException,
                     * NullPointException
                     */
                    e.printStackTrace();
                }
            }
        }

        /**
         * set noti extras, only init once
         *
         * @param extras
         */
        public void setNotiExtras(String extras) {
            synchronized (this) {

                if (!isInitNotiExtras) {
                    isInitNotiExtras = true;
                    try {
                        setNotiExtras = Request.class
                                .getMethod(METHOD_NAME_SET_NOTI_EXTRAS,
                                        CharSequence.class);
                    } catch (Exception e) {
                        // accept all exception
                        e.printStackTrace();
                    }
                }
            }

            if (setNotiExtras != null) {
                try {
                    setNotiExtras.invoke(this, extras);
                } catch (Exception e) {
                    /**
                     * accept all exception, include ClassNotFoundException,
                     * NoSuchMethodException, InvocationTargetException,
                     * NullPointException
                     */
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * get string column
     *
     * @param downloadId
     * @param columnName
     * @return
     */
    private String getString(long downloadId, String columnName) {
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        String result = null;
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                result = c.getString(c.getColumnIndex(columnName));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }

    /**
     * get int column
     *
     * @param downloadId
     * @param columnName
     * @return
     */
    private int getInt(long downloadId, String columnName) {
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        int result = -1;
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                result = c.getInt(c.getColumnIndex(columnName));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return result;
    }


    public static Map<String, String> getMimeTypeMap(){
        Map<String, String> maps = new HashMap<String, String>();
        maps.put(".3gp", "video/3gpp" );
        maps.put( ".apk", "application/vnd.android.package-archive" );
        maps.put( ".asf", "video/x-ms-asf" );
        maps.put( ".avi", "video/x-msvideo" );
        maps.put( ".bin", "application/octet-stream" );
        maps.put( ".bmp", "image/bmp" );
        maps.put( ".c", "text/plain" );
        maps.put( ".class", "application/octet-stream" );
        maps.put( ".conf", "text/plain" );
        maps.put( ".cpp", "text/plain" );
        maps.put( ".doc", "application/msword" );
        maps.put( ".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document" );
        maps.put( ".xls", "application/vnd.ms-excel" );
        maps.put( ".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
        maps.put( ".exe", "application/octet-stream" );
        maps.put( ".f4v", "audio/x-mpeg" );
        maps.put( ".gif", "image/gif" );
        maps.put( ".gtar", "application/x-gtar" );
        maps.put( ".gz", "application/x-gzip" );
        maps.put( ".h", "text/plain" );
        maps.put( ".htm", "text/html" );
        maps.put( ".html", "text/html" );
        maps.put( ".jar", "application/java-archive" );
        maps.put( ".java", "text/plain" );
        maps.put( ".jpeg", "image/jpeg" );
        maps.put( ".jpg", "image/jpeg" );
        maps.put( ".js", "application/x-javascript" );
        maps.put( ".log", "text/plain" );
        maps.put( ".m3u", "audio/x-mpegurl" );
        maps.put( ".m4a", "audio/mp4a-latm" );
        maps.put( ".m4b", "audio/mp4a-latm" );
        maps.put( ".m4p", "audio/mp4a-latm" );
        maps.put( ".m4u", "video/vnd.mpegurl" );
        maps.put( ".m4v", "video/x-m4v" );
        maps.put( ".mov", "video/quicktime" );
        maps.put( ".mp2", "audio/x-mpeg" );
        maps.put( ".mp3", "audio/x-mpeg" );
        maps.put( ".mp4", "video/mp4" );
        maps.put( ".mpc", "application/vnd.mpohun.certificate" );
        maps.put( ".mpe", "video/mpeg" );
        maps.put( ".mpeg", "video/mpeg" );
        maps.put( ".mpg", "video/mpeg" );
        maps.put( ".mpg4", "video/mp4" );
        maps.put( ".mpga", "audio/mpeg" );
        maps.put( ".msg", "application/vnd.ms-outlook" );
        maps.put( ".ogg", "audio/ogg" );
        maps.put( ".pdf", "application/pdf" );
        maps.put( ".png", "image/png" );
        maps.put( ".pps", "application/vnd.ms-powerpoint" );
        maps.put( ".ppt", "application/vnd.ms-powerpoint" );
        maps.put( ".pptx","application/vnd.openxmlformats-officedocument.presentationml.presentation" );
        maps.put( ".prop", "text/plain" );
        maps.put( ".rc", "text/lain" );
        maps.put( ".rmvb", "audio/x-pn-realaudio" );
        maps.put( ".rtf", "application/rtf" );
        maps.put( ".sh", "text/plain" );
        maps.put( ".tar", "application/x-tar" );
        maps.put( ".tgz", "application/x-compressed" );
        maps.put( ".txt", "text/plain" );
        maps.put( ".wav", "audio/x-wav" );
        maps.put( ".wma", "audio/x-ms-wma" );
        maps.put( ".wmv", "audio/x-ms-wmv" );
        maps.put( ".wps", "application/vnd.ms-works" );
        maps.put( ".xml", "text/plain" );
        maps.put( ".z", "application/x-compress" );
        maps.put( ".zip", "application/x-zip-compressed" );
        maps.put( "", "*/*" );
        return maps;
    }

}
