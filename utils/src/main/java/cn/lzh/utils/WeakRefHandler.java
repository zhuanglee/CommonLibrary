package cn.lzh.utils;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * 实现回调弱引用的Handler,防止由于内部持有导致的内存泄露<br/>
 * 博客地址：http://www.jianshu.com/p/88cf7a923b56<br/>
 * @author brian512
 */
public class WeakRefHandler extends Handler {
	
    private WeakReference<Callback> mWeakReference;

    public WeakRefHandler() {
	}

	/**
	 *
	 * @param callback 注意callback的生命周期，匿名类和局部变量均会被立即释放
	 */
	public WeakRefHandler(Callback callback) {
    	if(callback != null){
    		mWeakReference = new WeakReference<>(callback);
    	}
    }

	/**
	 * @param looper Looper
	 * @param callback 注意callback的生命周期，匿名类和局部变量均会被立即释放
	 */
	public WeakRefHandler(Looper looper, Callback callback) {
        super(looper);
		if(callback != null){
			mWeakReference = new WeakReference<>(callback);
		}
    }

	/**
	 * 设置回调
	 * @param callback 注意callback的生命周期，匿名类和局部变量均会被立即释放
	 */
	public void setCallback(Callback callback) {
		if(callback == null){
			if(mWeakReference != null){
				mWeakReference.clear();
				mWeakReference = null;
			}
		}else{
    		mWeakReference = new WeakReference<>(callback);
		}
    }

	/**
	 * 不能被子类重写
	 * @param msg Message
	 * @see #setCallback(Callback)
	 */
	@Override
    public final void handleMessage(Message msg) {
        if (mWeakReference == null || mWeakReference.get() == null) {
            Log.e("WeakRefHandler","callback is null");
        }else{
        	Callback callback = mWeakReference.get();
            callback.handleMessage(msg);
        }
    }
}