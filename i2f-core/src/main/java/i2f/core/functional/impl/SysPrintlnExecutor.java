package i2f.core.functional.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IExecutor;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Author("i2f")
public class SysPrintlnExecutor<T extends Object> implements IExecutor<T> {
    @Override
    public void accept(T val) {
        System.out.println(val);
    }
}
