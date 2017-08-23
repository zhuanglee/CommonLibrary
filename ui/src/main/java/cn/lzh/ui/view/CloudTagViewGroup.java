package cn.lzh.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CloudTagViewGroup extends CloudViewGroup {


	
	public CloudTagViewGroup(Context context) {
		super(context);
	}

	public CloudTagViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setTags(Context context,List<String> tagStringList,String emptyString){
		setTags(context, tagStringList, emptyString, true);
	}
	
	public void setTags(Context context,List<String> tagStringList,String emptyString,boolean clickable) {
		removeAllViews();
		if (tagStringList == null) {
			tagStringList = new ArrayList<String>();
		}
		if (tagStringList.isEmpty() && emptyString!=null) {
			tagStringList.add(emptyString);
		}
		for (int i = 0; i < tagStringList.size(); i++) {			
			TextView tagItem =  new TextView(context);
			tagItem.setText(tagStringList.get(i));
			tagItem.setFocusable(clickable);
			tagItem.setClickable(clickable);
			addView(tagItem);
		}
	}
	
	public void addTag(Context context,String tag) {
		TextView tagItem =  new TextView(context);
		tagItem.setText(tag);
		tagItem.setFocusable(false);
		tagItem.setClickable(false);
		addView(tagItem);
	}
}
