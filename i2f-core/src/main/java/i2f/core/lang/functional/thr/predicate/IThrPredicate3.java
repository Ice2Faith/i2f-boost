package i2f.core.lang.functional.thr.predicate;

import i2f.core.lang.functional.thr.IThrPredicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface IThrPredicate3<V1, V2, V3> extends IThrPredicate {
    boolean test(V1 v1, V2 v2, V3 v3) throws Throwable;
}