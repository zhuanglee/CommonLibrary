package cn.lzh.utils.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static cn.lzh.utils.io.StreamUtil.getStrings;
import static cn.lzh.utils.io.StreamUtil.readString;

/**
 * Created by lzh on 2018/12/26.<br/>
 *
 * @see #geFileFromAssets(Context, String)
 * @see #getFileToListFromAssets(Context, String)
 */
public final class AssetUtil {

    private AssetUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    public static String geFileFromAssets(@NonNull Context ctx, @NonNull String name) throws IOException {
        return readString(ctx.getAssets().open(name));
    }

    /**
     * same to {@link #geFileFromAssets(Context, String)}, but return type is List<String>
     *
     * @param context  Context
     * @param fileName 文件名
     */
    @Nullable
    public static List<String> getFileToListFromAssets(@NonNull Context context, String fileName) {
        try {
            return getStrings(context.getResources().getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
        InputStream is = null;
        try {
            is = ct.getAssets().open(fileName);
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
