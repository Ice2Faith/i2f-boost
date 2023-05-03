package i2f.core.streaming.comparator;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2023/5/1 21:20
 * @desc
 */
public class DefaultComparator<E> implements Comparator<E> {
    @Override
    public int compare(E o1, E o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1 instanceof Comparable) {
            Comparable c1 = (Comparable) o1;
            return c1.compareTo(o2);
        }
        return o2.hashCode() - o1.hashCode();
    }
}
