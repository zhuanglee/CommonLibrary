package cn.lzh.interview.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by lzh on 2018/12/25.<br/>
 * 需要动态注册的 Receiver 的基类
 */
public abstract class BaseDynamicReceiver extends BroadcastReceiver {

    /**
     * 注册
     * @param context Context
     */
    public void register(Context context){
        context.registerReceiver(this, getIntentFilter());
    }

    /**
     * 取消注册
     * @param context Context
     */
    public void unregister(Context context){
        context.unregisterReceiver(this);
    }

    /**
     * 获取子类的意图过滤器
     * @return IntentFilter
     */
    protected abstract IntentFilter getIntentFilter();
}
