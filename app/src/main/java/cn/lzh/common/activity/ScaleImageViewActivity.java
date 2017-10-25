package cn.lzh.common.activity;

import android.os.Bundle;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.utils.ToastUtil;
import cn.lzh.ui.view.ScaleImageView;

public class ScaleImageViewActivity extends BaseWatermarkActivity
{
	ScaleImageView joke;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scale_imageview);
		initToolbar(true);
		joke = (ScaleImageView) findViewById(R.id.c_joke);
		joke.setOnClickIntent(new ScaleImageView.OnViewClickListener()
		{

			@Override
			public void onViewClick(ScaleImageView view)
			{
				ToastUtil.show("joke");
			}
		});
	}
	
	
}