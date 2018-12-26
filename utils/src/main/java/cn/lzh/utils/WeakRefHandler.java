package cn.lzh.utils;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 实现回调弱引用的Handler，防止由于内部持有导致的内存泄露<br/>
 * 博客地址：http://www.jianshu.com/p/88cf7a923b56<br/>
 * @author from open source
 */
public class WeakRefHandler extends Handler {
	
    private WeakReference<Callback> mWeakReference;

	/**
	 * @param callback 注意callback的生命周期，匿名类和局部变量均会被立即释放
	 */
	public WeakRefHandler(@NonNull Callback callback) {
		mWeakReference = new WeakReference<>(callback);
    }

	/**
	 * @param looper Looper
	 * @param callback 注意callback的生命周期，匿名类和局部变量均会被立即释放
	 */
	public WeakRefHandler(Looper looper,@NonNull Callback callback) {
        super(looper);
		mWeakReference = new WeakReference<>(callback);
    }

	/**
	 * 不能被子类重写
	 * @param msg Message
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