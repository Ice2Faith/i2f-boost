package i2f.core.lambda.functional.preset;

import i2f.core.lambda.functional.provider.IProvider1;

/**
 * @author ltb
 * @date 2022/11/9 12:16
 * @desc
 */
@FunctionalInterface
public interface IFilter<V> extends IProvider1<Boolean, V> {
}
