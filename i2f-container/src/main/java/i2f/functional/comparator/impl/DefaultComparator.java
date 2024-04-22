package i2f.functional.comparator.impl;


import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 14:03
 * @desc
 */
public class DefaultComparator<T> implements Comparator<T> {
    private boolean reverseNull = false;
    private boolean reverseResult = false;

    public int compare(T o1, T o2) {
        return compareDefault(o1, o2, reverseNull, reverseResult);
    }

    public static <T> int compareDefault(T o1, T o2) {
        return compareDefault(o1, o2, false, false);
    }

    public static <T> int compareDefault(T o1, T o2, boolean reverseNull) {
        return compareDefault(o1, o2, reverseNull, false);
    }

    public static <T> int compareDefault(T o1, T o2, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        if (o1 instanceof Comparable) {
            ret = ((Comparable) o1).compareTo(o2);
            return reverseResult ? -ret : ret;
        }
        ret = Integer.compare(o1.hashCode(), o2.hashCode());
        return reverseResult ? -ret : ret;
    }
}
