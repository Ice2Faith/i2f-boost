package i2f.core.lang.functional.thr.supplier;

import i2f.core.lang.functional.thr.IThrSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ThrDoubleSupplier extends IThrSupplier {
    double get() throws Throwable;
}