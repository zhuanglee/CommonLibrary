package cn.lzh.common.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.ui.widget.FlowLayout;
import cn.lzh.utils.DrawableUtil;
import cn.lzh.utils.ToastUtil;
import cn.lzh.utils.RandomUtil;

/**
 * 流式布局
 *
 * @author lzh
 */
public class FlowLayoutActivity extends BaseWatermarkActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);
        initToolbar(true);
        final ArrayList<String> tags = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            tags.add("标签" + i + RandomUtil.getRandomLetters(i));
        }
        final FlowLayout flowLayout = findViewById(R.id.flow_layout);
        flowLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupFlowLayout(flowLayout, tags);
            }
        }, 1000);
    }

    private void setupFlowLayout(FlowLayout flowLayout, ArrayList<String> tags) {
        int textPaddingH = (int) getResources().getDimension(R.dimen.text_horizontal_padding);
        int textPaddingV = (int) getResources().getDimension(R.dimen.text_vertical_padding);
        float radius = getResources().getDimension(R.dimen.radius);
        //添加子控件
        for (final String string : tags) {
            TextView tv = new TextView(mContext);
            tv.setText(string);
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundDrawable(DrawableUtil.getRandomColorSelector(radius));
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(string);
                }
            });
            flowLayout.addView(tv);
        }
    }

}
