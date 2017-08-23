package cn.lzh.commonlibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.bean.ActvityBean;

public class ActivityBeanAdapter extends BaseAdapter {
    private Context context;
    private List<ActvityBean> datas = null;

    public ActivityBeanAdapter(Context context, List<ActvityBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listitem_activity_bean, null);
            holder.tv_activity_name = (TextView) convertView.findViewById(R.id.tv_activity_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_activity_name.setText(datas.get(position).getLabel());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_activity_name;
    }
}