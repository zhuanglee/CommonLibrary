package cn.lzh.common.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cn.lzh.common.R;
import cn.lzh.common.base.BaseActivity;
import cn.lzh.common.utils.BitmapCache;
import cn.lzh.common.view.GroupImageView;
import cn.lzh.utils.ToastUtil;

/**
 * 群组头像控件演示界面
 */
public class GroupImageViewActivity extends BaseActivity {

    private static final int SLEEP_TIME = 1500;

    private GroupImageView groupImageView;

    private LoadingLocalImage mLoadingLocalImage;
    private ExecutorService mExecutorService;
    private List<Future> mFutures;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_imageview);
        initToolbar(true);
        groupImageView = findViewById(R.id.groupImageView);
        mLoadingLocalImage = new LoadingLocalImage(this, mBitmaps -> runOnUiThread(() -> {
            if (mBitmaps.size() == 1) {
                groupImageView.setImageBitmap(mBitmaps.get(0));
            } else {
                groupImageView.setImageBitmaps(mBitmaps);
            }
        }));
        mExecutorService = Executors.newFixedThreadPool(1);
        mFutures = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoadingLocalImage.isRunning = true;
        if (mFutures.isEmpty()) {
            mFutures.add(mExecutorService.submit(mLoadingLocalImage));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoadingLocalImage.isRunning = false;
        boolean isCanceled = true;
        for (Future mFuture : mFutures) {
            isCanceled &= mFuture.cancel(true);
        }
        if (isCanceled) {
            mFutures.clear();
        } else {
            ToastUtil.show("后台任务仍在执行");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutorService.shutdownNow();
        mExecutorService = null;
        mLoadingLocalImage = null;
    }

    private static class LoadingLocalImage implements Runnable {
        private List<Bitmap> mBitmaps = new ArrayList<>();
        private BitmapCache mBitmapCache = BitmapCache.getInstance();
        private boolean isRunning;
        private Context context;
        private Callback callback;

        LoadingLocalImage(Context context, Callback callback) {
            this.context = context.getApplicationContext();
            this.callback = callback;
        }

        @Override
        public void run() {
            int resId = R.drawable.user;
            while (isRunning) {
                for (int i = 0; i < 9; i++) {
                    if(!isRunning) break;
                    String key = String.valueOf(resId);
                    Bitmap bitmap = mBitmapCache.get(key);
                    if (bitmap == null || bitmap.isRecycled()) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                        mBitmapCache.put(key, bitmap);
                    }
                    mBitmaps.add(bitmap);
                    callback.onImageLoaded(mBitmaps);
                    SystemClock.sleep(SLEEP_TIME);
                }
                mBitmaps.clear();
                SystemClock.sleep(SLEEP_TIME);
            }
        }

        public interface Callback{
            void onImageLoaded(List<Bitmap> mBitmaps);
        }
    }
}
