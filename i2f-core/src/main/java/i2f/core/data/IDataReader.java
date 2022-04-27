package i2f.core.data;

import i2f.core.cycle.ILifeCycle;

import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/27 10:07
 * @desc
 */
public interface IDataReader<T> extends ILifeCycle {
    T read() throws IOException;
    boolean hasMore() throws IOException;
}
