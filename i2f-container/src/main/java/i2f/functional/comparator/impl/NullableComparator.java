package i2f.functional.comparator.impl;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 13:53
 * @desc
 */
public class NullableComparator<T> implements Comparator<T> {
    private Comparator<T> comparator;
    private boolean reverseNull = false;
    private boolean reverseResult = false;

    public NullableComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public NullableComparator(Comparator<T> comparator, boolean reverseNull) {
        this.comparator = comparator;
        this.reverseNull = reverseNull;
    }

    public NullableComparator(Comparator<T> comparator, boolean reverseNull, boolean reverseResult) {
        this.comparator = comparator;
        this.reverseNull = reverseNull;
        this.reverseResult = reverseResult;
    }

    @Override
    public int compare(T o1, T o2) {
        return compareNullable(o1, o2, comparator, reverseNull, reverseResult);
    }

    public static final int COMPARE_UN_NULL_RETURN_VALUE = 9;

    public static <T> int compareNullable(T o1, T o2, Comparator<T> comparator) {
        return compareNullable(o1, o2, comparator, false, false);
    }

    public static <T> int compareNullable(T o1, T o2, Comparator<T> comparator, boolean reverseNull) {
        return compareNullable(o1, o2, comparator, reverseNull, false);
    }

    public static <T> int compareNullable(T o1, T o2, Comparator<T> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = compareNull(o1, o2, reverseNull);
        if (ret != COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        ret = comparator.compare(o1, o2);
        return reverseResult ? -ret : ret;
    }

    public static <T> int compareNull(T o1, T o2, boolean reverseNull) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return reverseNull ? 1 : -1;
        }
        if (o2 == null) {
            return reverseNull ? -1 : 1;
        }
        return COMPARE_UN_NULL_RETURN_VALUE;
    }
}
