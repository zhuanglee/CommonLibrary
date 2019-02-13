package cn.lzh.utils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static cn.lzh.utils.CalendarUtil.WEEK_DAYS;
import static cn.lzh.utils.DateUtil.*;

/**
 * Created by lzh on 2017/9/4.
 */

public class CalendarTest {

    @Test
    public void testScheduling() {
        Calendar calendar = Calendar.getInstance();
        int daysOfMonth = CalendarUtil.getDaysOfMonth(calendar);
        int dayOfWeek = CalendarUtil.getWeekDayOfMonthFirstDay(calendar);
        System.out.println("本月第一天是星期" + WEEK_DAYS[dayOfWeek % 7]);
        List<String> weekdays = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (int i = 1; i <= daysOfMonth; i++, dayOfWeek++) {
            weekdays.add(WEEK_DAYS[dayOfWeek % 7]);
            days.add(String.valueOf(i));
            System.out.print(weekdays.get(i - 1) + "(" + days.get(i - 1) + ")");
        }
        System.out.println();
    }

    @Test
    public void testCompare() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_DATE_TIME);
        System.out.println(formatter.format(getCalendar(1546927777419L).getTime()));
        System.out.println(formatter.format(getCalendar(1546927776419L).getTime()));
        assert compare(1546927776419L, 1546927777419L) == -1;
        assert compare(1546927777419L, 1546927776419L) == 1;
        assert compare(1546927777419L, 1546927777419L) == 0;
    }

}
