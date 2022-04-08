package i2f.core.thread;

/**
 * @author ltb
 * @date 2022/3/26 12:57
 * @desc
 */
public interface ITaskProxy {
    <T> T task(Object ... args) throws Throwable;
}
