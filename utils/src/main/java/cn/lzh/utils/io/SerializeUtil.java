package cn.lzh.utils.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialize Utils
 * @author open source
 */
public final class SerializeUtil {

    private SerializeUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * Deserialization object from file.
     * 
     * @param filePath file path
     * @return de-serialized object
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(in);
        }
        return null;
    }

    /**
     * Serialize object to file.
     * 
     * @param filePath file path
     * @param obj object
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(out);
        }
    }

}
