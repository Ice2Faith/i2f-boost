package i2f.core.functional.supplier;

import i2f.core.functional.ISupplier;

import java.util.function.Supplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ISupplier0<R> extends ISupplier, Supplier<R> {
    R get();
}
