package i2f.core.functional.common;

import i2f.core.functional.jvf.BiConsumer;

/**
 * @author ltb
 * @date 2022/11/16 10:56
 * @desc
 */
@FunctionalInterface
public interface ISetter<V1, V2> extends BiConsumer<V1, V2> {
}