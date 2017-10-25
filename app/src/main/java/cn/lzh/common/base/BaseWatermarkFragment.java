package cn.lzh.common.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;

import cn.lzh.utils.BitmapUtil;
import cn.lzh.utils.WatermarkUtil;

public abstract class BaseWatermarkFragment extends Fragment {
	private static final String DIR_DEBUG = "/debug/";
	private static final int WATERMARK_TEXT_SZIE = 80;
	private static final int WATERMARK_TEXT_COLOR = 0x331d9ef1;
	private static String mWatermarkText;
	private static Drawable mWatermarkDrawable;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		initBackground(activity, this.getClass().getSimpleName());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = getContentView(inflater, container, savedInstanceState);
		if (mWatermarkDrawable != null) {
			view.setBackgroundDrawable(mWatermarkDrawable);
		}
		return view;
	}

	protected abstract View getContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	private void initBackground(Context context, String text) {
		if (mWatermarkDrawable != null && mWatermarkText.equals(text)) {
			return;
		}
		mWatermarkText = text;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		File file = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String dir = Environment.getExternalStorageDirectory()
					+ DIR_DEBUG;
			file = new File(String.format(WatermarkUtil.WATERMARK_FILE_FORMAT,
					dir, text));
		}
		WatermarkUtil.Watermark watermark = new WatermarkUtil.Watermark();
		watermark.width = outMetrics.widthPixels;
		watermark.height = outMetrics.heightPixels;
		if (file != null && file.exists()) {
			Bitmap bitmap = BitmapUtil.getBitmap(file);
			if (bitmap != null) {
				int width = outMetrics.widthPixels >> 1;
				int height = bitmap.getHeight() * width / bitmap.getWidth();// 保证等比例缩放
				Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(bitmap,
						width, height);
				if (scaledBitmap != null) {
					bitmap = scaledBitmap;
				}
				mWatermarkDrawable = WatermarkUtil.getWatermarkDrawable(
						bitmap, watermark);
			}
		}
		if (mWatermarkDrawable == null) {
			// 没有获取到水印图片,则自己画
			watermark.text = mWatermarkText;
			watermark.textColor = WATERMARK_TEXT_COLOR;
			watermark.textSize = WATERMARK_TEXT_SZIE;
			mWatermarkDrawable = WatermarkUtil.getWatermarkDrawable(watermark);
		}
	}

	public static Drawable getWatermarkDrawable() {
		return mWatermarkDrawable;
	}

}
