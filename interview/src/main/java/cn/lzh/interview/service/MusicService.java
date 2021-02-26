package cn.lzh.interview.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by lzh on 2018/9/17.<br/>
 */
public class MusicService extends Service {

    public static final String INTENT_FILTER_ACTION ="cn.lzh.services.music";

    private static final String TAG = "MusicService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControlBinder();
    }

    public static class MusicControlBinder extends Binder {
        public void start() {
            Log.i(TAG, "MusicControlBinder.start");
        }

        public int getProgress() {
            Log.i(TAG, "MusicControlBinder.getProgress");
            return 0;
        }

        public void stop() {
            Log.i(TAG, "MusicControlBinder.stop");
        }

    }

}
