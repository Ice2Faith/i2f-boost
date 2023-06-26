package i2f.core.container.comparator;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2023/6/26 9:47
 * @desc
 */
public class ComparatorUtil {

    public static <T> int compare(T v1, T v2, Comparator<T> comparator) {
        if (v1 == v2) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        return comparator.compare(v1, v2);
    }

    public static <T extends Comparable<T>> int compare(T v1, T v2) {
        if (v1 == v2) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        return v1.compareTo(v2);
    }

    public static <T> int compare(T v1, T v2) {
        if (v1 == v2) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        if (v1 instanceof Comparable) {
            return ((Comparable) v1).compareTo(v2);
        }
        if (v2 instanceof Comparable) {
            return ((Comparable) v2).compareTo(v1);
        }
        return Integer.compare(v1.hashCode(), v2.hashCode());
    }
}
