package i2f.core.lang.functional.predicate;

import i2f.core.lang.functional.IPredicate;

import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface IPredicate1<V1> extends IPredicate, Predicate<V1> {
    boolean test(V1 v1);
}
