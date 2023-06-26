package i2f.core.container.comparator;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2023/5/1 21:20
 * @desc
 */
public class DefaultComparator<E> implements Comparator<E> {
    @Override
    public int compare(E o1, E o2) {
        return ComparatorUtil.compare(o1, o2);
    }
}
