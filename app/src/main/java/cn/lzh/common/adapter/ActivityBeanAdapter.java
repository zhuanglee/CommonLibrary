package cn.lzh.common.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.lzh.common.R;
import cn.lzh.common.bean.ActivityBean;

public class ActivityBeanAdapter extends BaseAdapter {
    private List<ActivityBean> mList;

    public ActivityBeanAdapter(@NonNull List<ActivityBean> list) {
        this.mList = list;
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
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_activity, parent, false);
            holder.tvActivityTitle = convertView.findViewById(R.id.tv_activity_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvActivityTitle.setText(mList.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        private TextView tvActivityTitle;
    }
}