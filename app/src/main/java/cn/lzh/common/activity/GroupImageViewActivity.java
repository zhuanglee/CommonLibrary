package cn.lzh.common.activity;

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

    private Runnable mLoadingLocalImage;
    private ExecutorService mExecutorService;
    private List<Future> mFutures;

    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_imageview);
        initToolbar(true);
        groupImageView = findViewById(R.id.groupImageView);
        mLoadingLocalImage = new LoadingLocalImage();
        mExecutorService = Executors.newFixedThreadPool(1);
        mFutures = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        if (mFutures.isEmpty()) {
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
    }

    public class LoadingLocalImage implements Runnable {
        private List<Bitmap> mBitmaps = new ArrayList<>();
        private BitmapCache mBitmapCache = BitmapCache.getInstance();

        @Override
        public void run() {
            int resId = R.drawable.user;
            while (isRunning) {
                for (int i = 0; i < 9; i++) {
                    String key = String.valueOf(resId);
                    Bitmap bitmap = mBitmapCache.get(key);
                    if (bitmap == null || bitmap.isRecycled()) {
                        bitmap = BitmapFactory.decodeResource(getResources(), resId);
                        mBitmapCache.put(key, bitmap);
                    }
                    mBitmaps.add(bitmap);
                    runOnUiThread(() -> {
                        if (mBitmaps.size() == 1) {
                            groupImageView.setImageBitmap(mBitmaps.get(0));
                        } else {
                            groupImageView.setImageBitmaps(mBitmaps);
                        }
                    });
                    SystemClock.sleep(SLEEP_TIME);
                }
                mBitmaps.clear();
                SystemClock.sleep(SLEEP_TIME);
            }
        }
    }
}
