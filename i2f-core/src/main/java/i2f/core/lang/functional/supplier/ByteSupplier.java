package i2f.core.lang.functional.supplier;

import i2f.core.lang.functional.ISupplier;

/**
 * @author ltb
 * @date 2022/11/16 10:22
 * @desc
 */
@FunctionalInterface
public interface ByteSupplier extends ISupplier {
    byte get();

    default byte getAsByte() {
        return get();
    }
}
