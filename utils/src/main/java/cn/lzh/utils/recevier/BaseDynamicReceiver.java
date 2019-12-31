package cn.lzh.utils.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by lzh on 2018/12/25.<br/>
 * 需要动态注册的 Receiver 的基类
 */
public abstract class BaseDynamicReceiver extends BroadcastReceiver {

    private boolean registered;

    /**
     * 注册
     * @param context Context
     */
    public void register(Context context){
        if(!registered){
            context.registerReceiver(this, getIntentFilter());
            registered = true;
        }
    }

    /**
     * 取消注册
     * @param context Context
     */
    public void unregister(Context context){
        if(registered){
            context.unregisterReceiver(this);
            registered = false;
        }
    }

    /**
     * 获取子类的意图过滤器
     * @return IntentFilter
     */
    protected abstract IntentFilter getIntentFilter();
}
