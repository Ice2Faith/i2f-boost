package i2f.core.lang.functional.jvf;

import i2f.core.lang.functional.supplier.ISupplier2;

/**
 * @author ltb
 * @date 2022/11/16 11:00
 * @desc
 */
@FunctionalInterface
public interface BiFunction<R, V1, V2> extends ISupplier2<R, V1, V2> {
    default R apply(V1 v1, V2 v2) {
        return get(v1, v2);
    }
}
