package cn.lzh.interview.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by lzh on 2018/9/3.<br/>
 */
public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";

    public DownloadService() {
        super(TAG);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent：" + Thread.currentThread().getName());
        if (intent == null) {
            Log.i(TAG, "intent is null");
            return;
        }
        String url = intent.getStringExtra("url");
        Log.i(TAG, "url=" + url);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
