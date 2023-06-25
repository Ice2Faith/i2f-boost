package i2f.core.security.jce.encrypt;

/**
 * @author ltb
 * @date 2022/6/8 10:49
 * @desc
 */
public interface IEncryptType {
    String type();

    String algorithmName();

    boolean noPadding();

    boolean requireVector();

    int[] secretBytesLen();

    int[] vectorBytesLen();

    default boolean supportSecretBytesLen(int len) {
        int[] arr = secretBytesLen();
        if (arr == null) {
            return true;
        }
        for (int item : arr) {
            if (item == len) {
                return true;
            }
        }
        return false;
    }

    default boolean supportVectorBytesLen(int len) {
        int[] arr = vectorBytesLen();
        if (arr == null) {
            return true;
        }
        for (int item : arr) {
            if (item == len) {
                return true;
            }
        }
        return false;
    }
}
