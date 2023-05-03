package i2f.core.streaming.comparator;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2023/5/1 21:17
 * @desc
 */
public class AntiComparator<E> implements Comparator<E> {
    private Comparator<E> comparator;

    public AntiComparator(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(E o1, E o2) {
        int val = comparator.compare(o1, o2);
        return 0 - val;
    }
}
