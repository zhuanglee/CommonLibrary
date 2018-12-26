package cn.lzh.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * 修复内存泄露的工具类
 * @author from open source
 */
public final class FixMemoryLeakHelper {

    private FixMemoryLeakHelper() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 修复华为手机内存的泄露
     */
    public static void fixHuaWeiMemoryLeak(Context context) {
        //测试
        try {
            Class<?> clazz = Class.forName("android.gestureboost.GestureBoostManager");
            Field field = clazz.getDeclaredField("sGestureBoostManager");
            field.setAccessible(true);
            Object obj = field.get(clazz);
            Field contextField = clazz.getDeclaredField("mContext");
            contextField.setAccessible(true);
            if (contextField.get(obj) == context) {
                contextField.set(obj, null);
            }
        } catch (Throwable t) {
//            t.printStackTrace();
        }
    }

    /**
     * 修复InputMethodManager导致Activity的内存泄露<br/>
     * 问题链接：http://blog.csdn.net/sodino/article/details/32188809
     *
     * @param destContext Context
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView", "mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj = f.get(imm);
                if (obj instanceof View) {
                    View v = (View) obj;
                    if (v.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
//                t.printStackTrace();
            }
        }
    }

}
