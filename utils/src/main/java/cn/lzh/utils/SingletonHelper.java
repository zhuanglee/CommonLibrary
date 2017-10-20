package cn.lzh.utils;

/**
 * Singleton helper class for lazily initialization.
 * 
 * @author <a href="http://www.trinea.cn/" target="_blank">Trinea</a>
 * 
 * @param <T>
 */
public abstract class SingletonHelper<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonHelper.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }

}
