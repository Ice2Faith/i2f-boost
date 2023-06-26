package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

import java.util.function.BooleanSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface BoolSupplier extends ISupplier, BooleanSupplier {
    boolean get();

    @Override
    default boolean getAsBoolean() {
        return get();
    }
}
