package cn.lzh.common.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.WorkerThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.databinding.ActivityWatermarkImageViewBinding;

/**
 * 在MainActivity中点一个按钮, 可以在图片显示框中添加一个矩形区域，对已经添加的矩形区域可点击选中
 * （选中与未选中样式有所区别）并可以移动该区域, 可以改变区域大小。矩形区域不可移出到图片显示框之外.
 * (提示: 一种实现方式是可以继承 ImageView, 做一个自定义控件 MyImageView.)
 *
 * 在图片框中显示一张图片，再添加一个"保存"按钮，点击时保存显示的图片到手机存储，并可将之前添加的矩形区域作为水印添加在图片中.
 * 保存图片的分辨率大小为高度 1080 像素, 宽度按比例自适应计算.
 */
public class WatermarkImageViewActivity extends BaseActivity implements View.OnClickListener {

    private ActivityWatermarkImageViewBinding binding;
    private ExecutorService executor;
    private ProgressDialog dialog;
    private File lastSaveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executor = Executors.newSingleThreadExecutor();
        binding = ActivityWatermarkImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar(true);
        binding.btnAdd.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.btnShow.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add) {
            binding.myImageView.addRect();
        } else if (id == R.id.btn_save) {
            actionSave();
        } else if (id == R.id.btn_show) {
            if(lastSaveFile == null){
                Toast.makeText(this, "需先保存图片", Toast.LENGTH_SHORT).show();
            }else{
                binding.myImageView.setImageURI(Uri.fromFile(lastSaveFile));
            }
        } else if (id == R.id.btn_clear) {
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            ColorDrawable drawable = new ColorDrawable(getResources().getColor(typedValue.resourceId));
            binding.myImageView.setImageDrawable(drawable);
        }
    }

    /**
     * 保存操作
     */
    private void actionSave() {
        dialog = ProgressDialog.show(this, "", "正在保存图片…");
        binding.myImageView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = binding.myImageView.getDrawingCache();
        executor.execute(() -> {
            // 这里只是将图片保存到手机存储, 并没有添加到图库（需要申请存储权限）
            String filename = UUID.randomUUID().toString();
            File file = new File(getFilesDir(), filename);
            boolean result = saveImage(resizeImage(drawingCache), file);
            runOnUiThread(() -> {
                dialog.dismiss();
                if(result){
                    // 缓存前一次保存的图片文件地址
                    lastSaveFile = file;
                }
                Toast.makeText(this,
                        result ? "保存成功" : "保存失败", Toast.LENGTH_SHORT).show();
                binding.myImageView.setDrawingCacheEnabled(false);
            });
        });
    }

    /**
     * 修改图片分辨率大小为高度 1080 像素, 宽度按比例自适应计算.
     * @param bitmap Bitmap
     * @return Bitmap
     */
    private Bitmap resizeImage(Bitmap bitmap) {
        int h = 1080;
        float rate = (float) bitmap.getWidth() / (float) bitmap.getHeight();
        int w = (int) (rate * h);
        return Bitmap.createScaledBitmap(bitmap, w, h, false);
    }

    @WorkerThread
    private boolean saveImage(Bitmap bitmap, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            return bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
        // 关闭线程池
        executor.shutdownNow();
        executor = null;
    }
}