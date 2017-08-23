package cn.lzh.commonlibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.adapter.ActivityBeanAdapter;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.commonlibrary.bean.ActvityBean;

/**
 * Demo主界面
 * @author lzh
 *
 */
public class DemoMainActivity extends BaseWatermarkActivity {

	private Context mContext;
	private List<ActvityBean> datas;
	private ActivityBeanAdapter myAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_demo_main);
		initData();
		initView();
		initListener();
	}

	private void initView() {
		myAdapter = new ActivityBeanAdapter(this, datas);
		mListView = (ListView) findViewById(R.id.refreshListView);
		mListView.setAdapter(myAdapter);
	}

	private void initData() {
		datas = new ArrayList<ActvityBean>();
		datas.add(new ActvityBean(CalendarActivity.class));
		datas.add(new ActvityBean(FlowLayoutActivity.class));
		datas.add(new ActvityBean(QuickIndexBarActivity.class));
		datas.add(new ActvityBean(CustomImageViewActivity.class));
		datas.add(new ActvityBean(CustomVolumControlBarActivity.class));
		datas.add(new ActvityBean(ScaleImageViewActivity.class));
		datas.add(new ActvityBean(ParallaxListViewActivity.class));
		datas.add(new ActvityBean(RoundProgressBarActivity.class));
	}

	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 下标要减去列表中头布局个数
				int index=position-mListView.getHeaderViewsCount();
				if(index<0&&index>=datas.size()){
					return;
				}
				String name = datas.get(index).getName();
				if (TextUtils.isEmpty(name)) {
					System.out.println("click "
							+ datas.get(position).getLabel());
				} else {
					try {
						Class<?> activityClass = Class.forName(name);
						startActivity(new Intent(mContext, activityClass));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						System.out.println(name + "不存在");
					}
				}
			}
		});
//		mListView
//				.setOnPullDownRefreshListener(new OnPullDownRefreshListener() {
//					@Override
//					public void onRefresh() {
//						LogUtils.debug(mContext, "onRefresh");
//						new Handler().postDelayed(new Runnable() {
//							@Override
//							public void run() {
//								datas.add(0, new ActvityBean("onRefresh:"
//										+ DateUtils.formatDate(new Date())));
//								myAdapter.notifyDataSetChanged();
//								mListView.onFinishRefresh();
//							}
//						}, 3000);
//					}
//				});
//		mListView.setOnLoadingMoreListener(new OnLoadingMoreListener() {
//			@Override
//			public void onLoadingMore() {
//				LogUtils.debug(mContext, "onLoadingMore");
//				new Handler().postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						datas.add(new ActvityBean("onLoadingMore:"
//								+ DateUtils.formatDate(new Date())));
//						myAdapter.notifyDataSetChanged();
//						mListView.onFinishLoadingMore();
//					}
//				}, 3000);
//			}
//		});
	}

}
