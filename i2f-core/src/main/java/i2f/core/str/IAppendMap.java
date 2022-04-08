package i2f.core.str;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2021/9/28
 */
@Author("i2f")
public interface IAppendMap<T> {
    void append(T val, Appendable appender,Object ... params);
}
