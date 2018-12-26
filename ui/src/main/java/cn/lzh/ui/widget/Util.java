package cn.lzh.ui.widget;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by lzh on 2018/12/26.<br/>
 */
final class Util {

    public static int dip2px(@NonNull Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
