package i2f.core.filter.bigdata.hash;

import i2f.core.cycle.ILifeCycle;
import i2f.core.data.IDataReader;
import i2f.core.data.IDataWriter;

/**
 * @author ltb
 * @date 2022/4/27 10:11
 * @desc
 */
public interface IHashGroupProvider<T> extends ILifeCycle {
    IDataWriter<T> getWriter(String hash);
    IDataReader<T> getReader(String hash);
}
