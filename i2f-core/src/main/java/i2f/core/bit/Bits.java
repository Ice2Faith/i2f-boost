package i2f.core.bit;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

import java.util.Stack;

/**
 * @author ltb
 * @date 2022/3/1 9:00
 * @desc
 */
@Author("i2f")
@Remark("provide bit operate")
public class Bits {
    @Remark({
            "set bit value from {bit} index get {len} length for {num} using {val}",
            "{val} should is bit location equal",
            "num: 100111b",
            "val: 011000b",
            "bit: 3",
            "len: 2",
            "return: 111111b"
    })
    public static long bit(@Name("num") long num, @Name("bit") int bit, @Name("len") int len, @Name("val") long val) {
        long mk = mask(bit, len);
        long rmk = ~mk;
        num &= rmk;
        val = bit(val, bit, len);
        num |= val;
        return num;
    }

    @Remark({
            "set bit value from {bit} index get {len} length for {num} using {val}",
            "{val} should is bit lower",
            "num: 100111b",
            "val: 11b",
            "bit: 3",
            "len: 2",
            "return: 111111b"
    })
    public static long bitl(@Name("num") long num, @Name("bit") int bit, @Name("len") int len, @Name("val") long val) {
        long mk = mask(bit, len);
        long rmk = ~mk;
        num &= rmk;
        val = bitl(val, 0, len);
        num |= (val << bit);
        return num;
    }

    @Remark({
            "get bit value from {bit} index get {len} length for {num} using {val}",
            "{val} should is bit location equal",
            "num: 100111b",
            "bit: 3",
            "len: 2",
            "return: 011000b",
    })
    public static long bit(@Name("num") long num, @Name("bit") int bit, @Name("len") int len) {
        long mk = mask(bit, len);
        return num & mk;
    }
    @Remark({
            "get bit value from {bit} index get {len} length for {num} using {val}",
            "{val} should is bit location equal",
            "num: 100111b",
            "bit: 3",
            "len: 2",
            "return: 11b",
    })
    public static long bitl(@Name("num") long num, @Name("bit") int bit, @Name("len") int len) {
        long ret = bit(num, bit, len);
        ret >>>= bit;
        return ret;
    }

    @Remark({
            "get {len} bit count mask code lower",
            "{val} should is bit lower",
            "len: 2",
            "return: 11b",
    })
    public static long maskl(@Name("len") int len) {
        long ret = 0;
        for (int i = 0; i < len; i++) {
            ret <<= 1;
            ret |= 1;
        }
        return ret;
    }
    @Remark({
            "get {len} bit count mask code and left move {bit} count",
            "{val} should is bit location equal",
            "bit: 3",
            "len: 2",
            "return: 11000b",
    })
    public static long mask(@Name("bit") int bit, @Name("len") int len) {
        long ret = maskl(len);
        ret <<= bit;
        return ret;
    }

    @Remark({
            "{num} to binary string",
            "num:5",
            "return:1001"
    })
    public static String toBin(@Name("num") long num) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < 64 && num > 0; i++) {
            if ((num & 0x01) == 1) {
                stack.push("1");
            } else {
                stack.push("0");
            }
            num >>>= 1;
        }
        StringBuilder builder = new StringBuilder();
        while (!stack.isEmpty()) {
            builder.append(stack.pop());
        }
        return builder.toString();
    }

    @Remark({
            "binary string {num} to long",
            "num:1001",
            "return:5"
    })
    public static long formBin(@Name("num") String num) {
        long ret = 0;
        for (int i = 0; i < num.length(); i++) {
            ret *= 2;
            if (num.charAt(i) == '1') {
                ret += 1;
            } else {
                ret += 0;
            }
        }
        return ret;
    }

    public static boolean getBoolean(@Name("b") byte[] b, @Name("off") int off) {
        return b[off] != 0;
    }

    public static char getChar(@Name("b") byte[] b, @Name("off") int off) {
        return (char) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static short getShort(@Name("b") byte[] b, @Name("off") int off) {
        return (short) ((b[off + 1] & 0xFF) +
                (b[off] << 8));
    }

    public static int getInt(@Name("b") byte[] b, @Name("off") int off) {
        return ((b[off + 3] & 0xFF)) +
                ((b[off + 2] & 0xFF) << 8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off]) << 24);
    }

    public static float getFloat(@Name("b") byte[] b, @Name("off") int off) {
        return Float.intBitsToFloat(getInt(b, off));
    }

    public static long getLong(@Name("b") byte[] b, @Name("off") int off) {
        return ((b[off + 7] & 0xFFL)) +
                ((b[off + 6] & 0xFFL) << 8) +
                ((b[off + 5] & 0xFFL) << 16) +
                ((b[off + 4] & 0xFFL) << 24) +
                ((b[off + 3] & 0xFFL) << 32) +
                ((b[off + 2] & 0xFFL) << 40) +
                ((b[off + 1] & 0xFFL) << 48) +
                (((long) b[off]) << 56);
    }

    public static double getDouble(@Name("b") byte[] b, @Name("off") int off) {
        return Double.longBitsToDouble(getLong(b, off));
    }

    public static int putBoolean(@Name("b") byte[] b, @Name("off") int off, @Name("val") boolean val) {
        b[off] = (byte) (val ? 1 : 0);
        return 1;
    }

    public static int putChar(@Name("b") byte[] b, @Name("off") int off, @Name("val") char val) {
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
        return 2;
    }

    public static int putShort(@Name("b") byte[] b, @Name("off") int off, @Name("val") short val) {
        b[off + 1] = (byte) (val);
        b[off] = (byte) (val >>> 8);
        return 2;
    }

    public static int putInt(@Name("b") byte[] b, @Name("off") int off, @Name("val") int val) {
        b[off + 3] = (byte) (val);
        b[off + 2] = (byte) (val >>> 8);
        b[off + 1] = (byte) (val >>> 16);
        b[off] = (byte) (val >>> 24);
        return 4;
    }

    public static int putFloat(@Name("b") byte[] b, @Name("off") int off, @Name("val") float val) {
        return putInt(b, off, Float.floatToIntBits(val));
    }

    public static int putLong(@Name("b") byte[] b, @Name("off") int off, @Name("val") long val) {
        b[off + 7] = (byte) (val);
        b[off + 6] = (byte) (val >>> 8);
        b[off + 5] = (byte) (val >>> 16);
        b[off + 4] = (byte) (val >>> 24);
        b[off + 3] = (byte) (val >>> 32);
        b[off + 2] = (byte) (val >>> 40);
        b[off + 1] = (byte) (val >>> 48);
        b[off] = (byte) (val >>> 56);
        return 8;
    }

    public static int putDouble(@Name("b") byte[] b, @Name("off") int off, @Name("val") double val) {
        return putLong(b, off, Double.doubleToLongBits(val));
    }

}
