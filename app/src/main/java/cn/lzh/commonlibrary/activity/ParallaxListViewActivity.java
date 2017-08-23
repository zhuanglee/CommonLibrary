package cn.lzh.commonlibrary.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.commonlibrary.data.Constants;
import cn.lzh.commonlibrary.view.ParallaxListView;

public class ParallaxListViewActivity extends BaseWatermarkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parallax_listview);
		final View view_lvheader = View.inflate(this, R.layout.layout_parallax_listview_header, null);
		final ImageView iv_header = (ImageView) view_lvheader
				.findViewById(R.id.iv_header);
		final ParallaxListView parallaxListView = (ParallaxListView) findViewById(R.id.parallaxListView);
		parallaxListView.addHeaderView(view_lvheader);
		view_lvheader.getViewTreeObserver().addOnGlobalLayoutListener(
				//TODO 布局监听
				new OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						parallaxListView.setHeaderImageView(iv_header);
						view_lvheader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
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
