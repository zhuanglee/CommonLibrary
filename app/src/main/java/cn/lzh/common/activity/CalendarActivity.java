package cn.lzh.common.activity;

import android.os.Bundle;
import android.widget.Toast;

import cn.benguo.calendar.CalendarWidget;
import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;

public class CalendarActivity extends BaseActivity {

    private CalendarWidget calendarWidget;

    public static final String CURRENT_DATE_FORMAT = "current date is %4d-%02d-%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initToolbar(true);
        calendarWidget = (CalendarWidget) findViewById(R.id.calendarWidget);
        calendarWidget.setOnCalendarClickListener((year, month, day) -> {
            Toast.makeText(CalendarActivity.this,
                    String.format(CURRENT_DATE_FORMAT, year, month, day),
                    Toast.LENGTH_SHORT).show();
            if (calendarWidget.isOpen()) {
                calendarWidget.switchOpenStatus();
            }
        });
        findViewById(R.id.btn_today).setOnClickListener(v -> calendarWidget.showTodayPage(true));
    }

}
