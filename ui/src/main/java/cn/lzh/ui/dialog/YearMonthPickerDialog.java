package cn.lzh.ui.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;


import java.util.Calendar;

import cn.lzh.ui.R;

/**
 * Created by lzh on 2017/9/8.
 * 年月选择器对话框
 */

public class YearMonthPickerDialog extends DatePickerDialog {

    /**
     * 固定的标题
     */
    private String mFixTitle;

    public YearMonthPickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, Calendar calendar) {
        this(context, listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    public YearMonthPickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, R.style.HoloDialog, listener,
                year, month, dayOfMonth);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout mSpinners = findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
        if (mSpinners != null) {
            NumberPicker mYearSpinner = findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
            NumberPicker mMonthSpinner = findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
            mSpinners.removeAllViews();
            if (mYearSpinner != null) {
                mSpinners.addView(mYearSpinner);
            }
            if (mMonthSpinner != null) {
                mSpinners.addView(mMonthSpinner);
            }
        }
        View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
        if(dayPickerView != null){
            dayPickerView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(mFixTitle);
    }

    /**
     * 设置固定的标题
     * @param text
     */
    public YearMonthPickerDialog setFixTitle(String text){
        mFixTitle = text;
        return this;
    }

    /**
     * 设置固定的标题
     * @param resId
     */
    public YearMonthPickerDialog setFixTitle(@StringRes int resId){
        mFixTitle = getContext().getString(resId);
        return this;
    }


    /**
     * 创建日期选择对话框
     */
    public static YearMonthPickerDialog create(Context context, Calendar date, OnDateSetListener listener) {
        if(date == null){
            date = Calendar.getInstance();
        }
        return new YearMonthPickerDialog(context,
                listener, date);
    }

}
