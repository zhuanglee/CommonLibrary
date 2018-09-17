package cn.lzh.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * last modify date is 2017-10-20<br/>
 *
 * 日历相关操作
 *
 * @author lzh
 */
public class CalendarUtil {

    public static final String[] WEEK_DAYS = new String[]{"日", "一", "二", "三", "四",
            "五", "六"};

    private CalendarUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取年份
     * @param calendar Calendar
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     * @param calendar Calendar
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期中的day
     * @param calendar Calendar
     */
    public static int getDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时
     * @param calendar Calendar
     */
    public static int getHourOfDay(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     * @param calendar Calendar
     */
    public static int getMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取秒
     * @param calendar Calendar
     */
    public static int getSecond(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取指定日期为周几
     *
     * @param calendar Calendar
     * @return 0~6，0未周末，1~6为周一到周六
     */
    public static int getDayOfWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取指定年月的第一天是星期几
     *
     * @param calendar Calendar
     */
    public static int getWeekDayOfMonthFirstDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDayOfWeek(calendar);
    }

    /**
     * 获取指定年月的第一天是星期几
     *
     * @param year 年份
     * @param month 实际月份，日历中的月份=实际月份-1
     */
    public static int getWeekDayOfMonthFirstDay(int year, int month) {
        return getDayOfWeek(new GregorianCalendar(year, month - 1, 1));
    }

    /**
     * 获取指定月历的天数
     *
     * @param calendar Calendar
     */
    public static int getDaysOfMonth(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定年月份的天数
     *
     * @param year 年份
     * @param month 实际月份，日历中的月份=实际月份-1
     */
    public static int getDaysOfMonth(int year, int month) {
        GregorianCalendar calendar = new GregorianCalendar(year, month - 1, 1);
        return getDaysOfMonth(calendar);
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @param date Date
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        int week = c.get(Calendar.WEEK_OF_YEAR) - 1;
        week = week == 0 ? 52 : week;
        return week > 0 ? week : 1;
    }

    /**
     * 获取本月第一天0点时间
     */
    public static long getTimesMonthMorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /**
     * 判断指定日期是否是今天
     *
     * @param date Calendar
     */
    public static boolean isToday(Calendar date) {
        Calendar calendar = Calendar.getInstance();
        return (getYear(date) == getYear(calendar)
                && getMonth(date) == getMonth(calendar)
                && getDayOfMonth(date) == getDayOfMonth(calendar));
    }

    /**
     * 判断指定日期是否是当前月份的日期
     *
     * @param date Calendar
     */
    public static boolean isCurrentMonth(Calendar date) {
        Calendar calendar = Calendar.getInstance();
        return (getYear(date) == getYear(calendar)
                && getMonth(date) == getMonth(calendar));
    }

}