package cn.lzh.ui.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


/**
 * 用户实现ListView效果的LinearLayout<br/>
 * Created by lzh on 2017/3/29 11:05.
 */

public class ListLinearLayout extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

	private DataSetObserver mDataSetObserver;
	private BaseAdapter mAdapter;
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;


	public ListLinearLayout(Context context) {
		this(context, null);
	}

	public ListLinearLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ListLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mDataSetObserver = new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				addViews();
			}

			@Override
			public void onInvalidated() {
				super.onInvalidated();
			}
		};
	}

	private void addViews() {
		removeAllViews();
		if(mAdapter != null){
			int count = mAdapter.getCount();
			View view = null;
			for (int i = 0; i < count; i++){
				view = mAdapter.getView(i, null, this);
				view.setOnClickListener(this);
				addView(view);
			}
		}
	}

	public void setAdapter(BaseAdapter adapter){
		if (mAdapter != null && mDataSetObserver != null) {
			mAdapter.unregisterDataSetObserver(mDataSetObserver);
		}
		this.mAdapter = adapter;
		if (mAdapter != null && mDataSetObserver != null) {
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}
		addViews();
	}

	public BaseAdapter getAdapter(){
		return mAdapter;
	}

	public void notifyDataSetChanged(){
		if(mAdapter != null){
			mAdapter.notifyDataSetChanged();
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener){
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener){
		this.mOnItemLongClickListener = listener;
	}

	@Override
	public void onClick(View v) {
		if(mOnItemClickListener != null){
			mOnItemClickListener.onItemClick(this, v, indexOfChild(v));
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(mOnItemLongClickListener == null){
			return false;
		}else{
			return mOnItemLongClickListener.onItemLongClick(
					this, v, indexOfChild(v), v.getId());
		}
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * ViewGroup has been clicked.
	 */
	public interface OnItemClickListener {

		/**
		 * Callback method to be invoked when an item in this ViewGroup has
		 * been clicked.
		 * <p>
		 * Implementers can call getItemAtPosition(position) if they need
		 * to access the data associated with the selected item.
		 *
		 * @param parent The ViewGroup where the click happened.
		 * @param view The view within the AdapterView that was clicked (this
		 *            will be a view provided by the adapter)
		 * @param position The position of the view in the adapter.
		 */
		void onItemClick(ViewGroup parent, View view, int position);
	}

	/**
	 * Interface definition for a callback to be invoked when an item in this
	 * view has been clicked and held.
	 */
	public interface OnItemLongClickListener {
		/**
		 * Callback method to be invoked when an item in this view has been
		 * clicked and held.
		 *
		 * Implementers can call getItemAtPosition(position) if they need to access
		 * the data associated with the selected item.
		 *
		 * @param parent The AbsListView where the click happened
		 * @param view The view within the AbsListView that was clicked
		 * @param position The position of the view in the list
		 * @param id The row id of the item that was clicked
		 *
		 * @return true if the callback consumed the long click, false otherwise
		 */
		boolean onItemLongClick(ViewGroup parent, View view, int position, long id);
	}

}
