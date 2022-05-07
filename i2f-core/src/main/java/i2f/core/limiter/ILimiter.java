package i2f.core.limiter;

/**
 * @author ltb
 * @date 2022/5/6 17:44
 * @desc 限流器
 */
public interface ILimiter {
    /**
     * 是否已被限流
     * @param key 限流的关键KEY
     * @param args 限流的其他参数
     * @return
     */
    boolean hasLimit(String key,Object ... args);

    /**
     * 进行限流
     * @param key
     * @param args
     */
    void limit(String key,Object ... args);

    /**
     * 取消限流
     * @param key
     * @param args
     */
    void unlimited(String key,Object ... args);
}
