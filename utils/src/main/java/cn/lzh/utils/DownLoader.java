package cn.lzh.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

@SuppressLint("NewApi")
public class DownLoader {

	private static String mimetype;
	private static Map<String, String> maps;
	private static String savePath;
	
	public interface ExceptionListener{
		/**
		 * 异常调用方法
		 * @param e
		 */
		void throwexception(Exception e);
	}

	public static void setSavePath(String path){
		savePath = path;
	}
	
	public static void downNewFile(final Context context, final String downurl,final String fileName,final ExceptionListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getRealDownloadUrl(context,downurl,fileName,listener);
			}
		}).start();
	}
	
	private static void getRealDownloadUrl(Context context,String downurl,String fileName,ExceptionListener listener) {
		HttpURLConnection conn= null;
		try {
			URL url = new URL(downurl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1000 * 10);  
            conn.setReadTimeout(1000 * 30);  
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);
			int status = conn.getResponseCode();
			if(status == HttpURLConnection.HTTP_MOVED_PERM 
					|| status == HttpURLConnection.HTTP_MOVED_TEMP){
				 String downString = conn.getHeaderField("Location");
//				System.out.println(status+ "  Location:  " + downString);
				 getRealDownloadUrl(context,downString,fileName,listener);
			}else if(status == HttpURLConnection.HTTP_OK){
				download(context, downurl,fileName,listener);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
	}
	
	private static void download(Context context, String downurl , String fileName , ExceptionListener listener) {
		Uri uri = Uri.parse(downurl);
		DownloadManager downloadManager = (DownloadManager) context
				.getSystemService(Context.DOWNLOAD_SERVICE);
		Request request = new Request(uri);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);
		mimetype = getMIMEType(new File(downurl));
		// 设置文件类型
		request.setMimeType(mimetype);
		// 在通知栏中显示
		request.setShowRunningNotification(true);
		// request.setVisibleInDownloadsUi(false);
		// sdcard的目录下的download文件夹
		try {
			getdownloaderdirectory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		fileName += downurl.substring(downurl.indexOf(".",30)+1,downurl.length());
		request.setDestinationInExternalPublicDir(savePath,
				fileName);
		request.setTitle(fileName);
		// request.setDescription("从宝软精灵下载");
		if (Build.VERSION.SDK_INT >= 11) {
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		try {
			long id = downloadManager.enqueue(request);
			// Loger.e("iamdebug", " 请求下载 Id= "+id);
		} catch (Exception e) {
			e.printStackTrace();
			Looper.prepare();
			listener.throwexception(e);
			Looper.loop();
		}
	}

	public static class CompleteBroadRecver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			DownloadManager manager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
			String extraID = DownloadManager.EXTRA_DOWNLOAD_ID;
			long references = intent.getLongExtra(extraID, -1);
			Cursor c = manager.query(new DownloadManager.Query()
					.setFilterById(references));
			if (c != null && c.moveToFirst()) {
				String url = c.getString(c
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//				Logg.e(BenguoApp.TAG, "——onReceive = "+url+" "+getClass().getName());
				// String COLUMN_ID = c.getString(c
				// .getColumnIndex(DownloadManager.COLUMN_ID));
				// String COLUMN_REASON = c.getString(c
				// .getColumnIndex(DownloadManager.COLUMN_REASON));
				// String COLUMN_STATUS = c.getString(c
				// .getColumnIndex(DownloadManager.COLUMN_STATUS));
				// String COLUMN_TITLE = c.getString(c
				// .getColumnIndex(DownloadManager.COLUMN_TITLE));
				// String name =
				// c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
				// //add API 11
				// Loger.e("iamdebug", "下载完成,Id： "+ references);
				// Loger.e("iamdebug", " url= "+ url);
				// Loger.e("iamdebug", " COLUMN_ID= "+
				// COLUMN_ID+" COLUMN_REASON="+COLUMN_REASON+" COLUMN_STATUS="+COLUMN_STATUS+" COLUMN_TITLE="+COLUMN_TITLE);
				c.close();
				if (url != null && !"".equals(url)) {
					// File apkfile = new File(url);
					// if (!apkfile.exists()) {
					// return;
					// }
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setDataAndType(Uri.parse(url), mimetype);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
				// Loger.e("iamdebug", "下载完成,packname："+packageName);
			}
		}
	}

	/**
	 * 获取下载目录
	 * 
	 * @throws Exception
	 */
	private static String getdownloaderdirectory() throws Exception {
		File saveDir = null;
		if(savePath==null){
			savePath = "/Download";
		}else if(!savePath.startsWith("/")){
			savePath += "/";
		}
		saveDir = new File(Environment.getExternalStorageDirectory(),
				savePath);
		if (!saveDir.exists()) {
			saveDir.mkdirs();
		}
		return saveDir.getAbsolutePath();
	}

	public static class NotifyClickBroadRecver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// String extraID =
			// DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS; // add API
			// 11
			// long[] references = intent.getLongArrayExtra(extraID);
			Intent i = new Intent();
			i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	public static String getMIMEType(File file) {

		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
//		for (int i = 0; i < MIME_MapTable.length; i++) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
//			if (end.equals(MIME_MapTable[i][0]))
//				type = MIME_MapTable[i][1];
//		}
		type = getMime_MapTable().get(end);
		return type;
	}
	
	private static Map<String, String> getMime_MapTable(){
		if (maps == null) {
			maps = new HashMap<String, String>();
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
		}
		return maps;
	}
}
