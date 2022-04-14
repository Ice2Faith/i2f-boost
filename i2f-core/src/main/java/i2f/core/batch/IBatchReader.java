package i2f.core.batch;

import i2f.core.annotations.remark.Remark;
import i2f.core.cycle.ILifeCycle;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/4/14 11:39
 * @desc
 */
public interface IBatchReader<T> extends ILifeCycle {
    @Remark("return null means ending read")
    Collection<T> read();
}
