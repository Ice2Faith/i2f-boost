package i2f.core.lang.functional.predicate;

import i2f.core.lang.functional.IPredicate;

import java.util.function.BiPredicate;

/**
 * @author ltb
 * @date 2022/11/16 10:34
 * @desc
 */
@FunctionalInterface
public interface IPredicate2<V1, V2> extends IPredicate, BiPredicate<V1, V2> {

}
