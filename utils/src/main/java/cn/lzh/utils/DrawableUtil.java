package cn.lzh.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.util.Random;

/**
 * @author from open source
 * @see #getColorDrawable(int, float) getColorDrawable
 * @see #getGradientColor(int, int, float) getGradientColor
 * @see #getGradientDrawable(int[], float) getGradientDrawable
 * @see #getPressedDrawable(BitmapDrawable) getPressedDrawable
 * @see #getRandomColor() getRandomColor
 * @see #getRandomColorDrawable(float) getRandomColorDrawable
 * @see #getRandomColorSelector(float) getRandomColorSelector
 * @see #getRandomGradientDrawable(float) getRandomGradientDrawable
 * @see #getSelector(BitmapDrawable) 增加点击变暗的效果
 * @see #getSelector(Drawable, Drawable) getSelector
 */
public final class DrawableUtil {

	private DrawableUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	/**
	 * 计算从 startColor 过度到 endColor 过程中百分比为 ratio 时的颜色值
	 * @param startColor 起始颜色 int类型
	 * @param endColor 结束颜色 int类型
	 * @param ratio 百分比0.5
	 * @return 返回int格式的color
	 */
	public static int getGradientColor(@ColorInt int startColor, @ColorInt int endColor, float ratio){
		String strStartColor = "#" + Integer.toHexString(startColor);
		String strEndColor = "#" + Integer.toHexString(endColor);
		return Color.parseColor(getGradientColor(strStartColor, strEndColor, ratio));
	}

	/**
	 * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
	 * @param startColor 起始颜色 （格式#FFFFFFFF）
	 * @param endColor 结束颜色 （格式#FFFFFFFF）
	 * @param ratio 百分比0.5
	 * @return 返回String格式的color（格式#FFFFFFFF）
	 */
	private static String getGradientColor(String startColor, String endColor, float ratio){

		int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
		int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
		int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
		int startBlue = Integer.parseInt(startColor.substring(7), 16);

		int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
		int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
		int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
		int endBlue = Integer.parseInt(endColor.substring(7), 16);

		int currentAlpha = (int) ((endAlpha - startAlpha) * ratio + startAlpha);
		int currentRed = (int) ((endRed - startRed) * ratio + startRed);
		int currentGreen = (int) ((endGreen - startGreen) * ratio + startGreen);
		int currentBlue = (int) ((endBlue - startBlue) * ratio + startBlue);

		return "#" + getHexString(currentAlpha) + getHexString(currentRed)
				+ getHexString(currentGreen) + getHexString(currentBlue);

	}

	/**
	 * 将10进制颜色值转换成16进制。
	 */
	private static String getHexString(int value) {
		String hexString = Integer.toHexString(value);
		if (hexString.length() == 1) {
			hexString = "0" + hexString;
		}
		return hexString;
	}

	/**
	 * 获取随机颜色
	 */
	@ColorInt
	public static int getRandomColor() {
		// 随机颜色(50-200)
		// 如果值太大，会偏白，太小则会偏黑，所以需要对颜色的值进行范围限定
		Random random = new Random();
		int red = random.nextInt(151) + 50;
		int green = random.nextInt(151) + 50;
		int blue = random.nextInt(151) + 50;
		return Color.rgb(red, green, blue);
	}

	/**
	 * 获取自定义圆角矩形Drawable对象
	 *
	 * @param argb 颜色
	 * @param radius 圆角
	 */
	public static Drawable getColorDrawable(@ColorInt int argb, float radius) {
		GradientDrawable gd = new GradientDrawable();
		gd.setShape(GradientDrawable.RECTANGLE);// 默认就是GradientDrawable.RECTANGLE
		gd.setCornerRadius(radius);
		gd.setColor(argb);
		return gd;
	}

	/**
	 * 获取随机颜色的,自定义圆角矩形Drawable对象
	 *
	 * @param radius 圆角
	 */
	public static Drawable getRandomColorDrawable(float radius) {
		return getColorDrawable(getRandomColor(), radius);
	}

	/**
	 * 获取一个按钮选择器,随机颜色
	 *
	 * @param radius 圆角
	 */
	public static StateListDrawable getRandomColorSelector(float radius) {
		StateListDrawable selector = new StateListDrawable();
		// 添加Pressed状态下的Drawable
		selector.addState(new int[] { android.R.attr.state_pressed },
				getRandomColorDrawable(radius));
		// 添加Normal状态下的Drawable(必须在Pressed后面设置)
		selector.addState(new int[] {}, getRandomColorDrawable(radius));
		return selector;
	}

	/**
	 * 生成渐变图片
	 * @param colors 渐变颜色的集合
	 * @param radius 圆角
	 */
	public static GradientDrawable getGradientDrawable(@ColorInt int colors[], float radius) {
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
		drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形
		drawable.setCornerRadius(radius);//设置圆角半径
		return drawable;
	}

	/**
	 * 生成随机颜色的渐变图片
	 */
	public static GradientDrawable getRandomGradientDrawable(float radius) {
		return getGradientDrawable(new int[]{getRandomColor(), getRandomColor()}, radius);
	}

	/**
	 * 动态生成Selector
	 */
	public static StateListDrawable getSelector(Drawable normal, Drawable pressed) {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//添加按下的图片
		drawable.addState(new int[]{}, normal);
		return drawable;
	}

	/**
	 * 生成Selector，增加点击变暗的效果
	 * @param drawable Drawable
	 */
	public static StateListDrawable getSelector(BitmapDrawable drawable) {
		Drawable pressed = getPressedDrawable(drawable);
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, drawable);
		return bg;
	}

	/**
	 * 获取给定图片的按下时变暗的效果
	 * @param drawable Drawable
	 */
	@NonNull
	public static Drawable getPressedDrawable(BitmapDrawable drawable) {
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		return createDrawable(drawable, paint);
	}

	private static Drawable createDrawable(BitmapDrawable bd, Paint p) {
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(b);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图
		return new BitmapDrawable(bitmap);
	}

}
