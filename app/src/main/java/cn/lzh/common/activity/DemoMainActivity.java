package cn.lzh.common.activity;

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

import cn.lzh.common.R;
import cn.lzh.common.adapter.ActivityBeanAdapter;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.common.bean.ActvityBean;
import cn.lzh.utils.logger.LogManager;

/**
 * Demo主界面
 *
 * @author lzh
 */
public class DemoMainActivity extends BaseWatermarkActivity {

    private static final String TAG = "DemoMainActivity";
    private Context mContext;
    private List<ActvityBean> mActivityBeans;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_demo_main);
        initToolbar(false);
        initData();
        initView();
        initListener();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ActivityBeanAdapter(this, mActivityBeans));
    }

    private void initData() {
        mActivityBeans = new ArrayList<>();
        mActivityBeans.add(new ActvityBean(CalendarActivity.class));
        mActivityBeans.add(new ActvityBean(CustomImageViewActivity.class));
        mActivityBeans.add(new ActvityBean(CustomVolumControlBarActivity.class));
        mActivityBeans.add(new ActvityBean(DialogDemoActivity.class));
        mActivityBeans.add(new ActvityBean(FlowLayoutActivity.class));
        mActivityBeans.add(new ActvityBean(GroupImageViewActivity.class));
        mActivityBeans.add(new ActvityBean(ParallaxListViewActivity.class));
        mActivityBeans.add(new ActvityBean(QuickIndexBarActivity.class));
        mActivityBeans.add(new ActvityBean(RingProgressBarActivity.class));
        mActivityBeans.add(new ActvityBean(PageActivity.class));
        mActivityBeans.add(new ActvityBean(UtilActivity.class));
    }

    private void initListener() {
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 下标要减去列表中头布局个数
                int index = position - mListView.getHeaderViewsCount();
                if (index < 0 && index >= mActivityBeans.size()) {
                    return;
                }
                String name = mActivityBeans.get(index).getName();
                if (TextUtils.isEmpty(name)) {
                    LogManager.getLogger().i(TAG, "click "
                            + mActivityBeans.get(position).getLabel());
                } else {
                    try {
                        Class<?> activityClass = Class.forName(name);
                        startActivity(new Intent(mContext, activityClass));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        LogManager.getLogger().i(TAG, name + "不存在");
                    }
                }
            }
        });
    }

}
