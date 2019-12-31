package cn.lzh.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cn.lzh.ui.R;

/**
 * Created by lzh on 2017/9/8.
 */

public class DateTimePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private OnDateTimeChangeListener mOnDateTimeChangeListener;
    private Calendar mCalendar;

    /**
     * 创建选择时间对话框
     * @param context Context
     * @param calendar Calendar
     * @param listener OnDateTimeChangeListener
     * @return DateTimePickerDialog
     */
    public static DateTimePickerDialog create(Context context, Calendar calendar,
            DateTimePickerDialog.OnDateTimeChangeListener listener) {
        return new DateTimePickerDialog(context, calendar, listener);
    }

    /**
     * 创建选择时间对话框
     * @param context Context
     * @param date Date
     * @param listener OnDateTimeChangeListener
     * @return DateTimePickerDialog
     */
    public static DateTimePickerDialog create(Context context, Date date,
                                              DateTimePickerDialog.OnDateTimeChangeListener listener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new DateTimePickerDialog(context, calendar, listener);
    }

    public DateTimePickerDialog(@NonNull Context context,
                                Calendar calendar, OnDateTimeChangeListener listener) {
        this(context, R.style.Theme_AppCompat_Light_Dialog, calendar, listener);
    }

    public DateTimePickerDialog(@NonNull Context context, int themeResId,
                                 Calendar calendar, OnDateTimeChangeListener listener) {
        super(context, themeResId);
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        this.mCalendar = calendar;
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_date_time_picker, null);
        mDatePicker = view.findViewById(R.id.datePicker);
        mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mTimePicker = view.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minute);
        mTimePicker.setOnTimeChangedListener(this);
        setView(view);
        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), this);
        setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), this);
        mOnDateTimeChangeListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relayoutDatePicker();
        relayoutTimePicker();
    }

    /**
     * 对日期选择器组件进行重新布局
     */
    private void relayoutDatePicker() {
        LinearLayout spinners = findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
        if (spinners != null) {
            View yearPickerView = findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
            View monthPickerView = findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
            View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));

            spinners.removeAllViews();
            int yearWidth = getContext().getResources().getDimensionPixelSize(R.dimen.year_width);
            int monthWidth = getContext().getResources().getDimensionPixelSize(R.dimen.moth_width);
            int dayWidth = getContext().getResources().getDimensionPixelSize(R.dimen.day_width);
            int margin = getContext().getResources().getDimensionPixelSize(R.dimen.divider_1dp);
            LinearLayout.LayoutParams params;
            if (yearPickerView != null) {
                params = new LinearLayout.LayoutParams(yearWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                spinners.addView(yearPickerView, params);
            }
            if (monthPickerView != null) {
                params = new LinearLayout.LayoutParams(monthWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = margin;
                params.rightMargin = margin;
                spinners.addView(monthPickerView, params);
            }
            if (dayPickerView != null) {
                params = new LinearLayout.LayoutParams(dayWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                spinners.addView(dayPickerView, params);
            }
        }
    }

    /**
     * 对时间选择器组件进行重新布局
     */
    private void relayoutTimePicker() {
        View hourPickerView = findViewById(getContext().getResources().getIdentifier("android:id/hour", null, null));
        View minutePickerView = findViewById(getContext().getResources().getIdentifier("android:id/minute", null, null));
        if (hourPickerView != null) {
            hourPickerView.getLayoutParams().width = getContext().getResources().getDimensionPixelSize(R.dimen.hour_width);
        }
        if (minutePickerView != null) {
            minutePickerView.getLayoutParams().width = getContext().getResources().getDimensionPixelSize(R.dimen.minute_width);
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        if (which == BUTTON_POSITIVE) {
            if (mOnDateTimeChangeListener != null) {
                mOnDateTimeChangeListener.onChanged(mCalendar);
            }
        }
    }

    public TimePicker getTimePicker() {
        return mTimePicker;
    }

    public DatePicker getDatePicker() {
        return mDatePicker;
    }

    /**
     * 设置标题
     * @param titleId 标题
     * @return DateTimePickerDialog
     */
    public DateTimePickerDialog title(@StringRes int titleId){
        super.setTitle(titleId);
        return this;
    }

    /**
     * 设置标题
     * @param title 标题
     * @return DateTimePickerDialog
     */
    public DateTimePickerDialog title(String title){
        super.setTitle(title);
        return this;
    }

    public interface OnDateTimeChangeListener {
        void onChanged(Calendar calendar);
    }

}
