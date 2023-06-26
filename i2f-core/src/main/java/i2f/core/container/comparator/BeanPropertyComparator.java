package i2f.core.container.comparator;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2023/5/1 14:52
 * @desc
 */
public class BeanPropertyComparator<T, R> implements Comparator<T> {
    private Function<T, R> getter;
    protected Comparator<R> embedComparator = new Comparator<R>() {
        @Override
        public int compare(R r1, R r2) {
            if (r1 instanceof Comparable) {
                return ((Comparable) r1).compareTo(r2);
            }
            if (r2 instanceof Comparable) {
                return ((Comparable) r2).compareTo(r1);
            }
            return Integer.compare(r1.hashCode(), r2.hashCode());
        }
    };
    protected Comparator<T> innerComparator = new Comparator<T>() {
        @Override
        public int compare(T o1, T o2) {
            R r1 = getter.apply(o1);
            R r2 = getter.apply(o2);
            return ComparatorUtil.compare(r1, r2, embedComparator);
        }
    };

    public BeanPropertyComparator(Function<T, R> getter) {
        this.getter = getter;
    }

    @Override
    public int compare(T o1, T o2) {
        return ComparatorUtil.compare(o1, o2, innerComparator);

    }
}
