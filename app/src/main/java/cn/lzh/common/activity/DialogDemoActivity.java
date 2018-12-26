package cn.lzh.common.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.dialog.BottomDialog;
import cn.lzh.ui.dialog.ContextDialog;
import cn.lzh.ui.dialog.DateTimePickerDialog;
import cn.lzh.ui.dialog.SmartPopupWindow;
import cn.lzh.ui.dialog.WaitingDialog;
import cn.lzh.ui.dialog.YearMonthPickerDialog;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.ToastUtil;
import cn.lzh.utils.DateUtil;

/**
 * Created by lzh on 2018/2/26.<br/>
 * 对话框效果演示
 */
public class DialogDemoActivity extends BaseActivity implements View.OnClickListener{

    private SmartPopupWindow popupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_demo);
        initToolbar(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_bottom:
                BottomDialog.create(this, R.layout.dialog_content_view, true).show();
                break;
            case R.id.btn_context:
                ContextDialog.create(this, "Title", "dialog message", true)
                        .setNegativeButton()
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.show(android.R.string.ok);
                            }
                        })
                        .show();
                break;
            case R.id.btn_date_time_picker:
                DateTimePickerDialog.create(this, Calendar.getInstance(), new DateTimePickerDialog.OnDateTimeChangeListener() {
                    @Override
                    public void onChanged(Calendar calendar) {
                        ToastUtil.show(DateUtil.formatDateTime(calendar.getTime()));
                    }
                }).show();
                break;
            case R.id.btn_waiting:
                WaitingDialog.show(this, "Title", "loading...", true, true);
                break;
            case R.id.btn_year_moth_picker:
                YearMonthPickerDialog.create(this, Calendar.getInstance(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ToastUtil.show(year + "年" + month + "月" + dayOfMonth + "日");
                    }
                }).show();
                break;
            case R.id.btn_popup_window:
                if(popupWindow == null){
                    popupWindow = new SmartPopupWindow(this,
                            R.layout.dialog_content_view, DeviceUtil.getScreenWidth(this), DeviceUtil.dip2px(this, 200));
                }
                popupWindow.showScreenCenter();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
