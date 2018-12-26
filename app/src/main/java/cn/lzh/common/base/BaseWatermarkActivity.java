package cn.lzh.common.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.lzh.common.utils.WatermarkHelper;

/**
 * 添加水印效果的基类
 *
 * @author lzh
 */
public abstract class BaseWatermarkActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        View inflate = View.inflate(this, layoutResID, null);
        this.setContentView(inflate);
    }

    @Override
    public void setContentView(View view) {
        view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        super.setContentView(view);
        getWindow().getDecorView().setBackgroundDrawable(WatermarkHelper.getWatermarkDrawable(this));
    }

}
