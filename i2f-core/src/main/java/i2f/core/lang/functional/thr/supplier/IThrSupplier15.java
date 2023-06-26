package i2f.core.lang.functional.thr.supplier;

import i2f.core.lang.functional.thr.IThrSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface IThrSupplier15<R, V1, V2, V3, V4, V5, V6, V7, V8, V9, V10, V11, V12, V13, V14, V15> extends IThrSupplier {
    R get(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8, V9 v9, V10 v10, V11 v11, V12 v12, V13 v13, V14 v14, V15 v15) throws Throwable;
}
