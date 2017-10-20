package cn.lzh.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	private static final String LOG_TAG = DateUtil.class.getSimpleName();
	
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

	private final static ThreadLocal<SimpleDateFormat> sDateTimeFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> sDateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private DateUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param date
	 * @return
	 */
	public static String friendlyTime(String date) {
		Date time = null;

		if (isChinaTimeZone())
			time = toDate(date);
		else
			time = transformTime(toDate(date),
					TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = sDateFormat.get().format(cal.getTime());
		String paramDate = sDateFormat.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天 ";
		} else if (days > 2 && days < 31) {
			ftime = days + "天前";
		} else if (days >= 31 && days <= 2 * 31) {
			ftime = "一个月前";
		} else if (days > 2 * 31 && days <= 3 * 31) {
			ftime = "2个月前";
		} else if (days > 3 * 31 && days <= 4 * 31) {
			ftime = "3个月前";
		} else {
			ftime = sDateFormat.get().format(time);
		}
		return ftime;
	}

	public static String friendlyDateTime(String date) {
		if (date == null || date.length() < 10)
			return "";
		String result = "";
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		String currentDate = getToday("MM-dd");
		int currentDay = toInt(currentDate.substring(3));
		int currentMoth = toInt(currentDate.substring(0, 2));

		int sMoth = toInt(date.substring(5, 7));
		int sDay = toInt(date.substring(8, 10));
		int sYear = toInt(date.substring(0, 4));
		Date dt = new Date(sYear, sMoth - 1, sDay - 1);

		if (sDay == currentDay && sMoth == currentMoth) {
			result = "今天 / " + weekDays[getWeekOfDate(new Date())];
		} else if (sDay == currentDay + 1 && sMoth == currentMoth) {
			result = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
		} else {
			if (sMoth < 10) {
				result = "0";
			}
			result += sMoth + "/";
			if (sDay < 10) {
				result += "0";
			}
			result += sDay + " / " + weekDays[getWeekOfDate(dt)];
		}

		return result;
	}

	/**
	 * 将字符串转位日期类型
	 *
	 * @param date
	 * @return
	 */
	@Nullable
	private static Date toDate(@NonNull String date) {
		try {
			return sDateTimeFormat.get().parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 字符串转整数
	 *
	 * @param str
	 * @return
	 */
	private static int toInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	private static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

	/**
	 * 返回当前系统时间
	 */
	private static String getToday(String format) {
		return new SimpleDateFormat(format).format(new Date());
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


	/**
	 * 判断用户的设备时区是否为东八区（中国）
	 * @return
	 */
	public static boolean isChinaTimeZone() {
		return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
	}

	/**
	 * 根据不同时区，转换时间
	 * @param date
	 * @param oldZone
	 * @param newZone
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;
	}
}

