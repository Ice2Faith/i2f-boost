package i2f.core.lang.functional.thr.supplier;

import i2f.core.lang.functional.thr.IThrSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface IThrSupplier2<R, V1, V2> extends IThrSupplier {
    R get(V1 v1, V2 v2) throws Throwable;

}
