package cn.lzh.common.bean;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Activity 信息
 *
 * @author lzh
 */
public class ActivityBean implements Comparable<ActivityBean> {

    private String name;
    private Class<? extends Activity> clazz;

    public ActivityBean(@NonNull Class<? extends Activity> clazz) {
        this.clazz = clazz;
        this.name = clazz.getSimpleName();
    }

    public Class<? extends Activity> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(ActivityBean another) {
        return getName().compareTo(another.getName());
    }

}
