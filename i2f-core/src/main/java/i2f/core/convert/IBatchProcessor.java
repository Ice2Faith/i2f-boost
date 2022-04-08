package i2f.core.convert;

import java.util.Collection;

/**
 * @author ltb
 * @date 2022/3/27 18:46
 * @desc
 */
public interface IBatchProcessor<T> {
    void process(Collection<T> col);
}
