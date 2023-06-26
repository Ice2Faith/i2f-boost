package i2f.core.lang.functional.thr.supplier;

import i2f.core.lang.functional.thr.IThrSupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ThrLongSupplier extends IThrSupplier {
    long get() throws Throwable;
}
