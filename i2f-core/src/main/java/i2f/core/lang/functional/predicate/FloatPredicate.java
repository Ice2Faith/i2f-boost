package i2f.core.lang.functional.predicate;

import i2f.core.lang.functional.IPredicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface FloatPredicate extends IPredicate {
    boolean test(float val);
}
