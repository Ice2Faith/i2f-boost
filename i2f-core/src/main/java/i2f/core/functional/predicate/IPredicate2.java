package i2f.core.functional.predicate;

import i2f.core.functional.IPredicate;

import java.util.function.BiPredicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface IPredicate2<V1, V2> extends IPredicate, BiPredicate<V1, V2> {
    boolean test(V1 v1, V2 v2);
}
