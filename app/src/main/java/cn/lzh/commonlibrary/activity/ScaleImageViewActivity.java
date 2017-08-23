package cn.lzh.commonlibrary.activity;

import android.os.Bundle;
import android.widget.Toast;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.ui.view.ScaleImageView;

public class ScaleImageViewActivity extends BaseWatermarkActivity
{
	ScaleImageView joke;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scale_imageview);

		joke = (ScaleImageView) findViewById(R.id.c_joke);
		joke.setOnClickIntent(new ScaleImageView.OnViewClickListener()
		{

			@Override
			public void onViewClick(ScaleImageView view)
			{
				Toast.makeText(ScaleImageViewActivity.this, "Joke", 1000).show();
			}
		});
	}
	
	
}