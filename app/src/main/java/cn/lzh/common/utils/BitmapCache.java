package cn.lzh.common.utils;

import android.graphics.Bitmap;

import cn.lzh.utils.AbstractLruCache;

/**
 * Created by lzh on 2018/6/12.<br/>
 */
public class BitmapCache extends AbstractLruCache<Bitmap> {
    @Override
    protected int getSizeOf(Bitmap value) {
        return value.getRowBytes()*value.getHeight();
    }

    private static BitmapCache mInstance;

    public static BitmapCache getInstance() {
        if(mInstance == null){
            synchronized (BitmapCache.class){
                if(mInstance == null){
                    mInstance = new BitmapCache();
                }
            }
        }
        return mInstance;
    }
}
