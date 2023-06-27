package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ISupplier8<R, V1, V2, V3, V4, V5, V6, V7, V8> extends ISupplier {
    R get(V1 v1, V2 v2, V3 v3, V4 v4, V5 v5, V6 v6, V7 v7, V8 v8);
}