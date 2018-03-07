package cn.lzh.utils.io;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

public final class AssetUtil {

	private AssetUtil() {
		throw new UnsupportedOperationException("Cannot be instantiated");
	}

	public static String getAssetString(@NonNull Context ctx, @NonNull String name) throws IOException {
		return StreamUtil.readString(ctx.getAssets().open(name));
	}

	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

}
