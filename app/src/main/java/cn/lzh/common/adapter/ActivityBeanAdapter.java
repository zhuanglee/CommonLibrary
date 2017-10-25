package cn.lzh.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lzh.common.R;
import cn.lzh.common.bean.ActvityBean;

public class ActivityBeanAdapter extends BaseAdapter {
    private Context context;
    private List<ActvityBean> mList = null;

    public ActivityBeanAdapter(Context context, List<ActvityBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
        holder.tv_activity_name.setText(mList.get(position).getLabel());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_activity_name;
    }
}