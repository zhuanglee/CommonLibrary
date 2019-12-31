package cn.lzh.common.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.databinding.ActivityUtilBinding;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.IntentUtil;
import cn.lzh.utils.SystemUtil;

public class UtilActivity extends BaseActivity implements View.OnClickListener {

    private ActivityUtilBinding mBinding;
    private int mRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_util);
        initToolbar(true);
        mRadius = DeviceUtil.dip2px(this, 5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateMemoryInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_wireless_settings:
                startActivity(IntentUtil.openWirelessSettings());
                break;
            case R.id.btn_random:
                mBinding.btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(mRadius));
                break;
            case R.id.btn_snap_shot:
                mBinding.imageView.setImageDrawable(null);
                Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
                mBinding.imageView.setImageDrawable(DrawableUtil.getSelector(bitmap));
                mBinding.btnSnapShot.setBackgroundDrawable(DrawableUtil.getRandomGradientSelector(mRadius));
                break;
        }
        updateMemoryInfo();
    }

    private void updateMemoryInfo() {
        mBinding.tvMemory.setText(String.format(Locale.CHINESE,
                "%dMB", SystemUtil.getDeviceUsableMemory(this)));
        mBinding.tvMemory.setTextColor(DrawableUtil.getGradientColor(
                DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
    }

}
