package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface DoubleSupplier<R> extends ISupplier, java.util.function.DoubleSupplier {
    double get();

    @Override
    default double getAsDouble() {
        return get();
    }
}
