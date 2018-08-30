package cn.lzh.utils;

import android.graphics.Color;

/**
 * 颜色渐变取值工具
 * Created by pyt on 2017/4/19.
 */
public class ColorGradientUtil {

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 int类型
     * @param endColor 结束颜色 int类型
     * @param ratio 百分比0.5
     * @return 返回int格式的color
     */
    public static int getColor(int startColor, int endColor, float ratio){
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(getColor(strStartColor, strEndColor, ratio));
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor 结束颜色 （格式#FFFFFFFF）
     * @param ratio 百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static String getColor(String startColor, String endColor, float ratio){

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
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }



}
