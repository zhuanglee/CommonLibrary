package cn.lzh.common.activity;

import android.os.Bundle;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.view.CalendarCard;
import cn.lzh.ui.view.CalendarCard.CustomDate;
import cn.lzh.ui.utils.ToastUtil;

public class CalendarActivity extends BaseWatermarkActivity implements CalendarCard.OnClickCellListener {
  
    private CalendarCard mCalendarCard;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_calendar);
        initToolbar(true);
        mCalendarCard =(CalendarCard)findViewById(R.id.calendar_card);
        mCalendarCard.setClickCellListener(this);
        mCalendarCard.setSelectedDate(new CustomDate());
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