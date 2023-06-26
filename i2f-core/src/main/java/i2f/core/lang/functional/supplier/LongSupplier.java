package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface LongSupplier<R> extends ISupplier, java.util.function.LongSupplier {
    long get();

    @Override
    default long getAsLong() {
        return get();
    }
}
