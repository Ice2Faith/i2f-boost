package i2f.core.functional.supplier;

import i2f.core.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ISupplier4<R, V1, V2, V3, V4> extends ISupplier {
    R get(V1 v1, V2 v2, V3 v3, V4 v4);
}
