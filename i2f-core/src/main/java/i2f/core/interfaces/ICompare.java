package i2f.core.interfaces;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/17 11:05
 * @desc
 */
@Author("i2f")
public interface ICompare<T> {
    int compare(T t1,T t2);
}
