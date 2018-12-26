package cn.lzh.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author from open source
 */
public final class DateUtil {

    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TIME_SIMPLE = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_SHORT = "yyyy-MM";
    public static final String FORMAT_TIME = "HH:mm:ss";
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(FORMAT_DATE_TIME, Locale.CHINESE);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(FORMAT_DATE, Locale.CHINESE);

    private DateUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 解析日期时间字符串
     *
     * @param datetime 格式："yyyy-MM-dd HH:mm:ss"
     * @return 日期时间
     */
    public static Date parseDateTime(@NonNull String datetime) throws ParseException {
        return DATE_TIME_FORMAT.parse(datetime);
    }

    /**
     * 解析日期字符串
     *
     * @param datetime 格式："yyyy-MM-dd"
     * @return 日期
     */
    public static Date parseDate(@NonNull String datetime) throws ParseException {
        return DATE_FORMAT.parse(datetime);
    }

    /**
     * 获取指定时间的Unix时间戳
     * @param time 时间毫秒值{@link Calendar#getTimeInMillis()}
     * @return 当指定时间的Unix时间戳
     */
    public static int getUnixTimestamp(long time) {
        return (int) (time / 1000);
    }

    /**
     * 获取Unix时间戳
     *
     * @param calendar 日历实例
     * @return calendar日期对应的 Unix时间戳
     */
    public static int getUnixTimestamp(Calendar calendar) {
        return getUnixTimestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取当前时间的Unix时间戳
     *
     * @return 当前时间的Unix时间戳
     */
    public static int getUnixTimestamp() {
        return getUnixTimestamp(System.currentTimeMillis());
    }

    /**
     * 获取日历
     *
     * @param timestamp  unix时间戳
     */
    public static Calendar getCalendar(@NonNull Integer timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp * 1000L);
        return calendar;
    }

    /**
     * 格式化日期时间
     *
     * @param timestamp  unix时间戳
     */
    @Nullable
    public static String formatDateTime(@Nullable Integer timestamp) {
        return timestamp == null ? null : formatDateTime(getCalendar(timestamp));
    }

    /**
     * 格式化日期到天
     *
     * @param timestamp  unix时间戳
     */
    @Nullable
    public static String formatDate(@Nullable Integer timestamp) {
        return timestamp == null ? null : formatDate(getCalendar(timestamp));
    }

    /**
     * 格式化日期到月份
     *
     * @param timestamp  unix时间戳
     */
    public static String formatDateShort(@Nullable Integer timestamp) {
        return timestamp == null ? null : formatDateShort(getCalendar(timestamp));
    }

    /**
     * 格式化时间
     *
     * @param timestamp  unix时间戳
     */
    public static String formatTime(@Nullable Integer timestamp) {
        return timestamp == null ? null : formatTime(getCalendar(timestamp));
    }

    /**
     * 格式化日期事件字符串
     * @param format 格式
     * @param datetime 日期时间字符串
     */
    public static String format(String format, String datetime) throws ParseException {
        return DateFormat.format(format, parseDateTime(datetime)).toString();
    }

    /**
     * 格式化日期字符串
     * @param datetime 日期时间字符串
     */
    public static String formatDate(@NonNull String datetime) throws ParseException {
        return formatDate(parseDateTime(datetime));
    }

    public static String formatDateTime(@NonNull Calendar date) {
        return DateFormat.format(FORMAT_DATE_TIME_SIMPLE, date).toString();
    }

    public static String formatDateTime(@NonNull Date date) {
        return DateFormat.format(FORMAT_DATE_TIME_SIMPLE, date).toString();
    }

    public static String formatDate(@NonNull Calendar date) {
        return DateFormat.format(FORMAT_DATE, date).toString();
    }

    public static String formatDate(@NonNull Date date) {
        return DateFormat.format(FORMAT_DATE, date).toString();
    }

    public static String formatDateShort(@NonNull Calendar date) {
        return DateFormat.format(FORMAT_DATE_SHORT, date).toString();
    }

