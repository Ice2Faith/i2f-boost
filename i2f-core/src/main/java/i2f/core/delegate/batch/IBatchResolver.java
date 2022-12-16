package i2f.core.delegate.batch;

import java.util.List;

/**
 * @author ltb
 * @date 2022/11/21 14:31
 * @desc
 */
@FunctionalInterface
public interface IBatchResolver<R, T> {
    R resolve(List<T> col) throws Exception;
}
