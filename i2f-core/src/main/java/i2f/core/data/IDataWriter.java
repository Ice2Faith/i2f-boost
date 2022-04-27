package i2f.core.data;

import i2f.core.cycle.ILifeCycle;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/27 10:08
 * @desc
 */
public interface IDataWriter<T> extends ILifeCycle {
    void write(T obj) throws IOException;
}
