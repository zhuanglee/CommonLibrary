package cn.lzh.utils;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工具类
 */
public class AnimationUtil {

    private AnimationUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 晃动View
     *
     * @param view View
     */
    public static void shakeView(View view) {
        if (view != null && view.getContext() != null) {
            TranslateAnimation shake = new TranslateAnimation(0, -10, 0, 10);
            shake.setDuration(1000);
            shake.setInterpolator(new CycleInterpolator(5));
            view.startAnimation(shake);
        }
    }

    /**
     * 获取布局缩放动画控制器
     */
    public static LayoutAnimationController getScaleAnimationController() {
        LayoutAnimationController controller;
        // AnimationSet set = new AnimationSet(true);
        Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF,
                1.0f);// 从0.1倍放大到1倍
        anim.setDuration(300);
        controller = new LayoutAnimationController(anim, 0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }

    /**
     * 获取布局过度动画（缩放进入和缩放退出）<br/>
     * 添加到布局中的View，要先设置setScaleX&setScaleY=0,否则动画会有“先完全显示，再执行缩放动画”bug
     *
     * @param duration 过渡动画时长
     */
    public static LayoutTransition getScaleLayoutTransition(long duration) {
        return getScaleLayoutTransition(duration, 0);
    }

    /**
     * 获取布局过度动画（缩放进入和缩放退出）<br/>
     *
     * @param duration 过渡动画时长
     * @param minScale 添加到布局中的View，要先设置setScaleX&setScaleY=minScale,否则动画会有“先完全显示，再执行缩放动画”bug
     */
    public static LayoutTransition getScaleLayoutTransition(long duration, float minScale) {
        if (minScale > 1) {
            minScale = 1;
        }
        if (minScale < 0) {
            minScale = 0;
        }
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(duration);

        //设置单个item间的动画间隔
//        transition.setStagger(LayoutTransition.CHANGE_APPEARING, TRANSITION_DURATION);
        //入场动画:view显示时的动画
        ObjectAnimator inAnimScaleX = ObjectAnimator.ofFloat(null, View.SCALE_X, minScale, 1);
        ObjectAnimator inAnimScaleY = ObjectAnimator.ofFloat(null, View.SCALE_Y, minScale, 1);
        ObjectAnimator inAnimAlpha = ObjectAnimator.ofFloat(null, View.ALPHA, minScale, 1);
        AnimatorSet inAnimatorSet = new AnimatorSet();
        inAnimatorSet.play(inAnimScaleX).with(inAnimScaleY).with(inAnimAlpha);
        transition.setAnimator(LayoutTransition.APPEARING, inAnimatorSet);

        //出场动画:view在这个容器中消失时触发的动画
        ObjectAnimator outAnimScaleX = ObjectAnimator.ofFloat(null, View.SCALE_X, 1, minScale);
        ObjectAnimator outAnimScaleY = ObjectAnimator.ofFloat(null, View.SCALE_Y, 1, minScale);
        AnimatorSet outAnimatorSet = new AnimatorSet();
        outAnimatorSet.play(outAnimScaleX).with(outAnimScaleY);
        transition.setAnimator(LayoutTransition.DISAPPEARING, outAnimatorSet);

        return transition;
    }
}