    public static String formatDateShort(@NonNull Date date) {
        return DateFormat.format(FORMAT_DATE_SHORT, date).toString();
    }

    public static String formatTime(@NonNull Calendar date) {
        return DateFormat.format(FORMAT_TIME, date).toString();
    }

    public static String formatTime(@NonNull Date date) {
        return DateFormat.format(FORMAT_TIME, date).toString();
    }

    /**
     * 日期比较
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 0相等，1大于，-1小于
     */
    public static int compare(Calendar date1, Calendar date2) {
        if (date1 == null && date2 == null) {
            return 0;
        }
        if (date1 == null) {
            return -1;
        }
        if (date2 == null) {
            return 1;
        }
        long d = date1.getTimeInMillis() - date2.getTimeInMillis();
        return d == 0 ? 0 : (int) (d / Math.abs(d));
    }


    /**
     * 日期比较
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 0相等，1大于，-1小于
     */
    public static int compare(long date1, long date2) {
        long d = date1 - date2;
        return d == 0 ? 0 : (int) (d / Math.abs(d));
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     */
    public static int daysBetween(Date startDate, Date endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE);
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
     */
    public static boolean isChinaTimeZone() {
        return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
    }

    /**
     * 根据不同时区，转换时间
     *
     * @param date Date
     * @param oldZone 旧时区
     * @param newZone 新时区
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

    /**
     * 以友好的方式显示时间
     *
     * @param dateStr 日期时间字符串
     */
    public static String friendlyTime(String dateStr) {
        String result = "Unknown";
        Date time = null;
        try {
            time = parseDateTime(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return result;
        }
        if (!isChinaTimeZone())
            time = transformTime(time,
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

        if (time == null) {
            return result;
        }
        // 判断是否是同一天
        Calendar cal = Calendar.getInstance();
        String curDate = formatDate(cal.getTime());
        if (curDate.equals(dateStr.substring(0, FORMAT_DATE.length()))) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                result = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                result = hour + "小时前";
            return result;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                result = Math.max(
                        (cal.getTimeInMillis() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                result = hour + "小时前";
        } else if (days == 1) {
            result = "昨天";
        } else if (days == 2) {
            result = "前天 ";
        } else if (days > 2 && days < 31) {
            result = days + "天前";
        } else if (days >= 31 && days <= 2 * 31) {
            result = "一个月前";
        } else if (days > 2 * 31 && days <= 3 * 31) {
            result = "2个月前";
        } else if (days > 3 * 31 && days <= 4 * 31) {
            result = "3个月前";
        } else {
            result = formatDate(time);
        }
        return result;
    }

    /**
     * 格式化分钟和秒
     *
     * @param second 秒数
     * @return xx小时xx分xx秒
     */
    public static String formatSimpleChineseTime(int second) {
        if (second == 0) {
            return "0秒";
        }
        int h = second / 3600;
        int m = (second / 60) % 60;
        int s = second % 60;
        String time = "";
        if (h > 0) {
            time += m > 0 || s > 0 ? String.format("%s时", h) : String.format("%s小时", h);
        }
        if (m > 0) {
            time += s > 0 ? String.format("%s分", m) : String.format("%s分钟", m);
        }
        if (s > 0) {
            time += String.format("%s秒", s);
        }
        return time;
    }

    /**
     * 格式化分钟和秒
     *
     * @param second 秒数
     * @return xx:xx:xx
     */
    public static String formatSimpleTime(int second) {
        int h = second / 3600;
        int m = (second / 60) % 60;
        int s = second % 60;
        if (h > 0) {
            return String.format("%s:%s:%s", formatNumber(h), formatNumber(m), formatNumber(s));
        }
        return String.format("%s:%s", formatNumber(m), formatNumber(s));
    }

    /**
     * 格式化数字，在1位数前填充"0"
     *
     * @param num 数字
     * @return 两位数的字符串
     */
    private static String formatNumber(int num) {
        return num > 9 ? String.valueOf(num) : "0" + num;
    }

}

