package i2f.core.functional.predicate;

import i2f.core.functional.IPredicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface IPredicate3<V1, V2, V3> extends IPredicate {
    boolean test(V1 v1, V2 v2, V3 v3);
}
