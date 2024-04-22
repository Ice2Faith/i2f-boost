package i2f.functional.comparator.impl;

import java.util.Comparator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 14:30
 * @desc
 */
public class ArrayComparator<T> implements Comparator<T[]> {
    private Comparator<T> comparator;
    private boolean reverseNull = false;
    private boolean reverseResult = false;

    public ArrayComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public ArrayComparator(Comparator<T> comparator, boolean reverseNull) {
        this.comparator = comparator;
        this.reverseNull = reverseNull;
    }

    public ArrayComparator(Comparator<T> comparator, boolean reverseNull, boolean reverseResult) {
        this.comparator = comparator;
        this.reverseNull = reverseNull;
        this.reverseResult = reverseResult;
    }

    @Override
    public int compare(T[] o1, T[] o2) {
        return compareArray(o1, o2, comparator, reverseNull, reverseResult);
    }

    public static <T> int compareArray(T[] o1, T[] o2) {
        return compareArray(o1, o2, DefaultComparator::compareDefault, false, false);
    }

    public static <T> int compareArray(T[] o1, T[] o2, Comparator<T> comparator) {
        return compareArray(o1, o2, comparator, false, false);
    }

    public static <T> int compareArray(T[] o1, T[] o2, Comparator<T> comparator, boolean reverseNull) {
        return compareArray(o1, o2, comparator, reverseNull, false);
    }

    public static <T> int compareArray(T[] o1, T[] o2, Comparator<T> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareBooleanArray(boolean[] o1, boolean[] o2) {
        return compareBooleanArray(o1, o2, Boolean::compareTo, false, false);
    }

    public static int compareBooleanArray(boolean[] o1, boolean[] o2, Comparator<Boolean> comparator) {
        return compareBooleanArray(o1, o2, comparator, false, false);
    }

    public static int compareBooleanArray(boolean[] o1, boolean[] o2, Comparator<Boolean> comparator, boolean reverseNull) {
        return compareBooleanArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareBooleanArray(boolean[] o1, boolean[] o2, Comparator<Boolean> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareByteArray(byte[] o1, byte[] o2) {
        return compareByteArray(o1, o2, Byte::compareTo, false, false);
    }

    public static int compareByteArray(byte[] o1, byte[] o2, Comparator<Byte> comparator) {
        return compareByteArray(o1, o2, comparator, false, false);
    }

    public static int compareByteArray(byte[] o1, byte[] o2, Comparator<Byte> comparator, boolean reverseNull) {
        return compareByteArray(o1, o2, comparator, reverseNull, false);
    }


    public static int compareByteArray(byte[] o1, byte[] o2, Comparator<Byte> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareCharArray(char[] o1, char[] o2) {
        return compareCharArray(o1, o2, Character::compareTo, false, false);
    }

    public static int compareCharArray(char[] o1, char[] o2, Comparator<Character> comparator) {
        return compareCharArray(o1, o2, comparator, false, false);
    }

    public static int compareCharArray(char[] o1, char[] o2, Comparator<Character> comparator, boolean reverseNull) {
        return compareCharArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareCharArray(char[] o1, char[] o2, Comparator<Character> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareDoubleArray(double[] o1, double[] o2) {
        return compareDoubleArray(o1, o2, Double::compareTo, false, false);
    }

    public static int compareDoubleArray(double[] o1, double[] o2, Comparator<Double> comparator) {
        return compareDoubleArray(o1, o2, comparator, false, false);
    }

    public static int compareDoubleArray(double[] o1, double[] o2, Comparator<Double> comparator, boolean reverseNull) {
        return compareDoubleArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareDoubleArray(double[] o1, double[] o2, Comparator<Double> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareFloatArray(float[] o1, float[] o2) {
        return compareFloatArray(o1, o2, Float::compareTo, false, false);
    }

    public static int compareFloatArray(float[] o1, float[] o2, Comparator<Float> comparator) {
        return compareFloatArray(o1, o2, comparator, false, false);
    }

    public static int compareFloatArray(float[] o1, float[] o2, Comparator<Float> comparator, boolean reverseNull) {
        return compareFloatArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareFloatArray(float[] o1, float[] o2, Comparator<Float> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareIntArray(int[] o1, int[] o2) {
        return compareIntArray(o1, o2, Integer::compareTo, false, false);
    }

    public static int compareIntArray(int[] o1, int[] o2, Comparator<Integer> comparator) {
        return compareIntArray(o1, o2, comparator, false, false);
    }

    public static int compareIntArray(int[] o1, int[] o2, Comparator<Integer> comparator, boolean reverseNull) {
        return compareIntArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareIntArray(int[] o1, int[] o2, Comparator<Integer> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }

    public static int compareLongArray(long[] o1, long[] o2) {
        return compareLongArray(o1, o2, Long::compareTo, false, false);
    }

    public static int compareLongArray(long[] o1, long[] o2, Comparator<Long> comparator) {
        return compareLongArray(o1, o2, comparator, false, false);
    }

    public static int compareLongArray(long[] o1, long[] o2, Comparator<Long> comparator, boolean reverseNull) {
        return compareLongArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareLongArray(long[] o1, long[] o2, Comparator<Long> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }


    public static int compareShortArray(short[] o1, short[] o2) {
        return compareShortArray(o1, o2, Short::compareTo, false, false);
    }

    public static int compareShortArray(short[] o1, short[] o2, Comparator<Short> comparator) {
        return compareShortArray(o1, o2, comparator, false, false);
    }

    public static int compareShortArray(short[] o1, short[] o2, Comparator<Short> comparator, boolean reverseNull) {
        return compareShortArray(o1, o2, comparator, reverseNull, false);
    }

    public static int compareShortArray(short[] o1, short[] o2, Comparator<Short> comparator, boolean reverseNull, boolean reverseResult) {
        int ret = NullableComparator.compareNull(o1, o2, reverseNull);
        if (ret != NullableComparator.COMPARE_UN_NULL_RETURN_VALUE) {
            return ret;
        }
        int i = 0;
        while (true) {
            if (i < o1.length && i < o2.length) {
                ret = NullableComparator.compareNullable(o1[i], o2[i], comparator, reverseNull, reverseResult);
                if (ret != 0) {
                    return ret;
                }
            } else {
                break;
            }
            i++;
        }
        if (i < o1.length) {
            return reverseResult ? -1 : 1;
        }
        if (i < o2.length) {
            return reverseResult ? 1 : -1;
        }
        return 0;
    }
}
