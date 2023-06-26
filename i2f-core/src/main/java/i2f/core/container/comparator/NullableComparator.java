package i2f.core.container.comparator;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2023/6/26 9:45
 * @desc
 */
public class NullableComparator<E> implements Comparator<E> {
    protected Comparator<E> comparator;

    @Override
    public int compare(E v1, E v2) {
        return ComparatorUtil.compare(v1, v2, comparator);
    }

}
