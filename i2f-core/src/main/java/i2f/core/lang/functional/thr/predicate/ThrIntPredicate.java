package i2f.core.lang.functional.thr.predicate;

import i2f.core.lang.functional.thr.IThrPredicate;


/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface ThrIntPredicate extends IThrPredicate {
    boolean test(int val) throws Throwable;
}
