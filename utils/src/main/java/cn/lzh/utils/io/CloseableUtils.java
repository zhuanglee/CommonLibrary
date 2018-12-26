package cn.lzh.utils.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * CloseableUtils
 *
 * @author from open source
 */
public final class CloseableUtils {

    private CloseableUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }


    /**
     * Close closable object and wrap {@link IOException} with {@link RuntimeException}
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close closable and hide possible {@link IOException}
     *
     * @param closeable closeable object
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

}