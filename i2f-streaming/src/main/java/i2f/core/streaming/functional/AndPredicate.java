package i2f.core.streaming.functional;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2023/5/4 10:25
 * @desc
 */
public class AndPredicate<E> implements Predicate<E> {
    private Collection<Predicate<E>> predicates;

    public AndPredicate(Predicate<E>... predicates) {
        this.predicates = Arrays.asList(predicates);
    }

    public AndPredicate(Collection<Predicate<E>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean test(E e) {
        for (Predicate<E> predicate : predicates) {
            boolean ok = predicate.test(e);
            if (!ok) {
                return false;
            }
        }
        return true;
    }
}
