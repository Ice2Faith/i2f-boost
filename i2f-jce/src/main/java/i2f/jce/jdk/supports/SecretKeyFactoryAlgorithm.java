package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum SecretKeyFactoryAlgorithm {

    AES("AES"),
    ARCFOUR("ARCFOUR"),
    DES("DES"),
    DESede("DESede"),

    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithHmacSHA256AndAES_128("PBEWithHmacSHA256AndAES_128"),

    PBKDF2WithHmacSHA256("PBKDF2WithHmacSHA256");

    private String text;

    SecretKeyFactoryAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
