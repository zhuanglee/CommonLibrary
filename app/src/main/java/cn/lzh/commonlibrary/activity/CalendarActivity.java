package cn.lzh.commonlibrary.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.ui.view.CalendarCard;
import cn.lzh.ui.view.CalendarCard.CustomDate;
import cn.lzh.ui.utils.ToastUtil;

public class CalendarActivity extends BaseWatermarkActivity implements OnClickListener, CalendarCard.OnClickCellListener {
  
    private ImageButton preImgBtn, nextImgBtn;  
    private TextView monthText;  
    private CalendarCard mCanlendarCard;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_calendar);  
        preImgBtn = (ImageButton) findViewById(R.id.btnPreMonth);
        nextImgBtn = (ImageButton) findViewById(R.id.btnNextMonth);  
        monthText = (TextView) findViewById(R.id.tvCurrentMonth);  
        mCanlendarCard=(CalendarCard)findViewById(R.id.calendar_card);
        mCanlendarCard.setClickCellListener(this);
        mCanlendarCard.setSelectedDate(new CustomDate());
        preImgBtn.setOnClickListener(this);  
        nextImgBtn.setOnClickListener(this);
    }  
  
    @Override  
    public void onClick(View v) {  
        switch (v.getId()) {  
        case R.id.btnPreMonth:
        	mCanlendarCard.slideToLeft();
            break;  
        case R.id.btnNextMonth:  
        	mCanlendarCard.slideToRight();
            break; 
        }
    }  
  
    @Override  
    public void onClickDate(CustomDate date, CalendarCard.SildeDirection state) {
          ToastUtil.show(date.toString());
    }
  
    @Override  
    public void onChangeCalendar(CustomDate date) {
        monthText.setText(date.year + "年" + date.month + "月");
    }

	@Override
	public boolean isClickable() {
		return true;
	}  
      
}  