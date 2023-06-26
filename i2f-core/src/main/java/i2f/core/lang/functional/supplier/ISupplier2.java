package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

import java.util.function.BiFunction;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ISupplier2<R, V1, V2> extends ISupplier, BiFunction<V1, V2, R> {
    R get(V1 v1, V2 v2);

    @Override
    default R apply(V1 v1, V2 v2) {
        return get(v1, v2);
    }
}
