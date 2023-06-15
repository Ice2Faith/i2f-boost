package i2f.core.str;

/**
 * @author Ice2Faith
 * @date 2023/6/13 11:09
 * @desc
 */
public class CharsetUtil {

    public static String ofCharset(byte[] data, String charset) {
        try {
            return new String(data, charset);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] toCharset(String str, String charset) {
        try {
            return str.getBytes(charset);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String ofUtf8(byte[] data) {
        return ofCharset(data, "UTF-8");
    }

    public static byte[] toUtf8(String str) {
        return toCharset(str, "UTF-8");
    }

    public static String ofGbk(byte[] data) {
        return ofCharset(data, "GBK");
    }

    public static byte[] toGbk(String str) {
        return toCharset(str, "GBK");
    }
}
