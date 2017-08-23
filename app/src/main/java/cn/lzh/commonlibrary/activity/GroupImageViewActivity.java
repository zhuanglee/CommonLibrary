package cn.lzh.commonlibrary.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.lzh.commonlibrary.R;
import cn.lzh.commonlibrary.base.BaseWatermarkActivity;
import cn.lzh.ui.view.GroupImageView;
import cn.lzh.utils.BitmapCache;
import cn.lzh.utils.BitmapUtil;

public class GroupImageViewActivity extends BaseWatermarkActivity {
	private static final int SLEEP_TIME = 3000;
	private boolean isRunning = true;
	private GroupImageView iv_group1,iv_group2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_imageview);
		iv_group1 = (GroupImageView) findViewById(R.id.iv_group1);
		iv_group2 = (GroupImageView) findViewById(R.id.iv_group2);
		test1();
		test2();
	}
	private void test1() {
		new Thread() {
			private int[] resIds = { R.drawable.a, R.drawable.b,
					R.drawable.c, R.drawable.d, R.drawable.e,
					R.drawable.f, R.drawable.g, R.drawable.h,
					R.drawable.j };
			private List<Bitmap> mBitmaps = new ArrayList<>();
			private BitmapCache mBitmapCache = BitmapCache.getInstance();
			
			public void run() {
				int length = resIds.length;
				Bitmap bitmap;
				while (isRunning) {
					for (int i = 0; i < length; i++) {
						bitmap = mBitmapCache.get("" + resIds[i]);
						if (bitmap == null || bitmap.isRecycled()) {
							bitmap = BitmapUtil.getBitmap(
									GroupImageViewActivity.this, resIds[i]);
							mBitmapCache.put("" + resIds[i], bitmap);
						}
						mBitmaps.add(bitmap);
						runOnUiThread(new Runnable() {
							public void run() {
								iv_group1.setImageBitmaps(mBitmaps);
							}
						});
						SystemClock.sleep(SLEEP_TIME);
					}
					mBitmaps.clear();
					runOnUiThread(new Runnable() {
						public void run() {
							//TODO 
							iv_group1.setBackgroundColor(Color.RED);
						}
					});
					SystemClock.sleep(SLEEP_TIME);
				}
			};
		}.start();
	}

	/**
	 * 传入图片URL
	 */
	private void test2() {
		new Thread() {
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
			
			public void run() {
				int length = urls.length;
				String imgUrl;
				Bitmap bitmap;
				while (isRunning) {
					for (int i = 0; i < length; i++) {
						try {
							imgUrl = BASE_URL+urls[i];
							bitmap = mBitmapCache.get(imgUrl);
							if (bitmap == null || bitmap.isRecycled()) {
								bitmap =BitmapUtil.getBitmap(GroupImageViewActivity.this, new URL(imgUrl));
								if(bitmap==null){
									bitmap = BitmapUtil.getBitmap(
											GroupImageViewActivity.this, R.drawable.ic_launcher);
								}
								mBitmapCache.put(imgUrl, bitmap);
							}
							mBitmaps.add(bitmap);
							runOnUiThread(new Runnable() {
								public void run() {
									iv_group2.setImageBitmaps(mBitmaps);
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
							iv_group2.setImageResource(R.drawable.user);
						}
					});
					SystemClock.sleep(SLEEP_TIME);
				}
			};
		}.start();
	}
	


	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}

}
