package i2f.core.interfaces;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2021/9/28
 */
@Author("i2f")
public interface IExecute<T> {
    void exec(T val);
}
