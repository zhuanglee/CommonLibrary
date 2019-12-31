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

    private List<ActivityBean> mActivityBeans = Arrays.asList(
            new ActivityBean(UtilActivity.class),
            new ActivityBean(GroupImageViewActivity.class),
            new ActivityBean(DialogDemoActivity.class),
            new ActivityBean(FlowLayoutActivity.class),
            new ActivityBean(RingProgressBarActivity.class)
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_main);
        initToolbar(false);
        initView();
    }

    private void initView() {
        Collections.sort(mActivityBeans);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new ActivityBeanAdapter(mActivityBeans));
        listView.setOnItemClickListener((parent, view, position, id) ->
        {
            ActivityBean bean = mActivityBeans.get(position);
            startActivity(new Intent(this, bean.getClazz())
                    .putExtra("title", bean.getName()));
        });
    }

}
