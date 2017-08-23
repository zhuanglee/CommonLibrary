package cn.lzh.utils;

import android.util.Base64;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lzh on 2017/2/9 17:44.
 */

public class Base64Util {

	public static String GetBase64StrFromImage(String imagePath)
	{
		String imgFile = imagePath;
		InputStream in = null;
		byte[] data = (byte[])null;
		try
		{
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(data,Base64.DEFAULT);
	}

	public static boolean GenerateImageFromBase64Str(String imgBase64Str, String objImgStr)
	{
		if (imgBase64Str == null)
			return false;
		try
		{
			byte[] b = Base64.decode(imgBase64Str,Base64.DEFAULT);
			for (int i = 0; i < b.length; ++i)
				if (b[i] < 0) {
					int tmp36_34 = i;
					byte[] tmp36_33 = b; tmp36_33[tmp36_34] = (byte)(tmp36_33[tmp36_34] + 256);
				}


			String imgFilePath = objImgStr;
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true; } catch (Exception e) {
		}
		return false;
	}

}
