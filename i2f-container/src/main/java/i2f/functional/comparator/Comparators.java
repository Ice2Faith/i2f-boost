package i2f.functional.comparator;


import i2f.functional.comparator.impl.ArrayComparator;
import i2f.functional.comparator.impl.DefaultComparator;
import i2f.functional.comparator.impl.NullableComparator;

/**
 * @author Ice2Faith
 * @date 2024/4/19 11:50
 * @desc 提供用于方法引用的比较操作
 * Comparators::compareInteger
 */
public class Comparators {

    public static <T> int compare(T o1, T o2) {
        return DefaultComparator.compareDefault(o1, o2);
    }

    public static int compareBoolean(boolean v1, boolean v2) {
        return Boolean.compare(v1, v2);
    }

    public static int compareBooleanObject(Boolean v1, Boolean v2) {
        return NullableComparator.compareNullable(v1, v2, Boolean::compare);
    }

    public static int compareByte(byte v1, byte v2) {
        return Byte.compare(v1, v2);
    }

    public static int compareByteObject(Byte v1, Byte v2) {
        return NullableComparator.compareNullable(v1, v2, Byte::compare);
    }

    public static int compareChar(char v1, char v2) {
        return Character.compare(v1, v2);
    }

    public static int compareCharObject(Character v1, Character v2) {
        return NullableComparator.compareNullable(v1, v2, Character::compare);
    }

    public static int compareDouble(double v1, double v2) {
        return Double.compare(v1, v2);
    }

    public static int compareDoubleObject(Double v1, Double v2) {
        return NullableComparator.compareNullable(v1, v2, Double::compare);
    }

    public static int compareFloat(float v1, float v2) {
        return Float.compare(v1, v2);
    }

    public static int compareFloatObject(Float v1, Float v2) {
        return NullableComparator.compareNullable(v1, v2, Float::compare);
    }

    public static int compareInteger(int v1, int v2) {
        return Integer.compare(v1, v2);
    }

    public static int compareIntegerObject(Integer v1, Integer v2) {
        return NullableComparator.compareNullable(v1, v2, Integer::compare);
    }

    public static int compareLong(long v1, long v2) {
        return Long.compare(v1, v2);
    }

    public static int compareLongObject(Long v1, Long v2) {
        return NullableComparator.compareNullable(v1, v2, Long::compare);
    }

    public static int compareShort(short v1, short v2) {
        return Short.compare(v1, v2);
    }

    public static int compareShortObject(Short v1, Short v2) {
        return NullableComparator.compareNullable(v1, v2, Short::compare);
    }

    public static <T> int compareArray(T[] v1, T[] v2) {
        return ArrayComparator.compareArray(v1, v2, DefaultComparator::compareDefault, false, false);
    }

    public static int compareBooleanArray(boolean[] v1, boolean[] v2) {
        return ArrayComparator.compareBooleanArray(v1, v2);
    }

    public static int compareByteArray(byte[] v1, byte[] v2) {
        return ArrayComparator.compareByteArray(v1, v2);
    }

    public static int compareCharArray(char[] v1, char[] v2) {
        return ArrayComparator.compareCharArray(v1, v2);
    }

    public static int compareDoubleArray(double[] v1, double[] v2) {
        return ArrayComparator.compareDoubleArray(v1, v2);
    }

    public static int compareFloatArray(float[] v1, float[] v2) {
        return ArrayComparator.compareFloatArray(v1, v2);
    }

    public static int compareIntArray(int[] v1, int[] v2) {
        return ArrayComparator.compareIntArray(v1, v2);
    }

    public static int compareLongArray(long[] v1, long[] v2) {
        return ArrayComparator.compareLongArray(v1, v2);
    }

    public static int compareBooleanArray(short[] v1, short[] v2) {
        return ArrayComparator.compareShortArray(v1, v2);
    }


}
