package i2f.core.interfaces.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.interfaces.IExecute;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Author("i2f")
public class SysPrintlnExecutor<T extends Object> implements IExecute<T> {
    @Override
    public void exec(T val) {
        System.out.println(val);
    }
}
