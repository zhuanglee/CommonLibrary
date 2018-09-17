package cn.lzh.common.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.common.databinding.ActivityUtilBinding;
import cn.lzh.common.utils.Test;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.SystemUtil;

public class UtilActivity extends BaseWatermarkActivity implements View.OnClickListener{

	private ActivityUtilBinding mBinding;
	private int mRadius;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_util);
		initToolbar(true);
		Test.testSystemUtil(this);
		mRadius = DeviceUtil.dip2px(this, 5);
		Log.i("UtilActivity", String.format("getStatusBarHeight：1=%d,2=%d",
				DeviceUtil.getStatusBarHeight(getApplicationContext()),
				DeviceUtil.getStatusBarHeight(this)));
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateMemoryInfo();
	}

	private void updateMemoryInfo() {
		mBinding.tvMemory.setText(String.format(Locale.CHINESE,
				"%dMB", SystemUtil.getDeviceUsableMemory(this)));
		mBinding.tvMemory.setTextColor(DrawableUtil.getGradientColor(
				DrawableUtil.getRandomColor(), DrawableUtil.getRandomColor(), 0.5f));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_random:
				mBinding.btnRandom.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(mRadius));
				break;
			case R.id.btn_snap_shot:
				Log.i("UtilActivity", String.format("getStatusBarHeight：1=%d,2=%d",
						DeviceUtil.getStatusBarHeight(getApplicationContext()),
						DeviceUtil.getStatusBarHeight(this)));
				mBinding.imageView.setImageDrawable(null);
				Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
				mBinding.imageView.setImageDrawable(DrawableUtil.getPressedDrawable(new BitmapDrawable(bitmap)));
				mBinding.btnSnapShot.setBackgroundDrawable(DrawableUtil.getSelector(
						DrawableUtil.getRandomGradientDrawable(mRadius),
						DrawableUtil.getRandomGradientDrawable(mRadius)));
				break;
		}
        updateMemoryInfo();
	}

}
