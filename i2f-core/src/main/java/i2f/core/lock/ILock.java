package i2f.core.lock;

/**
 * @author ltb
 * @date 2022/4/15 8:36
 * @desc
 */
public interface ILock {
    void lock() throws Throwable;
    void unlock() throws Throwable;
}
