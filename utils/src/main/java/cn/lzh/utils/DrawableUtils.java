package cn.lzh.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import java.util.Random;

public class DrawableUtils {
	private DrawableUtils() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 获取随机颜色
	 * 
	 * @return
	 */
	public static int getRandomColor() {
		// 随机颜色(50-200)
		// 如果值太大，会偏白，太小则会偏黑，所以需要对颜色的值进行范围限定
		Random random = new Random();
		int red = random.nextInt(151) + 50;
		int green = random.nextInt(151) + 50;
		int blue = random.nextInt(151) + 50;
		int rgb = Color.rgb(red, green, blue);
		return rgb;
	}

	/**
	 * 获取自定义圆角矩形Drawable对象
	 * 
	 * @param argb
	 *            颜色
	 * @param radius
	 *            圆角
	 * @return
	 */
	public static Drawable getColorDrawable(int argb, float radius) {
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.RECTANGLE);// 默认就是GradientDrawable.RECTANGLE
		gd.setCornerRadius(radius);
		gd.setColor(argb);
		return gd;
	}

	/**
	 * 获取随机颜色的,自定义圆角矩形Drawable对象
	 * 
	 * @param radius
	 *            圆角
	 * @return
	 */
	public static Drawable getRandomColorDrawable(float radius) {
		return getColorDrawable(getRandomColor(), radius);
	}

	/**
	 * 获取一个按钮选择器,随机颜色
	 * 
	 * @param radius
	 *            圆角
	 * @return
	 */
	public static StateListDrawable getBtnSelectorRandomColor(float radius) {
		StateListDrawable selector = new StateListDrawable();
		// 添加Pressed状态下的Drawable
		selector.addState(new int[] { android.R.attr.state_pressed },
				getRandomColorDrawable(radius));
		// 添加Normal状态下的Drawable(必须在Pressed后面设置)
		selector.addState(new int[] {}, getRandomColorDrawable(radius));
		return selector;
	}

}
