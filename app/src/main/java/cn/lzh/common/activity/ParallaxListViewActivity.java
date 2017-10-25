package cn.lzh.common.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.common.data.Constants;
import cn.lzh.common.view.ParallaxListView;

public class ParallaxListViewActivity extends BaseWatermarkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parallax_listview);
		initToolbar(true);
		final View header = View.inflate(this, R.layout.layout_parallax_listview_header, null);
		final ImageView iv_header = (ImageView) header
				.findViewById(R.id.iv_header);
		final ParallaxListView parallaxListView = (ParallaxListView) findViewById(R.id.parallaxListView);
		parallaxListView.addHeaderView(header);
		header.getViewTreeObserver().addOnGlobalLayoutListener(
				//TODO 布局监听
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						parallaxListView.setHeaderImageView(iv_header);
						header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
		parallaxListView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Constants.NAMES){
			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				 TextView tv=(TextView) super.getView(position, convertView, parent);
				 tv.setTextColor(Color.BLACK);
				 return tv;
			}
		});
	}

}
