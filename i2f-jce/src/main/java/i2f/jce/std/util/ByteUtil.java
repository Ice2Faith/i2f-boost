package i2f.jce.std.util;

/**
 * @author Ice2Faith
 * @date 2024/3/27 9:01
 * @desc
 */
public class ByteUtil {
    public static boolean compare(byte[] arr1, byte[] arr2) {
        if (arr1 == arr2) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static String toHex(byte[] data) {
        StringBuilder builder = new StringBuilder();
        if (data == null) {
            return builder.toString();
        }
        for (byte item : data) {
            builder.append(String.format("%02x", item & 0x0ff));
        }
        return builder.toString();
    }

    public static byte[] ofHex(String hex) {
        if (hex == null || "".equals(hex)) {
            return new byte[0];
        }
        int len = hex.length();
        byte[] ret = new byte[len / 2];
        for (int i = 0; i < ret.length; i++) {
            int num = Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
            ret[i] = (byte) (num & 0x0ff);
        }
        return ret;
    }

    public static byte[] toBytes(long num) {
        byte[] ret = new byte[8];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte) ((num >>> ((7 - i) * 8)) & 0x0ff);
        }
        return ret;
    }

    public static long ofLong(byte[] data) {
        long ret = 0;
        if (data == null) {
            return ret;
        }
        for (int i = 0; i < 8; i++) {
            ret = ret << 8;
            ret = ret | (data[i] & 0x0ff);
        }
        return ret;
    }


}
