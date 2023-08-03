package i2f.std.invoke;

import java.lang.reflect.Method;

/**
 * @author Ice2Faith
 * @date 2023/8/3 14:57
 * @desc
 */
public interface IMethodInvokable extends IInvokable {
    Method getMethod();
}
