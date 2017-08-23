package cn.lzh.utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 日历相关操作
 * @author lzh
 *
 */
public class CalendarUtil {
	private CalendarUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 判断指定日期是否是今天
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isToday(CustomDate date) {
		return (date.year == CalendarUtil.getYear()
				&& date.month == CalendarUtil.getMonth() && date.day == CalendarUtil
					.getDayOfMonth());
	}

	/**
	 * 判断指定日期是否是当前月份的日期
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isCurrentMonth(CustomDate date) {
		return (date.year == CalendarUtil.getYear() && date.month == CalendarUtil
				.getMonth());
	}

	/**
	 * 获取指定年月的第一天是星期几
	 * 
	 * @param year
	 * @param month
	 *            实际月份
	 * @return
	 */
	public static int getWeekDayOfMonthFirstDay(int year, int month) {
		// 日历中的月份=实际月份-1
		GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
		int currentWeekDay = getDayOfWeek(calendar);
		return currentWeekDay;
	}

	/**
	 * 获取指定年月份的天数
	 * 
	 * @param year
	 * @param month
	 *            实际月份
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		// 日历中的月份=实际月份-1
		GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
		return getDaysOfMonth(calendar);
	}

	/**
	 * 获取指定月历的天数
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getDaysOfMonth(Calendar calendar) {
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getDayOfMonth(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHourOfDay(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}

	public static int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * 周末为1,周六为7
	 */
	public static int getDayOfWeek(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public static int getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getHourOfDay() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	public static int getSecond() {
		return Calendar.getInstance().get(Calendar.SECOND);
	}

	/**
	 * 周末为1,周六为7
	 */
	public static int getDayOfWeek() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 自定义的日期，包括year,month,day
	 * 
	 * @author lzh
	 *
	 */
	public static class CustomDate implements Serializable {

		private static final long serialVersionUID = 1L;
		public int year;
		public int month;
		public int day;
		public int week;

		public CustomDate() {
			this.year = CalendarUtil.getYear();
			this.month = CalendarUtil.getMonth();
			this.day = CalendarUtil.getDayOfMonth();
		}

		public CustomDate(Calendar calendar) {
			this.year = calendar.get(Calendar.YEAR);
			this.month = calendar.get(Calendar.MONTH) + 1;
			this.day = calendar.get(Calendar.DAY_OF_MONTH);
		}

		public CustomDate(int year, int month, int day) {
			if (month > 12) {
				month = 1;
				year++;
			} else if (month < 1) {
				month = 12;
				year--;
			}
			this.year = year;
			this.month = month;
			this.day = day;
		}

		public CustomDate(CustomDate date) {
			this.year = date.year;
			this.month = date.month;
			this.day = date.day;
		}

		/*
		 * public static CustomDate modifiDayForObject(CustomDate date,int day){
		 * CustomDate modifiDate = new CustomDate(date.year,date.month,day);
		 * return modifiDate; }
		 */

		@Override
		public String toString() {
			return year + "-" + (month > 9 ? "" : "0") + month + "-"
					+ (day > 9 ? "" : "0") + day;
		}
	}
}