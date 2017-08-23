package cn.lzh.utils;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	private static final String LOG_TAG = DateUtils.class.getSimpleName();
	
	private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	private static final String PATTERN_DATE = "yyyy-MM-dd";
	
	private static final String PATTERN_TIME = "hh:mm:ss";
//
//	public static final int DATE_TIME_LENGTH = PATTERN_DATE_TIME.length();
//	
//	public static final int DATE_LENGTH = PATTERN_DATE.length();
//
//	public static final int TIME_LENGTH = PATTERN_TIME_HH_MM_SS.length();

	private static SimpleDateFormat formatDateTime = new SimpleDateFormat(PATTERN_DATE_TIME);
	private static SimpleDateFormat formatDate = new SimpleDateFormat(PATTERN_DATE);
	private static SimpleDateFormat formatTime = new SimpleDateFormat(PATTERN_TIME);
	
	private DateUtils() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}
	
	/**
	 * 返回具体时间描述（某某时间单位之前）
	 * @param offset
	 * @return
	 */
	public static String formatDetailTime(long offset){
		long second = 1000;    //1秒
		long minute = 60 * 1000;// 1分钟
		long hour = 60 * minute;
//		long week = 7 * day;
//		long mouth = 31 * day;
//		long year = 12 * mouth;
		long how = 0;
//		if (offset > year) {
//			how = offset / year;
//			return how + "年前";
//		}
//		if (offset > mouth) {
//			how = offset / mouth;
//			return how + "月前";
//		}
//		if (offset > week) {
//			how = offset / week;
//			return how + "周前";
//		}
		
		if (offset > hour) {
			how = offset / hour;
			return how + "小时前";
		}
		if (offset > minute) {
			how = offset / minute;
			return how + "分钟前";
		}
		if (offset > second) {
			how = offset / second;
			return how + "秒前";
		}
		return "刚刚";
	}
	
	/**
	 * 格式化时长(00:00)
	 * @param time 秒
	 * @return
	 */
	public static String formatTime(int time){
		Log.w(LOG_TAG, "formatTime:time="+time);
		if(time<=0){
			return "00:00";
		}
		int h=3600,m=60;
		StringBuilder sb=new StringBuilder();
		int temp = time/h;
		if(temp>0){
			sb.append(temp>9?temp:"0"+temp);
			temp=(time-temp*h)/m;
			if(temp>=0){
				sb.append(":");
				sb.append(temp>9?temp:"0"+temp);
				temp = time%m;//总时长与一分钟取余可得秒数
				if(temp>=0){
					sb.append(":");
					sb.append(temp>9?temp:"0"+temp);
				}
			}
		}else{
			temp=time/m;
			if(temp>=0){
				sb.append(temp>9?temp:"0"+temp);
				temp = time%m;//总时长与一分钟取余可得秒数
				if(temp>=0){
					sb.append(":");
					sb.append(temp>9?temp:"0"+temp);
				}
			}else{
				temp = time%m;//总时长与一分钟取余可得秒数
				if(temp>=0){
					sb.append("00:");
					sb.append(temp>9?temp:"0"+temp);
				}
			}
		}
		return sb.toString();
	}
	
	public static String format(String format, Date date) {
		if (date == null) {
			date = new Date();
		}
		return DateFormat.format(format, date).toString();
	}

	/**
	 * 格式化日期时间
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date){
		return formatDateTime.format(date);
	}
	
	/**
	 * 格式化日期
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return formatDate.format(date);
	}

	/**
	 * 格式化时间
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date){
		return formatTime.format(date);
	}
	
	/**
	 * 格式化日期时间字符串，转为日期字符串
	 * @param datetime(格式：yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static String formatDate(String datetime){
		return datetime.substring(0,PATTERN_DATE.length());
	}
	
	/**
	 * 格式化日期时间字符串，转为时间字符串
	 * @param datetime(格式：yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static String formatTime(String datetime){
//		datetime.substring(DATE_LENGTH+1);
		return datetime.substring(PATTERN_DATE_TIME.length()-PATTERN_TIME.length());
	}
	/**
	 * 解析日期时间字符串
	 * 
	 * @param datetime(格式：yyyy-MM-dd HH:mm:ss)
	 * @return 日期时间
	 */
	public static Date parseDateTime(String datetime) {
		Date date = null;
		try {
			date = formatDateTime.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
			date=new Date();
		}
		return date;
	}

	/**
	 * 解析日期字符串
	 * 
	 * @param datetime(格式：yyyy-MM-dd)
	 * @return 日期
	 */
	public static Date parseDate(String datetime) {
		Date date = null;
		try {
			date = formatDate.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
			date=new Date();
		}
		return date;
	}
	
	/**
	 * 计算两个日期之间相差的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date startDate, Date endDate){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE);
			startDate = sdf.parse(sdf.format(startDate));
			endDate = sdf.parse(sdf.format(endDate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(endDate);
			long time2 = cal.getTimeInMillis();
			long between_days = Math.abs(time2 - time1) / (1000 * 3600 * 24);
			return Integer.parseInt(String.valueOf(between_days));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
}

