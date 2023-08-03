package i2f.springboot.limit.core;

import i2f.springboot.limit.consts.LimitType;
import i2f.springboot.limit.data.LimitDto;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/8/3 10:58
 * @desc
 */
public interface ILimitListener {
    void accept(LimitType type, Method method, LimitDto dto) throws Throwable;
}
