package i2f.springboot.limit.core;

import org.aspectj.lang.JoinPoint;

/**
 * @author ltb
 * @date 2022/7/2 22:20
 * @desc
 */
public interface IUserIdProvider {
    String getUserId(JoinPoint jp);
}
