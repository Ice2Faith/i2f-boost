package i2f.core.streaming.comparator;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/1 14:52
 * @desc
 */
public class BeanPropertyComparator<T, R extends Comparable<R>> implements Comparator<T> {
    private Function<T, R> getter;

    public BeanPropertyComparator(Function<T, R> getter) {
        this.getter = getter;
    }

    @Override
    public int compare(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        R r1 = getter.apply(o1);
        R r2 = getter.apply(o2);
        if (r1 == r2) {
            return 0;
        }
        if (r1 == null) {
            return -1;
        }
        if (r2 == null) {
            return 1;
        }
        return r1.compareTo(r2);
    }
}
