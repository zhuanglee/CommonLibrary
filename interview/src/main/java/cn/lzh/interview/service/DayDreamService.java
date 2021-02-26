package cn.lzh.interview.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.service.dreams.DreamService;
import android.util.Log;

import cn.lzh.interview.R;

/**
 * Created by lzh on 2018/9/13.<br/>
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class DayDreamService extends DreamService {

    private static final String TAG = "DayDreamService";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG, "onAttachedToWindow");
        setInteractive(false);// Exit dream upon user touch
        setFullscreen(true);// Hide system UI
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        Log.i(TAG, "onDreamingStarted");
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        Log.i(TAG, "onDreamingStopped");

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG, "onDetachedFromWindow");
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
