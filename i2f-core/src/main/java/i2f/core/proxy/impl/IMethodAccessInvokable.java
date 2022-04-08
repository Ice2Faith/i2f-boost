package i2f.core.proxy.impl;

import i2f.core.proxy.IInvokable;

import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/3/28 9:14
 * @desc
 */
public interface IMethodAccessInvokable extends IInvokable {
    Method method();
}
