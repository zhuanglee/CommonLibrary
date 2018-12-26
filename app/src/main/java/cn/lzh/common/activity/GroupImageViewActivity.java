package cn.lzh.common.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.common.utils.BitmapCache;
import cn.lzh.common.view.GroupImageView;
import cn.lzh.utils.ToastUtil;
import cn.lzh.utils.BitmapUtil;

/**
 * 群组头像控件演示界面
 */
public class GroupImageViewActivity extends BaseActivity {

	private static final int SLEEP_TIME = 1500;

	private GroupImageView mGroupImageView1;

	private Runnable mLoadingLocalImage;
	private ExecutorService mExecutorService;
	private List<Future> mFutures;

	private boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_imageview);
		initToolbar(true);
		mGroupImageView1 = findViewById(R.id.iv_group_for_local);
		mLoadingLocalImage = new LoadingLocalImage();
		mExecutorService = Executors.newFixedThreadPool(2);
		mFutures = Collections.synchronizedList(new ArrayList<Future>());
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRunning = true;
		if(mFutures.isEmpty()){
			mFutures.add(mExecutorService.submit(mLoadingLocalImage));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		isRunning = false;
		boolean isCanceled = true;
		for (Future mFuture : mFutures) {
			isCanceled &= mFuture.cancel(true);
		}
		if(isCanceled){
			mFutures.clear();
		}else{
			ToastUtil.show("后台任务仍在执行");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mExecutorService.shutdownNow();
		mExecutorService = null;
	}

	public class LoadingLocalImage implements Runnable{
		private int[] resIds = { R.drawable.a, R.drawable.b,
				R.drawable.c, R.drawable.d, R.drawable.e,
				R.drawable.f, R.drawable.g, R.drawable.h,
				R.drawable.j };
		private List<Bitmap> mBitmaps = new ArrayList<>();
		private BitmapCache mBitmapCache = BitmapCache.getInstance();

		@Override
		public void run() {
			Bitmap bitmap;
			while (isRunning) {
				for (int resId : resIds) {
					bitmap = mBitmapCache.get("" + resId);
					if (bitmap == null || bitmap.isRecycled()) {
						bitmap = BitmapFactory.decodeResource(getResources(), resId);
						mBitmapCache.put("" + resId, bitmap);
					}
					mBitmaps.add(bitmap);
					runOnUiThread(() -> mGroupImageView1.setImageBitmaps(mBitmaps));
					SystemClock.sleep(SLEEP_TIME);
				}
				mBitmaps.clear();
				runOnUiThread(() -> mGroupImageView1.setBackgroundColor(Color.RED));
				SystemClock.sleep(SLEEP_TIME);
			}
		}
	}
}
