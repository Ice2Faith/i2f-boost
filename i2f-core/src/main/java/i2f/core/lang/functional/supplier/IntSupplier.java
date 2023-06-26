package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface IntSupplier<R> extends ISupplier, java.util.function.IntSupplier {
    int get();

    @Override
    default int getAsInt() {
        return get();
    }
}
