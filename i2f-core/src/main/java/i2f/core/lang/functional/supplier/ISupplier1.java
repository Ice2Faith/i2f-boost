package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

import java.util.function.Function;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ISupplier1<R, V1> extends ISupplier, Function<V1, R> {
    R get(V1 v1);

    @Override
    default R apply(V1 v1) {
        return get(v1);
    }
}