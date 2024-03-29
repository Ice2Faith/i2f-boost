package i2f.functional.predicate.except.impl;

import i2f.functional.predicate.except.IExPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IExPredicate2<V1, V2> extends IExPredicate {
    boolean test(V1 v1, V2 v2) throws Throwable;
}
