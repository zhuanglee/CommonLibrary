package cn.lzh.commonlibrary.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.bean.GoodMan;

public class ContactsAdapter extends BaseAdapter implements ListAdapter {
	private Context mContext;
	private ArrayList<GoodMan> mGoodMans;
	private String mCurrentFirstLetter,mLastFirstLetter;

	public ContactsAdapter(Context context, ArrayList<GoodMan> goodMans) {
		mContext=context;
		mGoodMans=goodMans;
	}

	@Override
	public int getCount() {
		return mGoodMans.size();
	}

	@Override
	public Object getItem(int position) {
		return mGoodMans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=View.inflate(mContext, R.layout.listitem_textview, null);
			viewHolder=new ViewHolder();
			viewHolder.mTvFirstLetter=(TextView) convertView.findViewById(R.id.tv_first_letter);
			viewHolder.mTvName=(TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(position>0){
			mLastFirstLetter=mGoodMans.get(position-1).getPinyin().charAt(0)+"";
		}else{
			mLastFirstLetter="";
		}
		GoodMan goodMan = mGoodMans.get(position);
		mCurrentFirstLetter = String.valueOf(goodMan.getPinyin().charAt(0));
		viewHolder.mTvFirstLetter.setVisibility(TextUtils.equals(mLastFirstLetter, mCurrentFirstLetter)?View.GONE:View.VISIBLE);
		viewHolder.mTvFirstLetter.setText(mCurrentFirstLetter);
		viewHolder.mTvName.setText(goodMan.getName());
		return convertView;
	}

	private static class ViewHolder{
		TextView mTvFirstLetter;
		TextView mTvName;
	}
	
}
