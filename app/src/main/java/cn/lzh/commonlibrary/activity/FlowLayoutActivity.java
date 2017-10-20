package cn.lzh.commonlibrary.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.ui.view.FlowLayout;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.ui.utils.ToastUtil;

/**
 * 流式布局
 * @author lzh
 *
 */
public class FlowLayoutActivity extends BaseWatermarkActivity {

	
	
	private Context mContext;
	private int mTextPaddingH;
	private int mTextPaddingV;
	private float mHotRadius;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mTextPaddingH=(int) getResources().getDimension(R.dimen.hot_text_hpading);
		mTextPaddingV=(int) getResources().getDimension(R.dimen.hot_text_vpading);
		mHotRadius = getResources().getDimension(R.dimen.hot_radius);
		FlowLayout flowLayout=new FlowLayout(mContext);
		setContentView(flowLayout);
		//添加子控件
		ArrayList<String> mList=new ArrayList<String>();
		for(int i=0;i<20;i++){
			mList.add("标签"+i);
		}
		for (final String string : mList ) {
			TextView tv=new TextView(mContext);
			tv.setText(string);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setPadding(mTextPaddingH,mTextPaddingV, mTextPaddingH, mTextPaddingV);
			tv.setGravity(Gravity.CENTER);
			tv.setBackgroundDrawable(DrawableUtil.getBtnSelectorRandomColor(mHotRadius));
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ToastUtil.show(string);
				}
			});
			flowLayout.addView(tv);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
