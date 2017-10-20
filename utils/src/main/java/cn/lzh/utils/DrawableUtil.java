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

import java.util.Random;

public final class DrawableUtil {

	private DrawableUtil() {
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

	/**
	 * 生成圆角图片
	 */
	public static GradientDrawable generateDrawable(int colors[]) {
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
		drawable.setShape(GradientDrawable.RECTANGLE);//设置矩形
		drawable.setCornerRadius(15);//设置圆角半径
		return drawable;
	}

	/**
	 * 动态生成Selector
	 */
	public static StateListDrawable generateSelector(Drawable normal, Drawable pressed) {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//添加按下的图片
		drawable.addState(new int[]{}, normal);
		return drawable;
	}

	/** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果 */
	public static StateListDrawable createSelector(Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

		return new BitmapDrawable(bitmap);
	}

}
