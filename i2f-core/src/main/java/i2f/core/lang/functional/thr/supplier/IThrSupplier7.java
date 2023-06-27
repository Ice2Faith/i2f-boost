package i2f.core.lang.functional.thr.supplier;

import i2f.core.lang.functional.thr.IThrSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface IThrSupplier7<R, V1, V2, V3, V4, V5, V6, V7> extends IThrSupplier {
    R get(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7) throws Throwable;
}