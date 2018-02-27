package cn.lzh.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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

    private DateUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 获取当前时间的Unix时间戳
     *
     * @return 当前时间的Unix时间戳
     */
    public static int getUnixTimestamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取Unix时间戳
     *
     * @param calendar 日历实例
     * @return calendar日期对应的 Unix时间戳
     */
    public static int getUnixTimestamp(Calendar calendar) {
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    /**
     * 获取指定时间的Unix时间戳
     *
     * @return 当指定时间的Unix时间戳
     */
    public static int getUnixTimestamp(long time) {
        return (int) (time / 1000);
    }

    /**
     * 获取当前时间的Unix时间戳
     *
     * @return 当前时间的Unix时间戳
     */
    public static String getUnixTimestampStr() {
        return String.valueOf(getUnixTimestamp());
    }

    /**
     * 获取指定时间的Unix时间戳
     *
     * @param time 毫秒值
     * @return 指定时间的Unix时间戳
     */
    public static String getUnixTimestampStr(long time) {
        return String.valueOf(time / 1000);
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

    public static String format(String format, String datetime) {
        Date date = parse(datetime);
        if (date == null) {
            return "";
        }
        return DateFormat.format(format, date).toString();
    }

    public static String formatMonth(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return DateFormat.format("yyyy-MM", calendar).toString();
    }

    public static String formatDay(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return DateFormat.format("yyyy-MM-dd", calendar).toString();
    }

    public static String formatDay(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormat.format("yyyy-MM-dd", date).toString();
    }

    public static String formatDay(String datetime) {
        return formatDay(parse(datetime));
    }

    /**
     * 格式化日期
     *
     * @param time            毫秒数 或 unix时间戳
     * @param isTimestamp 是否为unix时间戳
     */
    public static String formatDay(long time, boolean isTimestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(isTimestamp ? time * 1000 : time);
        return formatDay(c);
    }

    public static String formatTime(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return DateFormat.format("HH:mm:ss", calendar).toString();
    }

    public static String formatTime(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormat.format("HH:mm:ss", date).toString();
    }

    public static String formatDateTime(Calendar calendar) {
        if (calendar == null) {
            return "";
        }
        return DateFormat.format("yyyy-MM-dd HH:mm", calendar).toString();
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormat.format("yyyy-MM-dd HH:mm", date).toString();
    }

    /**
     * 格式化日期时间
     *
     * @param time            毫秒数 或 unix时间戳
     * @param isTimestamp 是否为unix时间戳
     * @return
     */
    public static String formatDateTime(long time, boolean isTimestamp) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(isTimestamp ? time * 1000 : time);
        return formatDateTime(c);
    }

    /**
     * 解析日期时间格式（"yyyy-MM-dd HH:mm:ss"）字符串
     *
     * @param datetime "yyyy-MM-dd HH:mm:ss"
     * @return Date
     */
    public static Date parse(String datetime) {
        if (TextUtils.isEmpty(datetime)) {
            return null;
        }
        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
            return dateTimeFormat.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
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
     * 获取日历
     *
     * @param time 毫秒数 或 unix时间戳
     * @param isTimestamp 是否为unix时间戳
     */
    public static Calendar getCalendar(long time, boolean isTimestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(isTimestamp ? time * 1000 : time);
        return calendar;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param dateStr
     * @return
     */
    public static String friendlyTime(String dateStr) {
        String result = "Unknown";
        Date time = parseDateTime(dateStr);
        if (!isChinaTimeZone())
            time = transformTime(time,
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

        if (time == null) {
            return result;
        }
        Calendar cal = Calendar.getInstance();
        // 判断是否是同一天
        String curDate = formatDay(cal.getTime());
        if (curDate.equals(dateStr.substring(0, PATTERN_DATE.length()))) {
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
            result = formatDay(time);
        }
        return result;
    }

    public static String friendlyDateTime(String date) {
        if (date == null || date.length() < 10)
            return "";
        String result = "";
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String currentDate = getToday("MM-dd");
        int currentDay = toInt(currentDate.substring(3));
        int currentMoth = toInt(currentDate.substring(0, 2));

        int year = toInt(date.substring(0, 4));
        int month = toInt(date.substring(5, 7));
        int day = toInt(date.substring(8, 10));
        Date dt = new Date(year, month - 1, day - 1);

        if (day == currentDay && month == currentMoth) {
            result = "今天 / " + weekDays[getWeekOfDate(new Date())];
        } else if (day == currentDay + 1 && month == currentMoth) {
            result = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
        } else {
            if (month < 10) {
                result = "0";
            }
            result += month + "/";
            if (day < 10) {
                result += "0";
            }
            result += day + " / " + weekDays[getWeekOfDate(dt)];
        }

        return result;
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
     * 格式化时长(00:00)
     *
     * @param time 秒
     * @return
     */
    public static String formatTime(int time) {
        if (time <= 0) {
            return "00:00";
        }
        int h = 3600, m = 60;
        StringBuilder sb = new StringBuilder();
        int temp = time / h;
        if (temp > 0) {
            sb.append(temp > 9 ? temp : "0" + temp);
            temp = (time - temp * h) / m;
            if (temp >= 0) {
                sb.append(":");
                sb.append(temp > 9 ? temp : "0" + temp);
                temp = time % m;//总时长与一分钟取余可得秒数
                if (temp >= 0) {
                    sb.append(":");
                    sb.append(temp > 9 ? temp : "0" + temp);
                }
            }
        } else {
            temp = time / m;
            if (temp >= 0) {
                sb.append(temp > 9 ? temp : "0" + temp);
                temp = time % m;//总时长与一分钟取余可得秒数
                if (temp >= 0) {
                    sb.append(":");
                    sb.append(temp > 9 ? temp : "0" + temp);
                }
            } else {
                temp = time % m;//总时长与一分钟取余可得秒数
                if (temp >= 0) {
                    sb.append("00:");
                    sb.append(temp > 9 ? temp : "0" + temp);
                }
            }
        }
        return sb.toString();
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
            date = new Date();
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
            date = new Date();
        }
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date startDate, Date endDate) {
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
     *
     * @return
     */
    public static boolean isChinaTimeZone() {
        return TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
    }

    /**
     * 根据不同时区，转换时间
     *
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

