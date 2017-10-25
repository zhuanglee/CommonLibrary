package cn.lzh.common.activity;


import android.os.Bundle;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;

public class CustomVolumControlBarActivity extends BaseWatermarkActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_volum_controlbar);
		initToolbar(true);
	}


}
