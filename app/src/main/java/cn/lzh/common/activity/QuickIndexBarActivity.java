package cn.lzh.common.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import cn.lzh.common.R;
import cn.lzh.common.adapter.ContactsAdapter;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.common.bean.GoodMan;
import cn.lzh.common.data.Constants;
import cn.lzh.ui.view.QuickIndexBar;

public class QuickIndexBarActivity extends BaseWatermarkActivity {

	private ListView mLvContacts;
	private QuickIndexBar mQuickIndexBar;
	private TextView mTvCurrentLetter;
	private ArrayList<GoodMan> mGoodMans;

	private static Handler handler=new Handler();
	private ContactsAdapter mContactsAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_index_bar);
		initToolbar(true);
		initData();
		initView();
	}

	private void initData() {
		mGoodMans=new ArrayList<GoodMan>();
		GoodMan goodMan;
		for(int i = 0; i< Constants.NAMES.length; i++){
			goodMan=new GoodMan(Constants.NAMES[i]);
			mGoodMans.add(goodMan);
		}
		Collections.sort(mGoodMans);
	}

	private void initView() {
		mLvContacts = (ListView) findViewById(R.id.lv_contacts);
		mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
		mTvCurrentLetter = (TextView) findViewById(R.id.tv_current_letter);
		mContactsAdapter = new ContactsAdapter(this,mGoodMans);
		mLvContacts.setAdapter(mContactsAdapter);
		mLvContacts.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				char firstLetter=mGoodMans.get(mLvContacts.getFirstVisiblePosition()).getPinyin().charAt(0);
				mQuickIndexBar.setCurrentLetter(String.valueOf(firstLetter));
			}
		});
		mQuickIndexBar.setOnLetterChangeListener(new QuickIndexBar.OnLetterChangeListener() {

			@Override
			public void onLetterChange(String letter) {
				int position=-1;
				for(int i=0;i<mGoodMans.size();i++){
					String currentFirstLetter = mGoodMans.get(i).getPinyin().charAt(0)+"";
					if(TextUtils.equals(letter, currentFirstLetter)){
						position=i;
						break;
					}
				}
				System.out.println("onLetterChange-->letter="+letter+",position="+position);
				if(position==-1){
					//有的拼音字母找不到对应的组
					return;
				}
				mLvContacts.setSelection(position);
				showText(letter);				
			}

			@Override
			public void onActionUp() {
				handler.removeCallbacksAndMessages(null);//保证每次消息队列只有一条最新的消息
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mTvCurrentLetter.setVisibility(View.GONE);
					}
				}, 500);
			}
		});
		mTvCurrentLetter.setVisibility(View.GONE);
	}

	private void showText(String letter) {
		mTvCurrentLetter.setVisibility(View.VISIBLE);
		mTvCurrentLetter.setText(letter);
	}
}
