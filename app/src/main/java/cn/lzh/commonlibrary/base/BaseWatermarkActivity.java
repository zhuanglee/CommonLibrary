package cn.lzh.commonlibrary.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.io.File;

import cn.lzh.commonlibrary.R;
import cn.lzh.utils.BitmapUtil;
import cn.lzh.utils.WatermarkTools;

/**
 * 添加水印效果的基类
 *
 * @author lzh
 */
public class BaseWatermarkActivity extends FragmentActivity {

    private static final String DIR_DEBUG = "/debug/";
    private static final int COLOR_TRANSPARENT = 0x0000000;
    private static final int WATERMARK_TEXT_COLOR = 0x331d9ef1;
    private static final int WATERMARK_TEXT_SZIE = 80;
    private static String mWatermarkText;
    /**
     * 水印图片
     */
    private static Drawable mWatermarkDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackground(this, "Android");
    }

    @Override
    public void setContentView(int layoutResID) {
        View inflate = View.inflate(this, layoutResID, null);
        this.setContentView(inflate);
    }

    @Override
    public void setContentView(View view) {
        view.setBackgroundColor(COLOR_TRANSPARENT);
        // resetBG(view);
        super.setContentView(view);
        if (mWatermarkDrawable != null) {
            getWindow().getDecorView()
                    .setBackgroundDrawable(mWatermarkDrawable);
        }
    }

    /**
     * 重设视图背景
     *
     * @param text
     */
    // private void resetBG(View view) {
    // if(view instanceof ViewGroup){
    // ViewGroup viewGroup=(ViewGroup) view;
    // int childCount = viewGroup.getChildCount();
    // View childAt;
    // for(int i=0;i<childCount;i++){
    // childAt = viewGroup.getChildAt(i);
    // Logg.w(BenguoApp.TAG, "childAt="+childAt.getClass().getSimpleName());
    // if(childAt instanceof ViewGroup){
    // childAt.setBackgroundColor(COLOR_TRANSPARENT);
    // resetBG(childAt);
    // }else{
    // childAt.setBackgroundColor(COLOR_TRANSPARENT);
    // }
    // }
    // }else{
    // view.setBackgroundColor(COLOR_TRANSPARENT);
    // }
    // }
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
            file = new File(String.format(WatermarkTools.WATERMARK_FILE_FORMAT,
                    dir, text));
        }
        WatermarkTools.Watermark watermark = new WatermarkTools.Watermark();
        watermark.width = outMetrics.widthPixels;
        watermark.height = outMetrics.heightPixels;
        watermark.offsetY = (int) this.getResources().getDimension(
                R.dimen.title_bar_height);
        if (file != null && file.exists()) {
            Bitmap bitmap = BitmapUtil.getBitmap(context, file);
            if (bitmap != null) {
                int width = outMetrics.widthPixels >> 1;
                int height = bitmap.getHeight() * width / bitmap.getWidth();//保证等比例缩放
                Bitmap scaledBitmap = BitmapUtil.createScaledBitmap(bitmap, width, height);
                if (scaledBitmap != null) {
                    bitmap = scaledBitmap;
                }
                mWatermarkDrawable = WatermarkTools.getWatermarkDrawable(
                        bitmap, watermark);
            }
        }
        if (mWatermarkDrawable == null) {
            // 没有获取到水印图片,则自己画
            watermark.text = mWatermarkText;
//			watermark.textColor=WATERMARK_TEXT_COLOR;
//			watermark.textSize=WATERMARK_TEXT_SZIE;
            mWatermarkDrawable = WatermarkTools.getWatermarkDrawable(watermark);
        }
    }

    public static Drawable getWatermarkDrawable() {
        return mWatermarkDrawable;
    }

}
