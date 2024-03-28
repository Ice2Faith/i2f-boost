package i2f.jce.jdk.encrypt.symmetric;

import i2f.jce.jdk.encrypt.Encryptor;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public interface SymmetricType {

    default String provider() {
        return null;
    }

    String type();

    default String algorithmName() {
        return Encryptor.cipherAlgorithm(type());
    }

    int[] secretBytesLen();

    int[] vectorBytesLen();

    boolean noPadding();

    boolean requireVector();
}
