package cn.lzh.common.activity;

import android.os.Bundle;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.widget.PageWidget;
import cn.lzh.utils.BitmapUtil;

public class PageActivity extends BaseWatermarkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);
		initToolbar(true);

		PageWidget pageWidget = findViewById(R.id.page_widget);
		pageWidget.setBitmaps(BitmapUtil.getBitmap(this, R.drawable.a),
				BitmapUtil.getBitmap(this, R.drawable.f),
				BitmapUtil.getBitmap(this, R.drawable.h));
	}

}
