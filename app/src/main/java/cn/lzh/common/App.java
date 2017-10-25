package cn.lzh.common;

import android.app.Application;

import cn.lzh.ui.utils.ToastUtil;

/**
 * Created by lzh on 2017/10/20.<br/>
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
    }
}
