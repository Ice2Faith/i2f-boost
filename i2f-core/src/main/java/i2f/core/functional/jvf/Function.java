package i2f.core.functional.jvf;

import i2f.core.functional.supplier.ISupplier1;

/**
 * @author ltb
 * @date 2022/11/16 11:00
 * @desc
 */
@FunctionalInterface
public interface Function<R, V1> extends ISupplier1<R, V1> {
    default R apply(V1 v1) {
        return get(v1);
    }
}
