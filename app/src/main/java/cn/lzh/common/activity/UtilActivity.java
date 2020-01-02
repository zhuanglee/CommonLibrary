package cn.lzh.common.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.IntentUtil;
import cn.lzh.utils.SystemUtil;

public class UtilActivity extends BaseActivity implements View.OnClickListener {

    private int mRadius;
    private Button btnRandom;
    private TextView tvMemory;
    private Button btnSnapShot;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);
        initToolbar(true);
        initView();
        mRadius = DeviceUtil.dip2px(this, 5);
    }

    private void initView() {
        btnRandom = findViewById(R.id.btn_random);
        tvMemory = findViewById(R.id.tv_memory);
        btnSnapShot = findViewById(R.id.btn_snap_shot);
        imageView = findViewById(R.id.image_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_wireless_settings:
                startActivity(IntentUtil.openWirelessSettings());
                break;
            case R.id.btn_random:
                btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(mRadius));
                break;
            case R.id.btn_snap_shot:
                imageView.setImageDrawable(null);
                Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
                imageView.setImageDrawable(DrawableUtil.getSelector(bitmap));
                btnSnapShot.setBackgroundDrawable(DrawableUtil.getRandomGradientSelector(mRadius));
                break;
        }
        updateMemoryInfo();
    }

    private void updateMemoryInfo() {
        tvMemory.setText(String.format(Locale.CHINESE,
                "%dMB", SystemUtil.getDeviceUsableMemory(this)));
        tvMemory.setTextColor(DrawableUtil.getGradientColor(
                DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
    }

}
