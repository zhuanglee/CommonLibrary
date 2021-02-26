package cn.lzh.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.lzh.common.R;
import cn.lzh.common.adapter.ActivityBeanAdapter;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.bean.ActivityBean;

/**
 * Demo 主界面
 *
 * @author lzh
 */
public class DemoMainActivity extends BaseActivity {

    private final List<ActivityBean> activityBeans = Arrays.asList(
            new ActivityBean(CalendarActivity.class),
            new ActivityBean(DialogDemoActivity.class),
            new ActivityBean(FlowLayoutActivity.class),
            new ActivityBean(GestureLockActivity.class),
            new ActivityBean(GroupImageViewActivity.class),
            new ActivityBean(RingProgressBarActivity.class),
            new ActivityBean(UtilActivity.class),
            new ActivityBean(WatermarkImageViewActivity.class)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        initToolbar(false);
        initView();
    }

    private void initView() {
        Collections.sort(activityBeans);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ActivityBeanAdapter(activityBeans));
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            ActivityBean bean = activityBeans.get(position);
            startActivity(new Intent(this, bean.getClazz())
                    .putExtra("title", bean.getName()));
        });
    }

}
