package i2f.functional.predicate.except.impl;

import i2f.functional.predicate.except.IExPredicate;

/**
 * @author Ice2Faith
 * @date 2024/3/29 11:36
 * @desc
 */
@FunctionalInterface
public interface IExPredicate4<V1, V2, V3, V4> extends IExPredicate {
    boolean test(V1 v1, V2 v2, V3 v3, V4 v4) throws Throwable;
}
