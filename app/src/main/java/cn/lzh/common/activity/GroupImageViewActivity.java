package cn.lzh.common.activity;

import android.graphics.Bitmap;
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
import cn.lzh.common.base.BaseWatermarkActivity;
import cn.lzh.utils.ToastUtil;
import cn.lzh.ui.widget.GroupImageView;
import cn.lzh.utils.BitmapCache;
import cn.lzh.utils.BitmapUtil;

/**
 * 群组头像控件演示界面
 */
public class GroupImageViewActivity extends BaseWatermarkActivity {

	private static final int SLEEP_TIME = 1500;

	private GroupImageView mGroupImageView1;
	private GroupImageView mGroupImageView2;

	private Runnable mLoadingImageFormNet;
	private Runnable mLoadingLocalImage;
	private ExecutorService mExecutorService;
	private List<Future> mFutures;

	private boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_imageview);
		initToolbar(true);
		mGroupImageView1 = (GroupImageView) findViewById(R.id.iv_group_for_local);
		mGroupImageView2 = (GroupImageView) findViewById(R.id.iv_group_for_net);
		mLoadingImageFormNet = new LoadingImageFormNet();
		mLoadingLocalImage = new LoadingLocalImage();
		mExecutorService = Executors.newFixedThreadPool(2);
		mFutures = Collections.synchronizedList(new ArrayList<Future>());
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRunning = true;
		if(mFutures.isEmpty()){
			mFutures.add(mExecutorService.submit(mLoadingImageFormNet));
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

	public class LoadingImageFormNet implements Runnable{
		private static final String BASE_URL="http://v.yingsun.net/lbswork";
		private String[] urls = {
				"/upload/photo/small/5863_b2ea91bbdb8f4cb81c0cea2ca2261086.48_48.jpg",
				"/upload/photo/original/5863_ad3c119d89607bca64be7476e189a684.200_200.jpg",
				"/upload/photo/small/4983_d9039f8a5d0e50f5285c5510e9c7ab39.48_48.jpg",
				"/upload/photo/original/4983_4ed2f5b2992b8dc66bd86451b4fbca92.200_200.jpg",
				"/upload/photo/small/5124_de6eeb26c0ce02eeddac19f0285bded9.48_48.png",
				"/upload/photo/original/5124_b4e481d6a95142a78dd3d454dafc724c.375_376.png",
				"/upload/photo/small/3623_cad859b31a35ebf18d3da425095f34de.48_48.png",
				"/upload/photo/original/3623_4e52c7416623955601a28ed45e770335.414_415.png",
				"/upload/photo/original/4258_aedfd17774ec73cc0e4a284fccb49b0c.414_415.png"
		};
		private List<Bitmap> mBitmaps = new ArrayList<>();
		private BitmapCache mBitmapCache = BitmapCache.getInstance();

		@Override
		public void run() {
			String imgUrl;
			Bitmap bitmap;
			while (isRunning) {
				for (String url : urls) {
					try {
						imgUrl = BASE_URL + url;
						bitmap = mBitmapCache.get(imgUrl);
						if (bitmap == null || bitmap.isRecycled()) {
							bitmap = BitmapUtil.getBitmap(new URL(imgUrl));
							if (bitmap == null) {
								bitmap = BitmapUtil.getBitmap(
										GroupImageViewActivity.this, R.drawable.ic_launcher);
							}
							mBitmapCache.put(imgUrl, bitmap);
						}
						mBitmaps.add(bitmap);
						runOnUiThread(new Runnable() {
							public void run() {
								mGroupImageView2.setImageBitmaps(mBitmaps);
							}
						});
						SystemClock.sleep(SLEEP_TIME);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
				mBitmaps.clear();
				runOnUiThread(new Runnable() {
					public void run() {
						mGroupImageView2.setImageResource(R.drawable.user);
					}
				});
				SystemClock.sleep(SLEEP_TIME);
			}
		}
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
						bitmap = BitmapUtil.getBitmap(
								GroupImageViewActivity.this, resId);
						mBitmapCache.put("" + resId, bitmap);
					}
					mBitmaps.add(bitmap);
					runOnUiThread(new Runnable() {
						public void run() {
							mGroupImageView1.setImageBitmaps(mBitmaps);
						}
					});
					SystemClock.sleep(SLEEP_TIME);
				}
				mBitmaps.clear();
				runOnUiThread(new Runnable() {
					public void run() {
						mGroupImageView1.setBackgroundColor(Color.RED);
					}
				});
				SystemClock.sleep(SLEEP_TIME);
			}
		}
	}
}
