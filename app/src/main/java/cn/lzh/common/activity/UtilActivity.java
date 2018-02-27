package cn.lzh.common.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.utils.DeviceUtil;
import cn.lzh.utils.SystemUtil;

public class UtilActivity extends BaseWatermarkActivity implements View.OnClickListener{

	private TextView mTvMemory;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uitl);
		initToolbar(true);
		mTvMemory = findViewById(R.id.tv_memory);
		mImageView = findViewById(R.id.round_image_view);
	}

	private void updateMemoryInfo() {
		mTvMemory.setText(String.format(Locale.CHINESE,
				"%dMB", SystemUtil.getDeviceUsableMemory(this)));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_gc:
				SystemUtil.gc(this);
				updateMemoryInfo();
				break;
			case R.id.btn_snap_shot:
				Bitmap bitmap = DeviceUtil.snapShotWithoutStatusBar(this);
				mImageView.setImageBitmap(bitmap);
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateMemoryInfo();
	}
}
