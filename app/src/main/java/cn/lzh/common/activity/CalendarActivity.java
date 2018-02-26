package cn.lzh.common.activity;

import android.os.Bundle;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.view.CalendarCard;
import cn.lzh.ui.view.CalendarCard.CustomDate;
import cn.lzh.ui.utils.ToastUtil;
import cn.lzh.ui.view.PageWidget;
import cn.lzh.utils.BitmapUtil;

public class CalendarActivity extends BaseWatermarkActivity implements CalendarCard.OnClickCellListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_calendar);
        initToolbar(true);
        CalendarCard calendarCard = (CalendarCard) findViewById(R.id.calendar_card);
        calendarCard.setClickCellListener(this);
        calendarCard.setSelectedDate(new CustomDate());
    }

    @Override  
    public void onClickDate(CustomDate date, CalendarCard.SlideDirection state) {
          ToastUtil.show(date.toString());
    }
  
    @Override  
    public void onChangeCalendar(CustomDate date) {
        setTitle(date.year + "年" + date.month + "月");
    }

	@Override
	public boolean isClickable() {
		return true;
	}  
      
}  