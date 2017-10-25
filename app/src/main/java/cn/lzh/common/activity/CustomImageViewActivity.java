package cn.lzh.common.activity;

import android.os.Bundle;
import android.view.Menu;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;

public class CustomImageViewActivity extends BaseWatermarkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_image_view);
		initToolbar(true);
	}

}
