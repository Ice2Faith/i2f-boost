package i2f.core.batch;

import i2f.core.annotations.remark.Remark;
import i2f.core.cycle.ILifeCycle;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/27 18:46
 * @desc
 */
public interface IBatchWriter<T> extends ILifeCycle {
    @Remark("argument is null means ending")
    void write(Collection<T> col);
}
